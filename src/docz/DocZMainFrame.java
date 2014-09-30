/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author Michael
 */
public class DocZMainFrame extends javax.swing.JFrame {

    private WaitDialog.AsyncProcess searchProgress = null;

    /**
     * Creates new form DocZMainFrame
     */
    public DocZMainFrame() {
        initComponents();

        setTitle("DocZ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Log.l("DB file = " + DataHandler.instance);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    if (!DataHandler.instance.testConnection()) {
                        JPasswordField pf = new JPasswordField();
                        int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter password of the AES-encrypted database file.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (okCxl == JOptionPane.OK_OPTION) {
                            String password = new String(pf.getPassword());
                            DB.setPW(password);
                            DataHandler.instance.testConnection();
                            DataHandler.instance.init();
                        } else {
                            JOptionPane.showConfirmDialog(null, "This database is encrypted, you need a password to proceed", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
                            System.exit(1);
                        }
                    }
                } catch (SQLException ex) {
                    if (ex.getMessage().contains("Encryption error")) {
                        boolean canLeave = false;
                        while (!canLeave) {
                            try {
                                JPasswordField pf = new JPasswordField();
                                int okCxl = JOptionPane.showConfirmDialog(null, pf, "Wrong password! Please enter the right one.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                                if (okCxl == JOptionPane.OK_OPTION) {
                                    String password = new String(pf.getPassword());
                                    DB.setPW(password);
                                    DataHandler.instance.testConnection();
                                    DataHandler.instance.init();
                                } else {
                                    JOptionPane.showConfirmDialog(null, "This database is encrypted, you need a password to proceed", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
                                    System.exit(1);
                                }

                                canLeave = true;
                            } catch (SQLException ise) {
                                if (!ise.getMessage().contains("Encryption error")) {
                                    canLeave = true;
                                }
                            }
                        }
                    }
                } finally {
                    //initial search
                    txtSearchKeyTyped(null);
                }
            }

        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method
     * is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSearch = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        ckbDocs = new javax.swing.JCheckBox();
        ckbRelations = new javax.swing.JCheckBox();
        ckbTags = new javax.swing.JCheckBox();
        ckbInstitutions = new javax.swing.JCheckBox();
        btnChangePW = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new docz.ContentPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtSearch.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtSearch.setToolTipText("Search...");
        txtSearch.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtSearchInputMethodTextChanged(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        btnAdd.setText("+");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        ckbDocs.setSelected(true);
        ckbDocs.setText("Docs");

        ckbRelations.setSelected(true);
        ckbRelations.setText("Relations");

        ckbTags.setSelected(true);
        ckbTags.setText("Tags");

        ckbInstitutions.setSelected(true);
        ckbInstitutions.setText("Institutions");

        btnChangePW.setText("Change database PW");
        btnChangePW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePWActionPerformed(evt);
            }
        });

        contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Results"));
        contentPanel.setPreferredSize(null);
        jScrollPane1.setViewportView(contentPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ckbDocs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckbRelations)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckbInstitutions))
                    .addComponent(ckbTags)
                    .addComponent(btnChangePW)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdd)
                        .addGap(23, 23, 23)
                        .addComponent(btnChangePW)
                        .addGap(87, 87, 87)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ckbDocs)
                            .addComponent(ckbRelations)
                            .addComponent(ckbInstitutions))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckbTags)
                        .addGap(0, 328, Short.MAX_VALUE)))
                .addContainerGap())
        );

        ckbDocs.setVisible(false);
        ckbRelations.setVisible(false);
        ckbTags.setVisible(false);
        ckbInstitutions.setVisible(false);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtSearchInputMethodTextChanged
        //TODO???
    }//GEN-LAST:event_txtSearchInputMethodTextChanged

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        if (searchProgress != null && !searchProgress.isFinished()) {
            searchProgress.cancel(); //concurrency problems????
        }

        searchProgress = new WaitDialog.AsyncProcess("search") {
            Entity[] findings = null;
            private boolean canceled = false;

            @Override
            public void start() throws Exception {
                findings = DataHandler.instance.search(txtSearch.getText().split(" "), ckbDocs.isSelected(), ckbInstitutions.isSelected(), ckbRelations.isSelected(), ckbTags.isSelected(), DataHandler.DEFAULT_LIMIT);
            }

            @Override
            public void finished(boolean success) {
                try {
                    if (!canceled && findings != null) {
                        contentPanel.showResults(findings);
                    }
                } catch (Exception ex) {
                    Log.l(ex);
                }
            }

            @Override
            public void cancel() {
                canceled = true;
            }

        };
        new WaitDialog(null, searchProgress, true, false);
    }//GEN-LAST:event_txtSearchKeyTyped

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        AddDialog addDialog = new AddDialog(this, true);
        addDialog.dispose();
        System.gc();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnChangePWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePWActionPerformed
        ChangePassword pwDialog = new ChangePassword(this, true);
        pwDialog.setVisible(true);
        pwDialog.dispose();
        System.gc();
        DataHandler.instance.init();
    }//GEN-LAST:event_btnChangePWActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DocZMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DocZMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DocZMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DocZMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DocZMainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnChangePW;
    private javax.swing.JCheckBox ckbDocs;
    private javax.swing.JCheckBox ckbInstitutions;
    private javax.swing.JCheckBox ckbRelations;
    private javax.swing.JCheckBox ckbTags;
    private docz.ContentPanel contentPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
