/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Color;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *
 * @author Michael
 */
public class ChangePassword extends javax.swing.JDialog {

    private boolean firstEncrypt = false;

    /**
     * Creates new form ChangePassword
     */
    public ChangePassword(java.awt.Frame parent, boolean modal) throws SQLException {
        super(parent, modal);
        initComponents();

        if (DB.needPW()) {
            firstEncrypt = false;
        } else {
            firstEncrypt = true;
        }
        
        pwdOldPW.setEnabled(!firstEncrypt);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pwdNewPW = new javax.swing.JPasswordField();
        pwdConfirm = new javax.swing.JPasswordField();
        pwdOldPW = new javax.swing.JPasswordField();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Change database password");

        btnOK.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnOK.setText("OK");
        btnOK.setEnabled(false);
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Old Password:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("New Password:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Confirm:");

        pwdNewPW.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        pwdNewPW.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                pwdNewPWCaretUpdate(evt);
            }
        });
        pwdNewPW.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                pwdNewPWInputMethodTextChanged(evt);
            }
        });
        pwdNewPW.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwdNewPWKeyTyped(evt);
            }
        });

        pwdConfirm.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        pwdConfirm.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                pwdConfirmCaretUpdate(evt);
            }
        });
        pwdConfirm.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                pwdConfirmInputMethodTextChanged(evt);
            }
        });
        pwdConfirm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwdConfirmKeyTyped(evt);
            }
        });

        pwdOldPW.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        pwdOldPW.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                pwdOldPWCaretUpdate(evt);
            }
        });
        pwdOldPW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdOldPWActionPerformed(evt);
            }
        });
        pwdOldPW.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                pwdOldPWCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                pwdOldPWInputMethodTextChanged(evt);
            }
        });
        pwdOldPW.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwdOldPWKeyTyped(evt);
            }
        });

        lblStatus.setText("Type in a new password for the database file");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOK)
                        .addGap(18, 18, 18)
                        .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(76, 76, 76)
                        .addComponent(pwdConfirm))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pwdOldPW)
                            .addComponent(pwdNewPW))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pwdOldPW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(pwdNewPW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(pwdConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(lblStatus))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pwdOldPWInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_pwdOldPWInputMethodTextChanged

    }//GEN-LAST:event_pwdOldPWInputMethodTextChanged

    private void checkFields() {
        btnOK.setEnabled(false);
        lblStatus.setForeground(Color.red);
        if (pwdOldPW.getPassword().length == 0 && !firstEncrypt) {
            lblStatus.setText("Enter the old password");
        } else if ((firstEncrypt || pwdOldPW.getPassword().length > 0) && pwdNewPW.getPassword().length == 0) {
            lblStatus.setText("Enter the new password");
        } else if ((firstEncrypt || pwdOldPW.getPassword().length > 0) && pwdNewPW.getPassword().length > 0 && pwdConfirm.getPassword().length == 0) {
            lblStatus.setText("Enter the new again for confirmation");
        } else if ((firstEncrypt || pwdOldPW.getPassword().length > 0) && pwdNewPW.getPassword().length > 0 && pwdConfirm.getPassword().length > 0) {
            if (Arrays.equals(pwdNewPW.getPassword(), pwdConfirm.getPassword())) {
                lblStatus.setText("Seems legit, press OK to proceed.");
                lblStatus.setForeground(Color.green);
                btnOK.setEnabled(true);
            } else {
                lblStatus.setText("The new password and the confirmation of it are not equal!");
            }
        }
    }

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        try {
            if (DB.changePW(firstEncrypt ? null : new String(pwdOldPW.getPassword()), new String(pwdConfirm.getPassword()))) {
                setVisible(false);
            } else {
                lblStatus.setForeground(Color.red);
                lblStatus.setText("Old password was wrong, decryption failed");
            }
        } catch (SQLException ex) {
            Log.l(ex);
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void pwdNewPWInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_pwdNewPWInputMethodTextChanged

    }//GEN-LAST:event_pwdNewPWInputMethodTextChanged

    private void pwdConfirmInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_pwdConfirmInputMethodTextChanged

    }//GEN-LAST:event_pwdConfirmInputMethodTextChanged

    private void pwdOldPWKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdOldPWKeyTyped

    }//GEN-LAST:event_pwdOldPWKeyTyped

    private void pwdNewPWKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdNewPWKeyTyped

    }//GEN-LAST:event_pwdNewPWKeyTyped

    private void pwdConfirmKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdConfirmKeyTyped

    }//GEN-LAST:event_pwdConfirmKeyTyped

    private void pwdOldPWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdOldPWActionPerformed

    }//GEN-LAST:event_pwdOldPWActionPerformed

    private void pwdOldPWCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_pwdOldPWCaretPositionChanged

    }//GEN-LAST:event_pwdOldPWCaretPositionChanged

    private void pwdOldPWCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_pwdOldPWCaretUpdate
        checkFields();
    }//GEN-LAST:event_pwdOldPWCaretUpdate

    private void pwdNewPWCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_pwdNewPWCaretUpdate
        checkFields();
    }//GEN-LAST:event_pwdNewPWCaretUpdate

    private void pwdConfirmCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_pwdConfirmCaretUpdate
        checkFields();
    }//GEN-LAST:event_pwdConfirmCaretUpdate

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPasswordField pwdConfirm;
    private javax.swing.JPasswordField pwdNewPW;
    private javax.swing.JPasswordField pwdOldPW;
    // End of variables declaration//GEN-END:variables
}
