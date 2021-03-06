/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Font;
import java.sql.SQLException;

/**
 *
 * @author Michael
 */
public class AddRelationDialog extends javax.swing.JDialog {

    private final Entity entity1;
    private Entity entity2;
    private ImagePanel imgRelation;
    private WaitDialog.AsyncProcess searchProgress = null;

    /**
     * Creates new form AddRelation
     */
    public AddRelationDialog(java.awt.Frame parent, final Entity entity1) {
        super(parent, true);
        this.entity1 = entity1;
        initComponents();
        imlEntities.setImageListListener(new ImageList.ImageListListener() {

            @Override
            public void imageHovered(int index) {

            }

            @Override
            public void imageSelected(int index) {
                if (index >= 0) {
                    entity2 = (Entity) imlEntities.getThumbnails()[imlEntities.getSelectedIndex()];

                    lblTitle.setText(entity2.getTitle());
                    txaDescription.setText(entity2.getDescription());
                    lblDate.setText("date: " + entity2.getDate() + "");
                    lblCreated.setText("created: " + entity2.getCreated());
                    
                    try {
                        imgRelation.setImg(entity2.getThumbnail(imgRelation.getWidth(), imgRelation.getHeight(), new Font("Arial", Font.BOLD, 20)));
                    } catch (Exception ex) {
                        Log.l(ex);
                    }
                }
            }

            @Override
            public void doubleClicked(int index) {

            }
        });
        
        txtSearchKeyTyped(null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method
     * is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAdd = new javax.swing.JButton();
        btnAbort = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        imlEntities = new docz.ImageList();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaDescription = new javax.swing.JTextArea();
        lblTags = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblCreated = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtRelationTitle = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaRelationDescription = new javax.swing.JTextArea();
        pnlRelation = imgRelation = new ImagePanel();
        txtSearch = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnAdd.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnAbort.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnAbort.setText("Abort");
        btnAbort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbortActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout imlEntitiesLayout = new javax.swing.GroupLayout(imlEntities);
        imlEntities.setLayout(imlEntitiesLayout);
        imlEntitiesLayout.setHorizontalGroup(
            imlEntitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 558, Short.MAX_VALUE)
        );
        imlEntitiesLayout.setVerticalGroup(
            imlEntitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 579, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(imlEntities);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Entity"));

        lblTitle.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblTitle.setText(" ");

        txaDescription.setEditable(false);
        txaDescription.setBackground(getBackground());
        txaDescription.setColumns(20);
        txaDescription.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txaDescription.setLineWrap(true);
        txaDescription.setRows(5);
        txaDescription.setWrapStyleWord(true);
        txaDescription.setAutoscrolls(false);
        jScrollPane2.setViewportView(txaDescription);

        lblTags.setText(" ");

        lblDate.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDate.setText(" ");

        lblCreated.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        lblCreated.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCreated, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(lblTags, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTags)
                .addGap(18, 18, 18)
                .addComponent(lblDate)
                .addGap(18, 18, 18)
                .addComponent(lblCreated)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Relation"));

        txtRelationTitle.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtRelationTitle.setToolTipText("Title");
        txtRelationTitle.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtRelationTitleCaretUpdate(evt);
            }
        });

        txaRelationDescription.setColumns(20);
        txaRelationDescription.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txaRelationDescription.setRows(5);
        txaRelationDescription.setAutoscrolls(false);
        jScrollPane3.setViewportView(txaRelationDescription);

        javax.swing.GroupLayout pnlRelationLayout = new javax.swing.GroupLayout(pnlRelation);
        pnlRelation.setLayout(pnlRelationLayout);
        pnlRelationLayout.setHorizontalGroup(
            pnlRelationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlRelationLayout.setVerticalGroup(
            pnlRelationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 122, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRelationTitle)
                    .addComponent(jScrollPane3)
                    .addComponent(pnlRelation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtRelationTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlRelation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtSearch.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtSearch.setToolTipText("Search Field");
        txtSearch.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtSearchCaretUpdate(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(txtSearch))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAbort)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5, 5, 5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnAbort))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAbortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbortActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btnAbortActionPerformed

    private void txtSearchCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtSearchCaretUpdate

    }//GEN-LAST:event_txtSearchCaretUpdate

    private void txtRelationTitleCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtRelationTitleCaretUpdate
        btnAdd.setEnabled(txtRelationTitle.getText().trim().length() > 0);
    }//GEN-LAST:event_txtRelationTitleCaretUpdate

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        try {
            DataHandler.instance.createRelation(txtRelationTitle.getText().trim(), txaRelationDescription.getText().trim(), entity1, entity2);
            setVisible(false);
        } catch (SQLException ex) {
            Log.l(ex);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped

        if (searchProgress != null && !searchProgress.isFinished()) {
            searchProgress.cancel(); //concurrency problems????
        }

        searchProgress = new WaitDialog.AsyncProcess("search relations") {
            Entity[] findings = null;

            @Override
            public void start() throws Exception {
                findings = DataHandler.instance.search(txtSearch.getText().split(" "), true, true, false, true);
            }

            @Override
            public void finished(boolean success) {
                try {
                    if (!searchProgress.isCanceled() && findings != null) {
                        imlEntities.setThumbnails(findings);
                    }
                } catch (Exception ex) {
                    Log.l(ex);
                }
            }
        };
        new WaitDialog(null, searchProgress, true, false, 1000, "Searching for Relations");

    }//GEN-LAST:event_txtSearchKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbort;
    private javax.swing.JButton btnAdd;
    private docz.ImageList imlEntities;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCreated;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblTags;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlRelation;
    private javax.swing.JTextArea txaDescription;
    private javax.swing.JTextArea txaRelationDescription;
    private javax.swing.JTextField txtRelationTitle;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
