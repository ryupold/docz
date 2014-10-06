/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.gui.imagelist.AbstractImageList.Alignment;
import de.realriu.riulib.gui.imagelist.ImageListAdapter;
import de.realriu.riulib.gui.imagelist.ScaledImageList;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;

/**
 *
 * @author Michael
 */
public class ContentPanel extends javax.swing.JPanel {

    private ImagePanel imgPreview;
    private ScaledImageList fileList;
    private Entity currentEntity;
    private boolean editMode = false;

    /**
     * Creates new form ContentPanel
     */
    public ContentPanel() {
        initComponents();
        imlSearchResults.setImageListListener(new ImageList.ImageListListener() {

            @Override
            public void imageHovered(int index) {

            }

            @Override
            public void imageSelected(int index) {

            }

            @Override
            public void doubleClicked(int index) {
                try {
                    showPreview((Entity) imlSearchResults.getThumbnails()[imlSearchResults.getSelectedIndex()], true);
                } catch (SQLException ex) {
                    Log.l(ex);
                }
            }
        });

        imlRelatedWith.setImageListListener(new ImageList.ImageListListener() {

            @Override
            public void imageHovered(int index) {

            }

            @Override
            public void imageSelected(int index) {
                btnRemoveRelation.setEnabled(editMode && index >= 0);
                if (index >= 0) {
                    lblRelationTitle.setText(imlRelatedWith.getThumbnails()[index].getTitle());
                    txaRelationDescription.setText(imlRelatedWith.getThumbnails()[index].getDescription());
                }
            }

            @Override
            public void doubleClicked(int index) {
                if (((Relation) imlRelatedWith.getThumbnails()[index]).getEntity2() != null) {
                    try {
                        showPreview(((Relation) imlRelatedWith.getThumbnails()[index]).getEntity2(), true);
                    } catch (SQLException ex) {
                        Log.l(ex);
                    }
                }
            }
        });

        fileList.addImageListListener(new ImageListAdapter<Image>() {

            @Override
            public void imageSelected(int pos) {
                btnRemoveFile.setEnabled(pos >= 0 && editMode);
                btnExportFile.setEnabled(pos >= 0);
                if (pos >= 0) {
                    try {
                        String title = fileList.getTitle(pos);
                        Font font = new Font("Arial", Font.PLAIN, 30);
                        Image img = DataHandler.instance.getFileAsImage(currentEntity, title);
                        if (img == null) {
                            img = DataHandler.instance.getThumbnail(currentEntity, title,
                                    (int) (imgPreview.getWidth() * 1.5), (int) (imgPreview.getHeight() * 1.5), font);
                        }
                        imgPreview.setImg(img);
                    } catch (SQLException ex) {
                        Log.l(ex);
                    }
                }
            }
        });
    }

    public void showResults(Entity[] resultEntities) throws SQLException, Exception {
        //imlSearchResults.setThumbnails(resultEntities);
        imlSearchResults.setThumbnails(resultEntities);
        
        ((CardLayout) getLayout()).show(this, "card2");
    }

