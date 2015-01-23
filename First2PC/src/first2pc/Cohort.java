/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package first2pc;

import definitions.CohortState;
import definitions.Command;
import definitions.Params;
import gui.CohortFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import remote_interfaces.PeerInterface;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.Timer;
import simple_interfaces.ICohort;
import simple_interfaces.IFaultyProcess;

/**
 *
 * @author Lorenzo
 */
public class Cohort extends Node implements PeerInterface, ICohort, IFaultyProcess {

    private String name;
    private Coordinator coord;
    private ArrayList<Cohort> cohorts;
    private File log_file;
    private CohortState state;
    private CohortFrame frame;
    private boolean crashed;
    private Timer timer = null;
    private boolean shouldCommit = false;
    private boolean shouldAbort = false;

    public Cohort(String name, Coordinator coord) {
        super();
        this.name = name;
        this.coord = coord;
        this.state = CohortState.INIT;
        this.crashed = false;
        //setup logfile
        try {
            PeerInterface cohort = this;
            PeerInterface stub = (PeerInterface) UnicastRemoteObject.exportObject(cohort, 0);
            Registry registry = LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostName(), 5432);
            registry.rebind(name, stub);
            System.out.println("Cohort started and bounded with name: " + name);
        } catch (RemoteException ex) {
            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //da cambiare la firma, facciamo che i params sono un dizionario <String,Object>
    @Override
    public synchronized boolean processRequest(String command, Map<String, Object> params) throws RemoteException {
        boolean retval = false;
        JButton btn_yes = this.frame.getBtn_yes();
        JButton btn_no = this.frame.getBtn_no();
        JTextArea txt_area = this.frame.getConsolleTextArea();
        JLabel dec_label = this.frame.getDecisionEditLbl();

        //dealing with different requests
        switch (command) {
            case Command.REQUEST_VOTE:
                Integer vote = (Integer) params.get(Params.VOTE);
                txt_area.append("Received vote=" + vote + " request from Coord\n");
                dec_label.setText("" + vote);
                btn_yes.setEnabled(true);
                btn_no.setEnabled(true);
                retval = true;
                break;
            case Command.COMMIT:
                txt_area.append("canCommit received\n");
                setShouldCommit(true);
                if (state.equals(CohortState.INIT) || state.equals(CohortState.READY)) {
                    allowCommit();
                } else {
                    txt_area.append("Discard canCommit, we are not in INIT or READY state\n");
                }
                retval = true;
                break;
            case Command.ABORT:
                txt_area.append("shouldAbort received\n");
                setShouldAbort(true);
                if (state.equals(CohortState.INIT) || state.equals(CohortState.READY)) {
                    allowAbort();
                } else {
                    txt_area.append("Discard shouldAbort, we are not in INIT or READY state\n");
                }
                retval = true;
                break;
            case Command.TERMINATION:
                txt_area.append("termination request received. Replying true if deciced else false\n");
                if (state.equals(CohortState.ABORT) || state.equals(CohortState.COMMIT)) {
                    retval = true;
                } else {
                    retval = false;
                }
                break;
            case Command.GET_DECISION:
                if (state.equals(CohortState.COMMIT)) {
                    txt_area.append("sending COMMIT to sibling");
                    retval = true;
                } else {
                    txt_area.append("sending COMMIT to sibling");
                    retval = false;
                }
                break;
            default:
                retval = false;
                break;
        }

        return retval;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Cohort> getCohorts() {
        return cohorts;
    }

    public void setCohorts(ArrayList<Cohort> cohorts) {
        this.cohorts = cohorts;
    }

    public JFrame buildGUI() {
        JFrame frame = new CohortFrame(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Random r = new Random();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int) frame.getSize().getWidth();
//        int height = (int) frame.getSize().getHeight();
//        frame.setLocation(r.nextInt(screenSize.width - 2 * width) + width, r.nextInt(screenSize.height - 2 * height) + height);
        frame.pack();
        this.frame = (CohortFrame) frame;

        return frame;
    }

    public CohortFrame getFrame() {
        return frame;
    }

    public void setFrame(CohortFrame frame) {
        this.frame = frame;
    }

    public Coordinator getCoord() {
        return coord;
    }

    public CohortState getState() {
        return state;
    }

    @Override
    public synchronized boolean vote(final boolean answer, final String vote) {
        enterREADYstate();
        Map<String, Object> params = new HashMap<>();
        params.put(Params.YES_NO_VOTE, new Boolean(answer));
        params.put(Params.VOTE, vote);
        params.put(Params.NODE, this.getName());
        return sendMessage(Command.VOTE_REPLY, params, coord.getName());
    }

    @Override
    public synchronized boolean runTerminationProtocol() {

        boolean decided = false;
        for (Cohort cohort : cohorts) {
            if (cohort != this) {
                decided = sendMessage(Command.TERMINATION, null, cohort.getName());
                if (decided) {
                    boolean isCommitted = sendMessage(Command.GET_DECISION, null, cohort.getName());
                    if (isCommitted) {
                        frame.getConsolleTextArea().append("temination succed with COMMIT\n");
                        setShouldCommit(true);
                        allowCommit();
                    } else {
                        frame.getConsolleTextArea().append("temination succed with ABORT\n");
                        setShouldAbort(true);
                        allowAbort();
                    }
                    return true;
                }
            }
        }
        frame.getConsolleTextArea().append("temination failed all in READY state\n");
        return false;
    }

    @Override
    public synchronized boolean commit() {
        setState(CohortState.COMMIT);
        frame.displayCommit();
        return true;
    }

    @Override
    public synchronized boolean abort() {
        setState(CohortState.ABORT);
        frame.displayAbort();
        return true;
    }

    @Override
    public synchronized void crash() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void recover() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private synchronized boolean sendMessage(final String command, final Map<String, Object> params, final String dest_name) {
        boolean retval = false;
        Registry registry = null;
        PeerInterface rif2node = null;

        try {
            //get registry to contact coordinator
            registry = LocateRegistry.getRegistry(5432);
            rif2node = (PeerInterface) registry.lookup(dest_name);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Cohort.class.getName()).log(Level.SEVERE, null, ex);
        }
        final PeerInterface rif_to_node = rif2node;
        //prepare callable thread to manage vote sending
        Callable<Boolean> callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                Cohort.this.frame.getConsolleTextArea().append("sending " + command + " to " + dest_name + "\n");
                //simulazione tempo invio messaggio
                int sleep_time = new Random().nextInt(3000);
                Thread.sleep(sleep_time);
                boolean processRequest = rif_to_node.processRequest(command, params);
                Cohort.this.frame.getConsolleTextArea().append(command + " sent!\n");
                return processRequest;
            }
        };
        //da rivedere qui come lanciare le call
        FutureTask<Boolean> ft = new FutureTask<>(callable);
        //final ExecutorService executor = Executors.newFixedThreadPool(10);
        ft.run();
        while (!ft.isDone()) {
            //do nothing
        }
        try {
            retval = ft.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Cohort.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval;
    }

