/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import first2pc.Cohort;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Lorenzo
 */
public class CohortFrame extends javax.swing.JFrame {

    private Cohort cohort;

    /**
     * Creates new form CohortFrame
     */
    public CohortFrame(Cohort cohort) {
        this.cohort = cohort;
        initComponents();
        myInitComponents();
    }

    private void myInitComponents() {
        DefaultCaret caret = (DefaultCaret) this.consolleTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.componentName.setText(cohort.getName());
        String s = "Coordinator: " + cohort.getCoord().getName() + "\n";
        for (Cohort c : cohort.getCohorts()) {
            s += c.getName() + "\n";
        }
        this.processesTextArea.append(s);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        componentName = new javax.swing.JLabel();
        label_deicision = new javax.swing.JLabel();
        decisionEditLbl = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        State = new javax.swing.JLabel();
        init_lbl = new javax.swing.JLabel();
        ready_lbl = new javax.swing.JLabel();
        abort_lbl = new javax.swing.JLabel();
        commit_lbl = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        consolleTextArea = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        btn_commit = new javax.swing.JButton();
        btn_abort = new javax.swing.JButton();
        btn_yes = new javax.swing.JButton();
        btn_no = new javax.swing.JButton();
        btn_terminationProt = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        processes_lbl = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        processesTextArea = new javax.swing.JTextArea();
        pb_timeout = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(500, 450));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        componentName.setText("Cohort_1");

        label_deicision.setText("vote: ");