    public void showPreview(Entity entity, boolean stopEditMode) throws SQLException {
        if (stopEditMode) {
            editMode = false;
        }

        if (editMode) {//save action
            btnEditSave.setText("save");
        } else { //save action
            btnEditSave.setText("edit");
        }

        lblRelationTitle.setText("");
        txaRelationDescription.setText("");
               

        txtPreviewTitle.setEditable(editMode);
        txaPreviewDescription.setEditable(editMode);
        txtTags.setEditable(editMode);
        btnChangeDate.setEnabled(editMode);
        btnAddFile.setEnabled(editMode);
        btnRemoveFile.setEnabled(editMode && fileList.getSelectedImageIndex() >= 0);
        btnAddRelation.setEnabled(editMode);
        btnRemoveRelation.setEnabled(editMode && imlRelatedWith.getSelectedIndex() >= 0);
        btnDeleteEntity.setEnabled(editMode);

        //set current entity
        currentEntity = entity;

//        ((TitledBorder) imgPreview.getBorder()).setTitle(currentEntity.getTitle());
        txtPreviewTitle.setText(currentEntity.getTitle());
        txaPreviewDescription.setText(currentEntity.getDescription());
        txtTags.setText(currentEntity.getTagsAsString());
        lblPreviewDate.setText(DateFormat.getDateInstance().format(currentEntity.getDate()));
        lblPreviewCreated.setText("created: " + DateFormat.getDateInstance().format(currentEntity.created));

        //load files attached to this entity
        try {
            fileList.clear();
            ImageFile[] files = DataHandler.instance.getThumbnails(currentEntity, 150, 150, new Font("Arial", Font.PLAIN, 20));
            for (ImageFile iF : files) {
                fileList.addImage(iF.image, iF.name);
            }
            fileList.setTitleColor(new Color(255 - fileList.getBackground().getRed(),
                    255 - fileList.getBackground().getGreen(), 255 - fileList.getBackground().getBlue()));
            
            
            if(fileList.count() > 0){
                fileList.selectImage(0);
            }
            else{
                imgPreview.setImg(Resources.getImg_nofiles());
            }
        } catch (IOException ex) {
            Log.l(ex);
        }

        try {
            //load related entities
            imlRelatedWith.setThumbnails(DataHandler.instance.getRelations(currentEntity));
        } catch (Exception ex) {
            Log.l(ex);
        }

        ((CardLayout) getLayout()).show(this, "card4");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method
     * is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        scrSearchResults = new javax.swing.JScrollPane();
        imlSearchResults = new docz.ImageList();
        pnlDocOverview = new javax.swing.JPanel();
        pnlPreview = imgPreview = new ImagePanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaPreviewDescription = new javax.swing.JTextArea();
        btnGoBack = new javax.swing.JButton();
        btnAddRelation = new javax.swing.JButton();
        btnRemoveRelation = new javax.swing.JButton();
        txtTags = new javax.swing.JTextField();
        lblPreviewDate = new javax.swing.JLabel();
        btnChangeDate = new javax.swing.JButton();
        lblPreviewCreated = new javax.swing.JLabel();
        btnEditSave = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblRelationTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaRelationDescription = new javax.swing.JTextArea();
        scrRelatedWith = new javax.swing.JScrollPane();
        imlRelatedWith = new docz.ImageList();
        txtPreviewTitle = new javax.swing.JTextField();
        btnAddFile = new javax.swing.JButton();
        btnRemoveFile = new javax.swing.JButton();
        btnExportFile = new javax.swing.JButton();
        imgListPreviewFiles = fileList = new ScaledImageList(Alignment.Horizontal, true, 150, 150);
        btnDeleteEntity = new javax.swing.JButton();

        jRadioButton1.setText("jRadioButton1");

        setLayout(new java.awt.CardLayout());

        scrSearchResults.setHorizontalScrollBar(null);

        javax.swing.GroupLayout imlSearchResultsLayout = new javax.swing.GroupLayout(imlSearchResults);
        imlSearchResults.setLayout(imlSearchResultsLayout);
        imlSearchResultsLayout.setHorizontalGroup(
            imlSearchResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1078, Short.MAX_VALUE)
        );
        imlSearchResultsLayout.setVerticalGroup(
            imlSearchResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 846, Short.MAX_VALUE)
        );

        scrSearchResults.setViewportView(imlSearchResults);

        add(scrSearchResults, "card2");

        pnlPreview.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Preview", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        pnlPreview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlPreviewMouseClicked(evt);
            }
        });

        jButton1.setText("⬜");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPreviewLayout = new javax.swing.GroupLayout(pnlPreview);
        pnlPreview.setLayout(pnlPreviewLayout);
        pnlPreviewLayout.setHorizontalGroup(
            pnlPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPreviewLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1))
        );
        pnlPreviewLayout.setVerticalGroup(
            pnlPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPreviewLayout.createSequentialGroup()
                .addGap(0, 394, Short.MAX_VALUE)
                .addComponent(jButton1))
        );

        txaPreviewDescription.setEditable(false);
        txaPreviewDescription.setBackground(getBackground());
        txaPreviewDescription.setColumns(20);
        txaPreviewDescription.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txaPreviewDescription.setLineWrap(true);
        txaPreviewDescription.setRows(5);
        txaPreviewDescription.setText("Description, description, Description, description, Description, description, Description, description, Description, description, Description, description, Description, description, Description, description, Description, description, ");
        txaPreviewDescription.setWrapStyleWord(true);
        txaPreviewDescription.setAutoscrolls(false);
        jScrollPane2.setViewportView(txaPreviewDescription);

        btnGoBack.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGoBack.setText("<--");
        btnGoBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackActionPerformed(evt);
            }
        });

        btnAddRelation.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnAddRelation.setText("+");
        btnAddRelation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRelationActionPerformed(evt);
            }
        });

        btnRemoveRelation.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnRemoveRelation.setText("-");
        btnRemoveRelation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveRelationActionPerformed(evt);
            }
        });

        txtTags.setEditable(false);
        txtTags.setBackground(getBackground());
        txtTags.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtTags.setText("TAG1, tag2, Tag3,...");

        lblPreviewDate.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblPreviewDate.setText("21.04.1988");

        btnChangeDate.setText("...");
        btnChangeDate.setEnabled(false);
        btnChangeDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeDateActionPerformed(evt);
            }
        });

        lblPreviewCreated.setText("created: 21.09.2014");

        btnEditSave.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnEditSave.setText("edit");
        btnEditSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSaveActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "relations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Aharoni", 1, 18))); // NOI18N

        lblRelationTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblRelationTitle.setText(" ");

        txaRelationDescription.setEditable(false);
        txaRelationDescription.setBackground(getBackground());
        txaRelationDescription.setColumns(20);
        txaRelationDescription.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txaRelationDescription.setLineWrap(true);
        txaRelationDescription.setRows(5);
        txaRelationDescription.setText(" ");
        txaRelationDescription.setWrapStyleWord(true);
        txaRelationDescription.setAutoscrolls(false);
        jScrollPane1.setViewportView(txaRelationDescription);

        scrRelatedWith.setHorizontalScrollBar(null);

        imlRelatedWith.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "related with", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        javax.swing.GroupLayout imlRelatedWithLayout = new javax.swing.GroupLayout(imlRelatedWith);
        imlRelatedWith.setLayout(imlRelatedWithLayout);
        imlRelatedWithLayout.setHorizontalGroup(
            imlRelatedWithLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );
        imlRelatedWithLayout.setVerticalGroup(
            imlRelatedWithLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        scrRelatedWith.setViewportView(imlRelatedWith);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrRelatedWith, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                    .addComponent(lblRelationTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRelationTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrRelatedWith, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        txtPreviewTitle.setEditable(false);
        txtPreviewTitle.setBackground(getBackground());
        txtPreviewTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPreviewTitle.setText("TITLE");

        btnAddFile.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnAddFile.setText("+");
        btnAddFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFileActionPerformed(evt);
            }
        });

        btnRemoveFile.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnRemoveFile.setText("-");
        btnRemoveFile.setEnabled(false);
        btnRemoveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveFileActionPerformed(evt);
            }
        });

        btnExportFile.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnExportFile.setText("export");
        btnExportFile.setEnabled(false);
        btnExportFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout imgListPreviewFilesLayout = new javax.swing.GroupLayout(imgListPreviewFiles);
        imgListPreviewFiles.setLayout(imgListPreviewFilesLayout);
        imgListPreviewFilesLayout.setHorizontalGroup(
            imgListPreviewFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imgListPreviewFilesLayout.setVerticalGroup(
            imgListPreviewFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        btnDeleteEntity.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        btnDeleteEntity.setText("delete");
        btnDeleteEntity.setEnabled(false);
        btnDeleteEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEntityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDocOverviewLayout = new javax.swing.GroupLayout(pnlDocOverview);
        pnlDocOverview.setLayout(pnlDocOverviewLayout);
        pnlDocOverviewLayout.setHorizontalGroup(
            pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBack)
                    .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPreviewTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                            .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                                .addComponent(btnAddFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnRemoveFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnExportFile)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(imgListPreviewFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(txtTags)
                    .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                        .addComponent(btnAddRelation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRemoveRelation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteEntity)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditSave)
                        .addGap(18, 18, 18)
                        .addComponent(lblPreviewCreated))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDocOverviewLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnChangeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPreviewDate))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlDocOverviewLayout.setVerticalGroup(
            pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnChangeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnGoBack)
                        .addComponent(lblPreviewDate)))
                .addGap(9, 9, 9)
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPreviewTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddRelation)
                            .addComponent(btnRemoveRelation)
                            .addComponent(lblPreviewCreated, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEditSave)
                            .addComponent(btnDeleteEntity)))
                    .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                        .addComponent(pnlPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imgListPreviewFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddFile)
                            .addComponent(btnRemoveFile)
                            .addComponent(btnExportFile))))
                .addContainerGap())
        );

        fileList.setPadding(0, 0, 0, 12);

        add(pnlDocOverview, "card4");
    }// </editor-fold>//GEN-END:initComponents

    private void btnGoBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackActionPerformed
        ((CardLayout) getLayout()).show(this, "card2");
    }//GEN-LAST:event_btnGoBackActionPerformed

    private void btnEditSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSaveActionPerformed
        editMode = !editMode;

        txtPreviewTitle.setEditable(editMode);
        txaPreviewDescription.setEditable(editMode);
        txtTags.setEditable(editMode);
        btnChangeDate.setEnabled(editMode);
        btnAddFile.setEnabled(editMode);
        btnRemoveFile.setEnabled(editMode);
        btnAddRelation.setEnabled(editMode);
        btnRemoveRelation.setEnabled(editMode && imlRelatedWith.getSelectedIndex() >= 0);
        btnDeleteEntity.setEnabled(editMode);
        btnDeleteEntity.setForeground(editMode ? new Color(204, 0, 0) : Color.black);

        if (editMode) {//save action
            btnEditSave.setText("save");
        } else { //save action
            btnEditSave.setText("edit");

            if (txtPreviewTitle.getText().trim().length() > 0) {
                try {
                    List<String> tags = new ArrayList<>();
                    String[] tagArray = txtTags.getText().split(",");
                    for (String t : tagArray) {
                        tags.add(t.trim().toLowerCase());
                    }

                    currentEntity.setTitle(txtPreviewTitle.getText().trim());
                    currentEntity.setDescription(txaPreviewDescription.getText());
                    currentEntity.setTags(tags);
                    currentEntity.setDate(DateFormat.getDateInstance().parse(lblPreviewDate.getText()));
                } catch (ParseException ex) {
                    Log.l(ex);
                }
            }

            DataHandler.instance.updateEntity(currentEntity);
        }
    }//GEN-LAST:event_btnEditSaveActionPerformed

    private void btnChangeDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeDateActionPerformed
        JDatePanel datePanel = (JDatePanelImpl) JDateComponentFactory.createJDatePanel();
        int okCxl = JOptionPane.showConfirmDialog(null, datePanel, "Datum auswählen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            DateModel<?> dm = ((JDatePanel) datePanel).getModel();
            lblPreviewDate.setText(dm.getDay() + "." + (1 + dm.getMonth()) + "." + dm.getYear());
        }
    }//GEN-LAST:event_btnChangeDateActionPerformed

    private void btnRemoveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveFileActionPerformed
        if (fileList.getSelectedImageIndex() >= 0) {
            try {
                if (DataHandler.instance.removeFile(currentEntity, fileList.getImageTitle(fileList.getSelectedImageIndex()))) {
                    fileList.removeImage(fileList.getSelectedImageIndex());
                }

                btnRemoveFile.setEnabled(fileList.getSelectedImageIndex() >= 0);
                btnExportFile.setEnabled(fileList.getSelectedImageIndex() >= 0);
            } catch (SQLException ex) {
                Log.l(ex);
            }
        }
    }//GEN-LAST:event_btnRemoveFileActionPerformed

    private void btnAddFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFileActionPerformed
        String lastPath = DB.getSetting("lastpath", "");
        final JFileChooser fc = new JFileChooser(new File(lastPath));

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            WaitDialog wait = new WaitDialog(null, new WaitDialog.AsyncProcess("Adding files...") {

                @Override
                public void start() throws Exception {
                    int progress = 1;
                    for (File f : fc.getSelectedFiles()) {
                        try {
                            String filename = f.getAbsolutePath().toLowerCase();
                            processing(((float) progress / (float) fc.getSelectedFiles().length), filename);
                            BufferedImage img;
                            if ((filename.endsWith(".jpg")
                                    || filename.endsWith(".jpeg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".bmp")
                                    || filename.endsWith(".wbmp")
                                    || filename.endsWith(".gif"))) {
                                try {
                                    img = ImageIO.read(f);
                                } catch (IOException ex) {
                                    img = Resources.createImageWithText(f.getName(), fileList.getHeight(), fileList.getHeight());
                                }

                            } else {
                                img = Resources.createImageWithText(f.getName(), fileList.getHeight(), fileList.getHeight());
                            }

                            //save file in DB                            
                            //add image to file list
                            fileList.addImage(img, DataHandler.instance.addFiles(currentEntity, f)[0]);

                            progress++;
                        } catch (Exception ex) {
                            Log.l(ex);
                        }
                    }
                }

                @Override
                public void finished(boolean success) {

                }

                @Override
                public void cancel() {

                }
            }, false, "Adding files to existing Entity.");

            if (fc.getSelectedFiles().length > 0) {
                lastPath = fc.getSelectedFiles()[0].getParentFile().getPath();
            }
            DB.setSetting("lastpath", lastPath);
        }


    }//GEN-LAST:event_btnAddFileActionPerformed

    private void btnExportFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportFileActionPerformed
        if (fileList.getSelectedImageIndex() >= 0) {
            String lastPath = DB.getSetting("lastpath", "");

            final JFileChooser fc = new JFileChooser(new File(lastPath));

            String fileExtension = "";
            if (fileList.getImageTitle(fileList.getSelectedImageIndex()).lastIndexOf(".") >= 0) {
                fileExtension = fileList.getImageTitle(fileList.getSelectedImageIndex()).substring(fileList.getImageTitle(fileList.getSelectedImageIndex()).lastIndexOf("."));
            }

            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setMultiSelectionEnabled(false);
            fc.setFileFilter(new FileNameExtensionFilter(fileExtension + " files", fileExtension));
            fc.setSelectedFile(new File(lastPath + File.separator + fileList.getImageTitle(fileList.getSelectedImageIndex())));
            final boolean[] abort = new boolean[]{false};
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

                WaitDialog wait = new WaitDialog(null, new WaitDialog.AsyncProcess("exporting file to " + fc.getSelectedFile().getAbsolutePath()) {

                    @Override
                    public void start() throws Exception {
                        try {
                            long bytesRead = 0;
                            long fileSize = 0;
                            DB.DBResult r = DB.select("SELECT name, created, file, size FROM files WHERE id='" + currentEntity.id + "' AND name='" + fileList.getImageTitle(fileList.getSelectedImageIndex()) + "'");
                            InputStream byteStream = null;
                            FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());

                            if (r.resultSet.next()) {
                                byteStream = r.resultSet.getBinaryStream(3);
                                fileSize = r.resultSet.getLong(4);
                            }

                            byte[] buffer = new byte[1024];
                            int tmpCount = 0;
                            while (!abort[0] && (tmpCount = byteStream.read(buffer)) > 0) {
                                bytesRead += tmpCount;
                                fos.write(buffer);
                                double percent = (double) bytesRead / (double) fileSize;
                                processing((double) bytesRead / (double) fileSize, (((int) (percent * 1000.0)) / 10.0) + "%");
                            }
                            byteStream.close();
                            fos.close();
                            r.close();
                            if (abort[0]) {
                                fc.getSelectedFile().delete();
                            }
                        } catch (IOException ex) {
                            Log.l(ex);
                        }
                    }

                    @Override
                    public void finished(boolean success) {

                    }

                    @Override
                    public void cancel() {
                        abort[0] = true;
                    }
                }, true, "Exporting file");
            }

            DB.setSetting("lastpath", lastPath);
        }
    }//GEN-LAST:event_btnExportFileActionPerformed

    private void btnAddRelationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRelationActionPerformed
        AddRelationDialog addRelationDialog = new AddRelationDialog(null, currentEntity);
        addRelationDialog.setVisible(true);
        addRelationDialog.dispose();
        System.gc();
        try {
            showPreview(currentEntity, false);
        } catch (SQLException ex) {
            Log.l(ex);
        }
    }//GEN-LAST:event_btnAddRelationActionPerformed

    private void btnDeleteEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEntityActionPerformed
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "This will delete this entity and all its relations, files and tags", "Delete entity " + currentEntity.getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
            Log.l(DataHandler.instance.deleteEntity(currentEntity) ? "Entity "+currentEntity.title+" deleted" : "couldn't delete Entity "+currentEntity.title);
            currentEntity = null;
            ((CardLayout) getLayout()).show(this, "card2");
        }
    }//GEN-LAST:event_btnDeleteEntityActionPerformed

    private void btnRemoveRelationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveRelationActionPerformed
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "Do you really want to delete the relation: " + imlRelatedWith.getThumbnails()[imlRelatedWith.getSelectedIndex()].getTitle(), "Delete Relation ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
            DataHandler.instance.deleteRelation((Relation) imlRelatedWith.getThumbnails()[imlRelatedWith.getSelectedIndex()]);
            
        }
    }//GEN-LAST:event_btnRemoveRelationActionPerformed

    private void pnlPreviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlPreviewMouseClicked
        
    }//GEN-LAST:event_pnlPreviewMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (fileList.getSelectedImageIndex() >= 0) {
            try {
                FullScreenPreview full = new FullScreenPreview(null, currentEntity, fileList.getImageTitle(fileList.getSelectedImageIndex()));
                full.setVisible(true);

                full.dispose();
                System.gc();
            } catch (Exception ex) {
                Log.l(ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFile;
    private javax.swing.JButton btnAddRelation;
    private javax.swing.JButton btnChangeDate;
    private javax.swing.JButton btnDeleteEntity;
    private javax.swing.JButton btnEditSave;
    private javax.swing.JButton btnExportFile;
    private javax.swing.JButton btnGoBack;
    private javax.swing.JButton btnRemoveFile;
    private javax.swing.JButton btnRemoveRelation;
    private javax.swing.JPanel imgListPreviewFiles;
    private docz.ImageList imlRelatedWith;
    private docz.ImageList imlSearchResults;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPreviewCreated;
    private javax.swing.JLabel lblPreviewDate;
    private javax.swing.JLabel lblRelationTitle;
    private javax.swing.JPanel pnlDocOverview;
    private javax.swing.JPanel pnlPreview;
    private javax.swing.JScrollPane scrRelatedWith;
    private javax.swing.JScrollPane scrSearchResults;
    private javax.swing.JTextArea txaPreviewDescription;
    private javax.swing.JTextArea txaRelationDescription;
    private javax.swing.JTextField txtPreviewTitle;
    private javax.swing.JTextField txtTags;
    // End of variables declaration//GEN-END:variables
}
