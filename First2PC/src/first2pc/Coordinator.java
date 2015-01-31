/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package first2pc;

import definitions.Command;
import definitions.CoordState;
import definitions.Params;
import gui.CohortFrame;
import gui.CoordFrame;
import remote_interfaces.PeerInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.Timer;
import simple_interfaces.ICoordinator;
import simple_interfaces.IFaultyProcess;

/**
 *
 * @author Lorenzo
 */
public class Coordinator extends Node implements PeerInterface, ICoordinator, IFaultyProcess {

    private String name;
    private ArrayList<Cohort> cohorts;
    private File log_file;
    private CoordState state;
    private CoordFrame frame;
    private boolean crashed;
    private Timer timer = null;
    private Map<String, String> votes;

    public Coordinator(String name) {
        super();
        this.name = name;
        this.state = CoordState.INIT;
        this.crashed = false;
        //setup logfile
        try {
            PeerInterface coord = this;
            PeerInterface stub = (PeerInterface) UnicastRemoteObject.exportObject(coord, 0);
            Registry registry = LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostName(), 5432);
            registry.rebind(name, stub);
            System.out.println("Coord started and bounded with name: " + name);
        } catch (RemoteException ex) {
            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized ArrayList<Boolean> broadcastMessage(final String command, final Map<String, Object> params) {
        ArrayList<Boolean> retval = new ArrayList<>();
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        //get the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(5432);
        } catch (RemoteException ex) {
            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
        }

        //for each cohort prepare a request callable thread
        for (Cohort c : cohorts) {
            final String c_name = c.getName();
            PeerInterface rif2c = null;
            try {
                rif2c = (PeerInterface) registry.lookup(c_name);
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
            }
            //preparazione callable
            final PeerInterface rif_2_c = rif2c;
            Callable<Boolean> task = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    JTextArea txtarea = Coordinator.this.frame.getConsolleTextArea();
                    txtarea.append("sending " + command + " to " + c_name + "\n");
                    int sleep_sec = new Random().nextInt(3000);
                    Thread.sleep(sleep_sec);
                    boolean b = rif_2_c.processRequest(command, params);
                    txtarea.append("answer from " + c_name + " about " + command + " received: " + b + "\n");
                    return b;
                }
            }; //end for