    private void enterREADYstate() {
        frame.getConsolleTextArea().append("enter READY state\n");

        //
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                //change cohort state
                setState(CohortState.READY);
                startTimer();
                Callable callable = new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {
                        //callable that periodically cheks if commit or abort message arrived
//                        Cohort.this.frame.getConsolleTextArea().append("Wait commit or abort message\n");
//                        Thread.sleep(3000);
//                        Cohort.this.frame.getConsolleTextArea().append("Something arrived!\n");
                        boolean stop = false;
                        int count = 0;
                        while (!stop) {
                            if (checkCommitOrAbort()) {
                                return true;
                            }
                            Cohort.this.frame.getConsolleTextArea().append("Wait commit or abort message\n");
                            Thread.sleep(2000);
                        }
                        return true;
                    }
                };

                ExecutorService executor = Executors.newFixedThreadPool(10);
                ArrayList<Callable<Boolean>> tasks = new ArrayList<>();
                tasks.add(callable);
                List<Future<Boolean>> invokeAll = null;
                try {
                    //nella realtà non c'è timeout, se l'utente vuole può provare a lanciare il termination protocol quando lo ritiene opportuno
                    invokeAll = executor.invokeAll(tasks, 11, TimeUnit.SECONDS);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                    //frame.getConsolleTextArea().append("Timeout expired run termination protocol\n");
                    Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                    stopTimer();
                    runTerminationProtocol();
                }
                //da qui in poi si può assumere che ft.isDone==true o perchè è finito ft o perchè è stato interrotto
                for (Future<Boolean> ft : invokeAll) {

                    if (!ft.isCancelled()) {
                        try {
                            if (ft.get() == true) {
                                stopTimer();
                            }
                        } catch (InterruptedException | ExecutionException ex) {
                            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        //handling cancellation, that means time out expired and task was cancelled
                        frame.getConsolleTextArea().append("Timeout expired run termination protocol\n");
                        stopTimer();
                        enterIndefiniteWaitstate();
                        return;
                    }
                }
                Cohort.this.frame.getConsolleTextArea().append("You have to follow your coordinator!\n");

            }
        });
        t.start();
        //
    }

    public void setState(CohortState state) {
        this.state = state;
    }

    private void stopTimer() {
        timer.stop();
        JProgressBar pb = frame.getPb_timeout();
        pb.setValue(pb.getMinimum());
    }

    private void enterIndefiniteWaitstate() {
        allowTerminationProtocol();
        JProgressBar pb = frame.getPb_timeout();
        pb.setIndeterminate(true);
        //callable task that checks if commit or abort arrived
        Callable callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                //callable that periodically cheks if commit or abort message arrived
//                        Cohort.this.frame.getConsolleTextArea().append("Wait commit or abort message\n");
//                        Thread.sleep(3000);
//                        Cohort.this.frame.getConsolleTextArea().append("Something arrived!\n");
                boolean stop = false;
                int count = 0;
                while (!stop) {
                    if (checkCommitOrAbort()) {
                        return true;
                    }
                    Cohort.this.frame.getConsolleTextArea().append("Wait indefinitely or try to perform termination protocol\n");
                    Thread.sleep(3000);
                }
                return true;
            }
        };
        FutureTask<Boolean> ft = new FutureTask<>(callable);
        ExecutorService executor = Executors.newFixedThreadPool(10);
//        ArrayList<Callable<Boolean>> tasks = new ArrayList<>();
//         tasks.add(callable);
//        List<Future<Boolean>> invokeAll = null;
//        try {
//            invokeAll = executor.invokeAll(tasks);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Cohort.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //check if every 3 seconds print indeterminate message to understand if controlling callable is beeing executed
        //executor.execute(ft);
        ft.run();
        while (!ft.isDone()) {
            //do nothing or perform termination protocol
        }
        pb.setIndeterminate(false);
        pb.setValue(pb.getMinimum());

    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JProgressBar pb = frame.getPb_timeout();

                int v = pb.getValue();
                v++;
                pb.setValue(v);
            }
        });
        timer.start();
    }

    public synchronized void setShouldCommit(boolean value) {
        this.shouldCommit = value;
    }

    public synchronized void setShouldAbort(boolean value) {
        this.shouldCommit = value;
    }

    private synchronized boolean checkCommitOrAbort() {
        return shouldAbort || shouldCommit;
    }

    private void allowAbort() {
        frame.allowAbort();
    }

    private void allowCommit() {
        frame.allowCommit();
    }

    private void allowTerminationProtocol() {
        frame.allowTerminationProtocol();
    }
}
