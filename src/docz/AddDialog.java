/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.gui.imagelist.AbstractImageList.Alignment;
import de.realriu.riulib.gui.imagelist.FileImageList;
import de.realriu.riulib.gui.imagelist.ImageListAdapter;
import de.realriu.riulib.gui.imagelist.ScaledImageList;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;

/**
 *
 * @author Michael
 */
public class AddDialog extends javax.swing.JDialog {

    private ScaledImageList imgList;
    private List<File> files = new ArrayList<>();
    private ImagePanel imgPanel, previewPanel;

    /**
     * Creates new form AddDialog
     */
    public AddDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        

        ((JDatePanel) datePicker).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DateModel<?> dm = ((JDatePanel) datePicker).getModel();
                lblDocDate.setText(dm.getDay() + "." + (1 + dm.getMonth()) + "." + dm.getYear());
            }
        });

        lblDocDate.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "." + Calendar.getInstance().get(Calendar.YEAR));
        
        imgList.addImageListListener(new ImageListAdapter<Image>() {

            @Override
            public void imageSelected(int pos) {
                if(pos>=0 && pos < files.size()){
                    try {
                        previewPanel.setImg(files.get(pos));
                    } catch (IOException ex) {
                        Log.l(ex);
                    }
                }
            }
            
});
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addButtonGroup = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDocDescription = new javax.swing.JTextField();
        txtDocTags = new javax.swing.JTextField();
        txtDocTitle = new javax.swing.JTextField();
        btnDocRemoveFile = new javax.swing.JButton();
        btnDocAddFile = new javax.swing.JButton();
        imgListDocFiles = imgList = new ScaledImageList(Alignment.Horizontal, true, 200, 200);
        btnDocSave = new javax.swing.JButton();
        datePicker = (JDatePanelImpl)net.sourceforge.jdatepicker.JDateComponentFactory.createJDatePanel();
        lblDocDate = new javax.swing.JLabel();
        imgPanelPreview = previewPanel = new ImagePanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtInstitutionTags = new javax.swing.JTextField();
        txtInstitutionTitle = new javax.swing.JTextField();
        txtInstitutionDescription = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        imgLogo = imgPanel = new ImagePanel();
        btnInstitutionLogo = new javax.swing.JButton();
        btnInstitutionSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPane1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Title:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Description:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Date:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Tags:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Files:");

        txtDocDescription.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        txtDocTags.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        txtDocTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtDocTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocTitleActionPerformed(evt);
            }
        });

        btnDocRemoveFile.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnDocRemoveFile.setText("-");
        btnDocRemoveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocRemoveFileActionPerformed(evt);
            }
        });

        btnDocAddFile.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnDocAddFile.setText("+");
        btnDocAddFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocAddFileActionPerformed(evt);
            }
        });

        imgListDocFiles.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout imgListDocFilesLayout = new javax.swing.GroupLayout(imgListDocFiles);
        imgListDocFiles.setLayout(imgListDocFilesLayout);
        imgListDocFilesLayout.setHorizontalGroup(
            imgListDocFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imgListDocFilesLayout.setVerticalGroup(
            imgListDocFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 175, Short.MAX_VALUE)
        );

        btnDocSave.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnDocSave.setText("save");
        btnDocSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocSaveActionPerformed(evt);
            }
        });

        datePicker.setBackground(getBackground());

        javax.swing.GroupLayout datePickerLayout = new javax.swing.GroupLayout(datePicker);
        datePicker.setLayout(datePickerLayout);
        datePickerLayout.setHorizontalGroup(
            datePickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 244, Short.MAX_VALUE)
        );
        datePickerLayout.setVerticalGroup(
            datePickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        lblDocDate.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblDocDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDocDate.setText("DATE");

        javax.swing.GroupLayout imgPanelPreviewLayout = new javax.swing.GroupLayout(imgPanelPreview);
        imgPanelPreview.setLayout(imgPanelPreviewLayout);
        imgPanelPreviewLayout.setHorizontalGroup(
            imgPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imgPanelPreviewLayout.setVerticalGroup(
            imgPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblDocDate, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(imgPanelPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtDocTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                            .addComponent(txtDocDescription)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(imgListDocFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(70, 70, 70)
                                .addComponent(txtDocTags, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDocAddFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDocRemoveFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDocSave)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDocTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDocDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(lblDocDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3)
                            .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtDocTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imgListDocFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDocAddFile)
                            .addComponent(btnDocRemoveFile))
                        .addGap(34, 34, 34))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(imgPanelPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDocSave)
                        .addContainerGap())))
        );

        imgList.setPadding(0, 0, 5, 10);

        jTabbedPane1.addTab("+ Doc", jPanel1);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Title:");

        txtInstitutionTags.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        txtInstitutionTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtInstitutionTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInstitutionTitleActionPerformed(evt);
            }
        });

        txtInstitutionDescription.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Description:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Tags:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setText("Logo:");

        imgLogo.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout imgLogoLayout = new javax.swing.GroupLayout(imgLogo);
        imgLogo.setLayout(imgLogoLayout);
        imgLogoLayout.setHorizontalGroup(
            imgLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        imgLogoLayout.setVerticalGroup(
            imgLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        btnInstitutionLogo.setText("...");
        btnInstitutionLogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstitutionLogoActionPerformed(evt);
            }
        });

        btnInstitutionSave.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnInstitutionSave.setText("save");
        btnInstitutionSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstitutionSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtInstitutionDescription)
                            .addComponent(txtInstitutionTitle)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(70, 70, 70)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(imgLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnInstitutionLogo)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtInstitutionTags, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnInstitutionSave)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtInstitutionTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtInstitutionDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txtInstitutionTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(imgLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnInstitutionLogo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 381, Short.MAX_VALUE)
                .addComponent(btnInstitutionSave)
                .addContainerGap())
        );

        jTabbedPane1.addTab("+ Institution", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtInstitutionTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInstitutionTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInstitutionTitleActionPerformed

    private void btnDocSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocSaveActionPerformed
        if (txtDocTitle.getText().trim().length() > 0 && imgList.count() > 0) {
            try {
                List<String> tags = new ArrayList<>();
                String[] tagArray = txtDocTags.getText().split(",");
                for (String t : tagArray) {
                    tags.add(t.trim().toLowerCase());
                }
                Doc.createDoc(txtDocTitle.getText(), txtDocDescription.getText(), tags, DateFormat.getDateInstance().parse(lblDocDate.getText()), files);
                setVisible(false);
            } catch (ParseException | SQLException | IOException ex) {
                Log.l(ex);
            }
        }
    }//GEN-LAST:event_btnDocSaveActionPerformed

    private void txtDocTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocTitleActionPerformed

    private void btnDocAddFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocAddFileActionPerformed
        String lastPath = DB.getSetting("lastpath", "");
        
        JFileChooser fc = new JFileChooser(new File(lastPath));

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File f : fc.getSelectedFiles()) {
                String filename = f.getAbsolutePath().toLowerCase();
                if ((filename.endsWith(".jpg")
                        || filename.endsWith(".jpeg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".bmp")
                        || filename.endsWith(".wbmp")
                        || filename.endsWith(".gif"))) {
                    try {
                        imgList.addImage(ImageIO.read(f), f.getName());
                    } catch (IOException ex) {
                        Log.l(ex);
                    }
                } else if (filename.endsWith(".pdf")) {
                    Image pdf = Resources.getImg_pdf();
                    imgList.addImage(pdf, f.getName());
                } else {
                    imgList.addImage(Resources.getImg_otherfile(), f.getName());
                }

                files.add(f);
                lastPath = f.getParentFile().getPath();
            }
            
            DB.setSetting("lastpath", lastPath);
        }
    }//GEN-LAST:event_btnDocAddFileActionPerformed

    private void btnInstitutionLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstitutionLogoActionPerformed
        String lastPath = DB.getSetting("lastpath", "");
        JFileChooser fc = new JFileChooser(new File(lastPath));
        fc.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "bmp", "wbmp", "png", "gif"));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                imgPanel.setImg(fc.getSelectedFile());
            } catch (IOException ex) {
                Log.l(ex);
            }
            DB.setSetting("lastpath", fc.getSelectedFile().getParentFile().getPath());
        }
    }//GEN-LAST:event_btnInstitutionLogoActionPerformed

    private void btnInstitutionSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstitutionSaveActionPerformed
        if (txtInstitutionTitle.getText().trim().length() > 0) {

            try {
                List<String> tags = new ArrayList<>();
                String[] tagArray = txtInstitutionTags.getText().split(",");
                for (String t : tagArray) {
                    tags.add(t.trim().toLowerCase());
                }
                
                List<File> logo = new ArrayList<>();
                logo.add(imgPanel.getImg());
                Institution.createInstitution(txtInstitutionTitle.getText(), txtInstitutionDescription.getText(), tags, logo);
                setVisible(false);
            } catch (IOException | SQLException ex) {
                Log.l(ex);
            } 
        }
    }//GEN-LAST:event_btnInstitutionSaveActionPerformed

    private void btnDocRemoveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocRemoveFileActionPerformed
        if (imgList.getSelectedImageIndex() >= 0) {
            files.remove(imgList.getSelectedImageIndex());
            imgList.removeImage(imgList.getSelectedImageIndex());            
        }
    }//GEN-LAST:event_btnDocRemoveFileActionPerformed

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
            java.util.logging.Logger.getLogger(AddDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddDialog dialog = new AddDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup addButtonGroup;
    private javax.swing.JButton btnDocAddFile;
    private javax.swing.JButton btnDocRemoveFile;
    private javax.swing.JButton btnDocSave;
    private javax.swing.JButton btnInstitutionLogo;
    private javax.swing.JButton btnInstitutionSave;
    private javax.swing.JPanel datePicker;
    private javax.swing.JPanel imgListDocFiles;
    private javax.swing.JPanel imgLogo;
    private javax.swing.JPanel imgPanelPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblDocDate;
    private javax.swing.JTextField txtDocDescription;
    private javax.swing.JTextField txtDocTags;
    private javax.swing.JTextField txtDocTitle;
    private javax.swing.JTextField txtInstitutionDescription;
    private javax.swing.JTextField txtInstitutionTags;
    private javax.swing.JTextField txtInstitutionTitle;
    // End of variables declaration//GEN-END:variables
}
