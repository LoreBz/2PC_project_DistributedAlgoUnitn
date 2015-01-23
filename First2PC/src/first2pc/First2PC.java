/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package first2pc;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Lorenzo
 */
//l'unica classe con un main parte tutto da qua
public class First2PC {

    public static void main(String[] args) {
        //the bootstrap of application: creation of rmi registry...
        bootstrap();
        //creation of coord and cohorts
        Coordinator coordinator = new Coordinator("coordinator");
        ArrayList<Cohort> cohorts = setupCohorts(coordinator);

        System.out.println("Coordinator and cohorts ready");
        //start GUI
        displayAll(coordinator, cohorts);

    }

    private static void bootstrap() {
        setLookAndFeel();
        try {
            //registry creation
            Registry registry = LocateRegistry.createRegistry(5432);
        } catch (RemoteException ex) {
            Logger.getLogger(First2PC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ArrayList<Cohort> setupCohorts(Coordinator coordinator) {
        ArrayList<Cohort> retval = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Cohort c = new Cohort("cohort_" + i, coordinator);
            retval.add(c);
        }
        for (Cohort c : retval) {
            c.setCohorts(retval);
        }
        coordinator.setCohorts(retval);
        return retval;
    }

    private static boolean setLookAndFeel() {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void displayAll(Coordinator coordinator, ArrayList<Cohort> cohorts) {
        final ArrayList<JFrame> frames = new ArrayList<>();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Random r = new Random();

        JFrame coordFrame = coordinator.buildGUI();
        int width = (int) coordFrame.getSize().getWidth();
        int height = (int) coordFrame.getSize().getHeight();
        coordFrame.setLocation(width / 4, height / 4);
        Point coord_point = coordFrame.getLocation();
        frames.add(coordFrame);
        Cohort previous = null;
        for (int i = 0; i < cohorts.size(); i++) {
            Cohort actual = cohorts.get(i);
            JFrame frame = actual.buildGUI();
            if (previous == null) {
                frame.setLocation((int) (coord_point.x + frame.getSize().getWidth() + 30), (int) (coord_point.y));
            } else {
                Point p = previous.getFrame().getLocation();
                frame.setLocation(p.x, (int) (p.y + frame.getSize().getHeight() + 20));
            }
            previous = actual;
            frames.add(frame);
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (JFrame frame : frames) {
                    frame.setVisible(true);
                }
            }
        });

    }
}