            tasks.add(task);
        }
        List<Future<Boolean>> invokeAll = null;
        try {
            //execute all the tasks simultaneously blocking until all done or exception
            invokeAll = executor.invokeAll(tasks);
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException");
        } catch (Exception e) {
            System.out.println("Ho beccato l'eccezione timeout?");
        }

        //azione se tutto va bene
        for (Future<Boolean> ft : invokeAll) {
            if (!ft.isCancelled()) {
                try {
                    retval.add(ft.get());
                } catch (InterruptedException | ExecutionException | CancellationException ex) {
                    Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ft.cancel(true);
            }

        }

        executor.shutdownNow();

        CoordFrame f = (CoordFrame) this.frame;
        f.getConsolleTextArea().append("end broadcast\n");

        return retval;
    }

    public synchronized ArrayList<Boolean> multicastMessage(final String command, final Map<String, Object> params, List<String> target) {
        ArrayList<Boolean> retval = new ArrayList<>();
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        //get the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(5432);
        } catch (RemoteException ex) {
            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
        }

        //for each cohort prepare a request callable thread
        for (String c : target) {
            final String c_name = c;
            PeerInterface rif2c = null;
            try {
                rif2c = (PeerInterface) registry.lookup(c_name);
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
            }
            //preparazione callable
            final PeerInterface rif_2_c = rif2c;
            Callable<Boolean> task = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    JTextArea txtarea = Coordinator.this.frame.getConsolleTextArea();
                    txtarea.append("sending " + command + " to " + c_name + "\n");
                    int sleep_sec = new Random().nextInt(3000);
                    Thread.sleep(sleep_sec);
                    boolean b = rif_2_c.processRequest(command, params);
                    txtarea.append("answer from " + c_name + " about " + command + " received: " + b + "\n");
                    return b;
                }
            }; //end for

            tasks.add(task);
        }
        List<Future<Boolean>> invokeAll = null;
        try {
            //execute all the tasks simultaneously blocking until all done or exception
            invokeAll = executor.invokeAll(tasks);
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException");
        } catch (Exception e) {
            System.out.println("Ho beccato l'eccezione timeout?");
        }

        //azione se tutto va bene
        for (Future<Boolean> ft : invokeAll) {
            if (!ft.isCancelled()) {
                try {
                    retval.add(ft.get());
                } catch (InterruptedException | ExecutionException | CancellationException ex) {
                    Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ft.cancel(true);
            }

        }

        executor.shutdownNow();

        CoordFrame f = (CoordFrame) this.frame;
        f.getConsolleTextArea().append("end broadcast\n");

        return retval;
    }

    public JFrame buildGUI() {
        JFrame frame = new CoordFrame(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Random r = new Random();
        //  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int) frame.getSize().getWidth();
//        int height = (int) frame.getSize().getHeight();
//        frame.setLocation(r.nextInt(screenSize.width - 2 * width) + width, r.nextInt(screenSize.height - 2 * height) + height);
        this.frame = (CoordFrame) frame;
        return frame;
    }

    public ArrayList<Cohort> getCohorts() {
        return cohorts;
    }

    public void setCohorts(ArrayList<Cohort> cohorts) {
        this.cohorts = cohorts;
    }

    public CoordFrame getFrame() {
        return frame;
    }

    public void setFrame(CoordFrame frame) {
        this.frame = frame;
    }

    @Override
    public synchronized boolean processRequest(String command, Map<String, Object> params) throws RemoteException {
        boolean retval = false;
        //dealing with different requests

        switch (command) {
            case Command.VOTE_REPLY:

                boolean reply = (boolean) params.get(Params.YES_NO_VOTE);
                String vote = (String) params.get(Params.VOTE);
                String node_name = (String) params.get(Params.NODE);
                System.out.println("Coord: tryng to add vote=" + reply + " (request=" + vote + ")");
                if (reply) {
                    addVote(node_name, vote);

                } else {
                    addVote(node_name, Command.ABORT);
                }
                retval = true;
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

    public CoordState getState() {
        return state;
    }

    @Override
    public synchronized boolean sendRequest(String command, Map<String, Object> params) {
        Integer v = (Integer) params.get(Params.VOTE);
        enterWAITstate("" + v);

        //può metterci fino a 3 sec
        Boolean br = (Boolean) params.get(Params.BROADCAST);
        if (br != null && br == false) {
            List<String> target = (List<String>) params.get(Params.COHORTS_NAMES);
            multicastMessage(command, params, target);
        } else {
            broadcastMessage(command, params);
        }
        return true;

    }

    @Override
    public synchronized boolean commit(String command, Map<String, Object> params) {

        enterCOMMITstate();
        Boolean br = null;
        try {
            br = (Boolean) params.get(Params.BROADCAST);
        } catch (Exception e) {
            //me ne frego se non c'è
        }
        if (br != null && br == false) {
            List<String> target = (List<String>) params.get(Params.COHORTS_NAMES);
            multicastMessage(command, params, target);
        } else {
            broadcastMessage(command, params);
        }
        return true;
    }

    @Override
    public synchronized boolean abort(String command, Map<String, Object> params) {
        enterABORTstate();
        Boolean br = null;
        try {
            br = (Boolean) params.get(Params.BROADCAST);
        } catch (Exception e) {
            //me ne frego se non c'è
        }

        if (br != null && br == false) {
            List<String> target = (List<String>) params.get(Params.COHORTS_NAMES);
            multicastMessage(command, params, target);
        } else {
            broadcastMessage(command, params);
        }
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

    public void setState(CoordState state) {
        this.state = state;
    }

    private void stopTimer() {
        timer.stop();
        JProgressBar pb = frame.getPb_timeout();
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

    private void enterWAITstate(String vote) {
        votes = new HashMap<>();
        addVote(this.getName(), vote);
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                //change coordinator state
                setState(CoordState.WAIT);
                startTimer();
                Callable callable = new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {
                        boolean stop = false;
                        while (!stop) {
                            if (abortReceived()) {
                                frame.getConsolleTextArea().append("vote NO received, should ABORT\n");
//                                stopTimer();
//                                abort(Command.ABORT, null);
                                return false;
                            }
                            if (checkVotes()) {
                                frame.getConsolleTextArea().append((votes.size() - 1) + "/" + cohorts.size() + " votes received!\n");
                                return true;
                            } else {
                                frame.getConsolleTextArea().append("Waiting cohorts decisions (Received " + (votes.size() - 1) + "/" + cohorts.size() + " votes)\n");
                            }
                            Thread.sleep(2000);
                        }
                        return true;
                    }
                };
                //FutureTask<Boolean> ft = new FutureTask<>(callable);
                ExecutorService executor = Executors.newFixedThreadPool(10);
                //executor.execute(ft);
                ArrayList<Callable<Boolean>> tasks = new ArrayList<>();
                tasks.add(callable);
                List<Future<Boolean>> invokeAll = null;
                try {
                    invokeAll = executor.invokeAll(tasks, 11, TimeUnit.SECONDS);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                    // frame.getConsolleTextArea().append("Timeout expired go to abort automatically\n");
                    //System.out.println("I voti non sono arrivati in tempo");
                    Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                    stopTimer();
                    abort(Command.ABORT, null);
                }
                for (Future<Boolean> ft : invokeAll) {
                    if (!ft.isCancelled()) {
                        try {
                            if (ft.get() == true) {
                                stopTimer();
                                //allowCommitOrAbort();
                                allowCommit();
                            } else {
                                //il caso in cui è arrivato un abort message dai cohort
                                stopTimer();
                                allowAbort();
                                return;
                            }
                        } catch (InterruptedException | ExecutionException ex) {
                            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        //handling cancellation, that means time out expired and task was cancelled
                        frame.getConsolleTextArea().append("Timeout expired you have to abort automatically\n");
                        stopTimer();
                        allowAbort();
                        return;
                    }
                }
                Coordinator.this.frame.getConsolleTextArea().append("Now you can commit!\n");

            }
        });
        t.start();
    }

    private synchronized boolean abortReceived() {
        if (votes != null && !votes.isEmpty()) {
            for (String vote : votes.values()) {
                if (vote.equals(Command.ABORT)) {
                    return true;
                }
            }
        }
        return false;
    }

    private synchronized boolean checkVotes() {
        if (votes != null && !votes.isEmpty()) {
            String coord_vote = votes.get(this.getName());
            if (votes.size() == cohorts.size() + 1) {
                boolean b = true;
                for (String vote : votes.values()) {
                    b = b && (vote.equals(coord_vote));
                }
                if (b) {
                    return true;
                }
            }
        }
        return false;
    }

    private synchronized void addVote(String node_name, String vote) {

        votes.put(node_name, vote);

    }

    private void allowCommitOrAbort() {
        frame.allowCommitOrAbort();
    }

    private void allowAbort() {
        frame.allowAbort();
    }

    private void allowCommit() {
        frame.allowCommit();
    }

    private void enterCOMMITstate() {
        setState(CoordState.COMMIT);
        frame.displayCommit();

    }

    private void enterABORTstate() {
        setState(CoordState.ABORT);
        frame.displayAbort();
    }

}