        decisionEditLbl.setText("???");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(componentName)
                .addGap(43, 43, 43)
                .addComponent(label_deicision)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decisionEditLbl)
                .addGap(70, 70, 70))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(componentName)
                    .addComponent(label_deicision)
                    .addComponent(decisionEditLbl))
                .addContainerGap(75, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 153, 102));

        State.setText("STATE:");

        init_lbl.setText("INIT");

        ready_lbl.setText("READY");
        ready_lbl.setEnabled(false);

        abort_lbl.setText("ABORT");
        abort_lbl.setEnabled(false);

        commit_lbl.setText("COMMIT");
        commit_lbl.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(State)
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ready_lbl)
                            .addComponent(init_lbl)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(abort_lbl)
                        .addGap(49, 49, 49)
                        .addComponent(commit_lbl)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(State)
                    .addComponent(init_lbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ready_lbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(abort_lbl)
                    .addComponent(commit_lbl))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 153));

        consolleTextArea.setColumns(20);
        consolleTextArea.setRows(5);
        jScrollPane1.setViewportView(consolleTextArea);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btn_commit.setText("COMMIT");
        btn_commit.setEnabled(false);
        btn_commit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_commitActionPerformed(evt);
            }
        });

        btn_abort.setText("ABORT");
        btn_abort.setEnabled(false);
        btn_abort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_abortActionPerformed(evt);
            }
        });

        btn_yes.setText("YES");
        btn_yes.setEnabled(false);
        btn_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_yesActionPerformed(evt);
            }
        });

        btn_no.setText("NO");
        btn_no.setEnabled(false);
        btn_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_noActionPerformed(evt);
            }
        });

        btn_terminationProt.setText("Termination P");
        btn_terminationProt.setEnabled(false);
        btn_terminationProt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_terminationProtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_yes)
                .addGap(18, 18, 18)
                .addComponent(btn_no)
                .addGap(18, 18, 18)
                .addComponent(btn_terminationProt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_commit)
                .addGap(18, 18, 18)
                .addComponent(btn_abort)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_yes)
                    .addComponent(btn_no)
                    .addComponent(btn_abort)
                    .addComponent(btn_commit)
                    .addComponent(btn_terminationProt))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        processes_lbl.setText("Known Processes");

        processesTextArea.setEditable(false);
        processesTextArea.setColumns(20);
        processesTextArea.setRows(5);
        jScrollPane2.setViewportView(processesTextArea);

        pb_timeout.setMaximum(10);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(processes_lbl)
                .addContainerGap(188, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pb_timeout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(processes_lbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pb_timeout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_yesActionPerformed
        //manage UI and start a cohort.vote() in a new thread
        //UI management
        btn_yes.setEnabled(false);
        btn_no.setEnabled(false);
        init_lbl.setEnabled(false);
        ready_lbl.setEnabled(true);
        consolleTextArea.append("sending YES to coord\n");
        final String vote = decisionEditLbl.getText();
        //vote call
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                cohort.vote(true, vote);
            }
        });
        t.start();

    }//GEN-LAST:event_btn_yesActionPerformed

    private void btn_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_noActionPerformed
        //manage UI and start a cohort.vote() in a new thread
        //UI management
        //cohort.allowAbort();
        btn_yes.setEnabled(false);
        btn_no.setEnabled(false);
        init_lbl.setEnabled(false);
        ready_lbl.setEnabled(true);
        consolleTextArea.append("sending NO to coord\n");
        final String vote = decisionEditLbl.getText();
        //vote call
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                cohort.vote(false, vote);
            }
        });
        t.start();
        consolleTextArea.append("go in ready state\n");
    }//GEN-LAST:event_btn_noActionPerformed

    private void btn_commitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_commitActionPerformed
        final Cohort c = cohort;
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                c.commit();
            }
        });
        t.start();
    }//GEN-LAST:event_btn_commitActionPerformed

    private void btn_abortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_abortActionPerformed
        final Cohort c = cohort;
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                c.abort();
            }
        });
        t.start();
    }//GEN-LAST:event_btn_abortActionPerformed

    private void btn_terminationProtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_terminationProtActionPerformed
        // TODO add your handling code here:
         final Cohort c = cohort;
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                c.runTerminationProtocol();
            }
        });
        t.start();
    }//GEN-LAST:event_btn_terminationProtActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel State;
    private javax.swing.JLabel abort_lbl;
    private javax.swing.JButton btn_abort;
    private javax.swing.JButton btn_commit;
    private javax.swing.JButton btn_no;
    private javax.swing.JButton btn_terminationProt;
    private javax.swing.JButton btn_yes;
    private javax.swing.JLabel commit_lbl;
    private javax.swing.JLabel componentName;
    private javax.swing.JTextArea consolleTextArea;
    private javax.swing.JLabel decisionEditLbl;
    private javax.swing.JLabel init_lbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label_deicision;
    private javax.swing.JProgressBar pb_timeout;
    private javax.swing.JTextArea processesTextArea;
    private javax.swing.JLabel processes_lbl;
    private javax.swing.JLabel ready_lbl;
    // End of variables declaration//GEN-END:variables

    public Cohort getCohort() {
        return cohort;
    }

    public void setCohort(Cohort cohort) {
        this.cohort = cohort;
    }

   

    public JButton getBtn_no() {
        return btn_no;
    }

    public void setBtn_no(JButton btn_no) {
        this.btn_no = btn_no;
    }

   

    public JButton getBtn_yes() {
        return btn_yes;
    }

    public void setBtn_yes(JButton btn_yes) {
        this.btn_yes = btn_yes;
    }

    public JTextArea getConsolleTextArea() {
        return consolleTextArea;
    }

    public void setConsolleTextArea(JTextArea consolleTextArea) {
        this.consolleTextArea = consolleTextArea;
    }

    public JLabel getDecisionEditLbl() {
        return decisionEditLbl;
    }

    public void setDecisionEditLbl(JLabel decisionEditLbl) {
        this.decisionEditLbl = decisionEditLbl;
    }

    public JProgressBar getPb_timeout() {
        return pb_timeout;
    }

    public void allowAbort() {
        init_lbl.setEnabled(false);
        ready_lbl.setEnabled(false);
        abort_lbl.setEnabled(true);
        abort_lbl.setForeground(Color.red);
        btn_abort.setEnabled(true);
        btn_commit.setEnabled(false);
        btn_no.setEnabled(false);
        btn_yes.setEnabled(false);
        btn_terminationProt.setEnabled(false);
    }

    public void allowCommit() {
        init_lbl.setEnabled(false);
        ready_lbl.setEnabled(false);
        commit_lbl.setEnabled(true);
        commit_lbl.setForeground(Color.red);
        btn_abort.setEnabled(false);
        btn_commit.setEnabled(true);
        btn_no.setEnabled(false);
        btn_yes.setEnabled(false);
        btn_terminationProt.setEnabled(false);
    }

    public void displayAbort() {
        consolleTextArea.append("ABORT!\n");
        btn_abort.setEnabled(false);
        abort_lbl.setForeground(Color.BLUE);
        abort_lbl.setEnabled(true);
        init_lbl.setEnabled(false);
        ready_lbl.setEnabled(false);
    }

    public void displayCommit() {
        consolleTextArea.append("COMMIT!\n");
        btn_commit.setEnabled(false);
        commit_lbl.setForeground(Color.BLUE);
    }

    public void allowTerminationProtocol() {
        btn_terminationProt.setEnabled(true);
    }

}
