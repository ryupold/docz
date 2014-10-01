/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;

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
        btnChangePW = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new docz.ContentPanel();
        jPanel1 = new javax.swing.JPanel();
        ckbDocs = new javax.swing.JCheckBox();
        ckbRelations = new javax.swing.JCheckBox();
        ckbInstitutions = new javax.swing.JCheckBox();
        ckbTags = new javax.swing.JCheckBox();
        spMaxResult = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ckbMinDate = new javax.swing.JCheckBox();
        ckbMaxDate = new javax.swing.JCheckBox();
        btnMinDate = new javax.swing.JButton();
        btnMaxDate = new javax.swing.JButton();

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

        btnChangePW.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnChangePW.setText("Change database PW");
        btnChangePW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePWActionPerformed(evt);
            }
        });

        contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Results"));
        contentPanel.setPreferredSize(null);
        jScrollPane1.setViewportView(contentPanel);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16))); // NOI18N

        ckbDocs.setSelected(true);
        ckbDocs.setText("Docs");

        ckbRelations.setSelected(true);
        ckbRelations.setText("Relations");

        ckbInstitutions.setSelected(true);
        ckbInstitutions.setText("Institutions");

        ckbTags.setSelected(true);
        ckbTags.setText("Tags");

        spMaxResult.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        spMaxResult.setModel(new javax.swing.SpinnerNumberModel(50, 1, 10000, 1));
        spMaxResult.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("max. results:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel2.setText("filter date:");

        ckbMinDate.setText("min:");
        ckbMinDate.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ckbMinDateStateChanged(evt);
            }
        });

        ckbMaxDate.setText("max:");
        ckbMaxDate.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ckbMaxDateStateChanged(evt);
            }
        });

        btnMinDate.setText("21.04.1988");
        btnMinDate.setEnabled(false);
        btnMinDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinDateActionPerformed(evt);
            }
        });

        btnMaxDate.setText("01.10.2014");
        btnMaxDate.setEnabled(false);
        btnMaxDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaxDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spMaxResult))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ckbDocs)
                                .addGap(18, 18, 18)
                                .addComponent(ckbInstitutions))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ckbRelations)
                                .addGap(18, 18, 18)
                                .addComponent(ckbTags)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(ckbMinDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(ckbMaxDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMaxDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMinDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbDocs)
                    .addComponent(ckbInstitutions))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbRelations)
                    .addComponent(ckbTags))
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spMaxResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(48, 48, 48)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbMinDate)
                    .addComponent(btnMinDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbMaxDate)
                    .addComponent(btnMaxDate))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //ckbDocs.setVisible(false);
        //ckbRelations.setVisible(false);
        //ckbInstitutions.setVisible(false);
        //ckbTags.setVisible(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnChangePW)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1231, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(103, 103, 103)
                        .addComponent(btnChangePW)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdd))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE))
                .addContainerGap())
        );

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
                findings = DataHandler.instance.search(txtSearch.getText().split(" "), 
                        ckbDocs.isSelected(), 
                        ckbInstitutions.isSelected(), 
                        ckbRelations.isSelected(), 
                        ckbTags.isSelected(), 
                        ckbMinDate.isSelected() ? DateFormat.getDateInstance().parse(btnMinDate.getText()) : null,
                        ckbMaxDate.isSelected() ? DateFormat.getDateInstance().parse(btnMaxDate.getText()) : null,
                        true,
                        ((Integer)spMaxResult.getModel().getValue()));
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

    private void btnMinDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinDateActionPerformed
        JDatePanel datePanel = (JDatePanelImpl) JDateComponentFactory.createJDatePanel();
        int okCxl = JOptionPane.showConfirmDialog(null, datePanel, "Datum auswählen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            DateModel<?> dm = ((JDatePanel) datePanel).getModel();
            btnMinDate.setText(dm.getDay() + "." + (1 + dm.getMonth()) + "." + dm.getYear());
        }
    }//GEN-LAST:event_btnMinDateActionPerformed

    private void btnMaxDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaxDateActionPerformed
        JDatePanel datePanel = (JDatePanelImpl) JDateComponentFactory.createJDatePanel();
        int okCxl = JOptionPane.showConfirmDialog(null, datePanel, "Datum auswählen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            DateModel<?> dm = ((JDatePanel) datePanel).getModel();
            btnMaxDate.setText(dm.getDay() + "." + (1 + dm.getMonth()) + "." + dm.getYear());
        }
    }//GEN-LAST:event_btnMaxDateActionPerformed

    private void ckbMinDateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ckbMinDateStateChanged
        btnMinDate.setEnabled(ckbMinDate.isSelected());
    }//GEN-LAST:event_ckbMinDateStateChanged

    private void ckbMaxDateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ckbMaxDateStateChanged
        btnMaxDate.setEnabled(ckbMaxDate.isSelected());
    }//GEN-LAST:event_ckbMaxDateStateChanged

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
    private javax.swing.JButton btnMaxDate;
    private javax.swing.JButton btnMinDate;
    private javax.swing.JCheckBox ckbDocs;
    private javax.swing.JCheckBox ckbInstitutions;
    private javax.swing.JCheckBox ckbMaxDate;
    private javax.swing.JCheckBox ckbMinDate;
    private javax.swing.JCheckBox ckbRelations;
    private javax.swing.JCheckBox ckbTags;
    private docz.ContentPanel contentPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spMaxResult;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
