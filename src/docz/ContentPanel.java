/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.CardLayout;
import java.sql.SQLException;
import java.text.DateFormat;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.border.TitledBorder;
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
    private Entity currentEntity;
    private boolean editMode = false;

    /**
     * Creates new form ContentPanel
     */
    public ContentPanel() {
        initComponents();
        getSearchResultPanel().setContentPanel(this);
    }

    private SearchResultsPanel getSearchResultPanel() {
        return (SearchResultsPanel) pnlSearchResults;
    }

    public void showResults(Entity[] resultEntities) throws SQLException {
        getSearchResultPanel().showResults(resultEntities);
        ((CardLayout) getLayout()).show(this, "card2");
    }

    public void showPreview(Entity entity) throws SQLException {
        currentEntity = entity;
        imgPreview.setImg(currentEntity.getThumbnail((int) (imgPreview.getWidth() * 1.5), (int) (imgPreview.getHeight() * 1.5)));
        ((TitledBorder) imgPreview.getBorder()).setTitle(currentEntity.getTitle());
        txtPreviewTitle.setText(currentEntity.getTitle());
        txaPreviewDescription.setText(currentEntity.getDescription());
        txtTags.setText(currentEntity.getTagsAsString());
        lblPreviewDate.setText(DateFormat.getDateInstance().format(currentEntity.getDate()));
        lblPreviewCreated.setText("created: "+DateFormat.getDateInstance().format(currentEntity.created));
        
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
        pnlSearchResults = new SearchResultsPanel();
        pnlDocOverview = new javax.swing.JPanel();
        pnlPreview = imgPreview = new ImagePanel();
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

        jRadioButton1.setText("jRadioButton1");

        setLayout(new java.awt.CardLayout());

        scrSearchResults.setHorizontalScrollBar(null);

        javax.swing.GroupLayout pnlSearchResultsLayout = new javax.swing.GroupLayout(pnlSearchResults);
        pnlSearchResults.setLayout(pnlSearchResultsLayout);
        pnlSearchResultsLayout.setHorizontalGroup(
            pnlSearchResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1078, Short.MAX_VALUE)
        );
        pnlSearchResultsLayout.setVerticalGroup(
            pnlSearchResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 799, Short.MAX_VALUE)
        );

        scrSearchResults.setViewportView(pnlSearchResults);

        add(scrSearchResults, "card2");

        pnlPreview.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TITLE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16))); // NOI18N

        javax.swing.GroupLayout pnlPreviewLayout = new javax.swing.GroupLayout(pnlPreview);
        pnlPreview.setLayout(pnlPreviewLayout);
        pnlPreviewLayout.setHorizontalGroup(
            pnlPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 484, Short.MAX_VALUE)
        );
        pnlPreviewLayout.setVerticalGroup(
            pnlPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txaPreviewDescription.setEditable(false);
        txaPreviewDescription.setBackground(getBackground());
        txaPreviewDescription.setColumns(20);
        txaPreviewDescription.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txaPreviewDescription.setLineWrap(true);
        txaPreviewDescription.setRows(5);
        txaPreviewDescription.setText("Description, description, Description, description, Description, description, Description, description, Description, description, Description, description, Description, description, Description, description, Description, description, ");
        txaPreviewDescription.setWrapStyleWord(true);
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

        btnRemoveRelation.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnRemoveRelation.setText("-");

        txtTags.setEditable(false);
        txtTags.setBackground(getBackground());
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("relation details"));

        lblRelationTitle.setText("RELATION TITLE");

        txaRelationDescription.setEditable(false);
        txaRelationDescription.setBackground(getBackground());
        txaRelationDescription.setColumns(20);
        txaRelationDescription.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txaRelationDescription.setLineWrap(true);
        txaRelationDescription.setRows(5);
        txaRelationDescription.setText("RELATION DESCRIPTION\nasdasd");
        txaRelationDescription.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txaRelationDescription);

        scrRelatedWith.setHorizontalScrollBar(null);

        imlRelatedWith.setBorder(javax.swing.BorderFactory.createTitledBorder("related with"));

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrRelatedWith, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(lblRelationTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
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
                .addComponent(scrRelatedWith, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE))
        );

        txtPreviewTitle.setEditable(false);
        txtPreviewTitle.setBackground(getBackground());
        txtPreviewTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPreviewTitle.setText("TITLE");

        javax.swing.GroupLayout pnlDocOverviewLayout = new javax.swing.GroupLayout(pnlDocOverview);
        pnlDocOverview.setLayout(pnlDocOverviewLayout);
        pnlDocOverviewLayout.setHorizontalGroup(
            pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBack)
                    .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPreviewTitle))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(txtTags)
                    .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                        .addComponent(btnAddRelation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveRelation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 301, Short.MAX_VALUE)
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
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGoBack)
                    .addComponent(btnChangeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPreviewDate))
                .addGap(9, 9, 9)
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPreviewTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDocOverviewLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlDocOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddRelation)
                            .addComponent(btnRemoveRelation)
                            .addComponent(lblPreviewCreated, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEditSave)))
                    .addComponent(pnlPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

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
        
        
        if(editMode){//save action
            btnEditSave.setText("save");
        }
        else{ //save action
            btnEditSave.setText("edit");
        }
        
        
        
    }//GEN-LAST:event_btnEditSaveActionPerformed

    private void btnChangeDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeDateActionPerformed
        JDatePanel datePanel = (JDatePanelImpl)JDateComponentFactory.createJDatePanel();
        int okCxl = JOptionPane.showConfirmDialog(null, datePanel, "Datum auswählen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_btnChangeDateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddRelation;
    private javax.swing.JButton btnChangeDate;
    private javax.swing.JButton btnEditSave;
    private javax.swing.JButton btnGoBack;
    private javax.swing.JButton btnRemoveRelation;
    private docz.ImageList imlRelatedWith;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPreviewCreated;
    private javax.swing.JLabel lblPreviewDate;
    private javax.swing.JLabel lblRelationTitle;
    private javax.swing.JPanel pnlDocOverview;
    private javax.swing.JPanel pnlPreview;
    private javax.swing.JPanel pnlSearchResults;
    private javax.swing.JScrollPane scrRelatedWith;
    private javax.swing.JScrollPane scrSearchResults;
    private javax.swing.JTextArea txaPreviewDescription;
    private javax.swing.JTextArea txaRelationDescription;
    private javax.swing.JTextField txtPreviewTitle;
    private javax.swing.JTextField txtTags;
    // End of variables declaration//GEN-END:variables
}
