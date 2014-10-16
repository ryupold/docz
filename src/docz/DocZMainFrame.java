/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
                }

                //initial search
                doSearch(100);
            }

        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSearch = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnChangePW = new javax.swing.JButton();
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
        ckbFiles = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        cbxSorting = new javax.swing.JComboBox();
        ckbDescending = new javax.swing.JCheckBox();
        contentPanel = new docz.ContentPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtSearch.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16))); // NOI18N

        ckbDocs.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ckbDocs.setSelected(true);
        ckbDocs.setText("Docs");
        ckbDocs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbDocsActionPerformed(evt);
            }
        });

        ckbRelations.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ckbRelations.setSelected(true);
        ckbRelations.setText("Relations");
        ckbRelations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbRelationsActionPerformed(evt);
            }
        });

        ckbInstitutions.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ckbInstitutions.setSelected(true);
        ckbInstitutions.setText("Institutions");
        ckbInstitutions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbInstitutionsActionPerformed(evt);
            }
        });

        ckbTags.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ckbTags.setSelected(true);
        ckbTags.setText("Tags");
        ckbTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbTagsActionPerformed(evt);
            }
        });

        spMaxResult.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        spMaxResult.setModel(new javax.swing.SpinnerNumberModel(50, 1, 10000, 1));
        spMaxResult.setToolTipText("");
        spMaxResult.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spMaxResultStateChanged(evt);
            }
        });
        spMaxResult.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                spMaxResultCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                spMaxResultInputMethodTextChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("max. results:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel2.setText("filter date:");

        ckbMinDate.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ckbMinDate.setText("min:");
        ckbMinDate.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ckbMinDateStateChanged(evt);
            }
        });
        ckbMinDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbMinDateActionPerformed(evt);
            }
        });

        ckbMaxDate.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ckbMaxDate.setText("max:");
        ckbMaxDate.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ckbMaxDateStateChanged(evt);
            }
        });
        ckbMaxDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbMaxDateActionPerformed(evt);
            }
        });

        btnMinDate.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnMinDate.setText("21.04.1988");
        btnMinDate.setEnabled(false);
        btnMinDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinDateActionPerformed(evt);
            }
        });

        btnMaxDate.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnMaxDate.setText("01.10.2014");
        btnMaxDate.setEnabled(false);
        btnMaxDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaxDateActionPerformed(evt);
            }
        });

        ckbFiles.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ckbFiles.setSelected(true);
        ckbFiles.setText("Files");
        ckbFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbFilesActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Sort by:");

        cbxSorting.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        cbxSorting.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Created", "Date", "Title" }));
        cbxSorting.setToolTipText("");
        cbxSorting.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxSortingItemStateChanged(evt);
            }
        });

        ckbDescending.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        ckbDescending.setSelected(true);
        ckbDescending.setText("DESC");
        ckbDescending.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ckbDescendingStateChanged(evt);
            }
        });
        ckbDescending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbDescendingActionPerformed(evt);
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
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(ckbMinDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(ckbMaxDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMaxDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMinDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ckbRelations)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ckbDocs)
                                .addGap(18, 18, 18)
                                .addComponent(ckbInstitutions))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ckbFiles)
                                .addGap(18, 18, 18)
                                .addComponent(ckbTags)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxSorting, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckbDescending)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbDocs)
                    .addComponent(ckbInstitutions))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbFiles)
                    .addComponent(ckbTags))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ckbRelations)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spMaxResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(58, 58, 58)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbMinDate)
                    .addComponent(btnMinDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbMaxDate)
                    .addComponent(btnMaxDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxSorting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckbDescending))
                .addContainerGap())
        );

        //ckbDocs.setVisible(false);
        //ckbRelations.setVisible(false);
        //ckbInstitutions.setVisible(false);
        //ckbTags.setVisible(false);

        contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Results"));
        contentPanel.setPreferredSize(null);

        jButton1.setText("export");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnChangePW)
                            .addComponent(jButton1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1235, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(btnChangePW)
                        .addGap(18, 18, 18)
                        .addComponent(btnAdd)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtSearchInputMethodTextChanged
        //delete me
    }//GEN-LAST:event_txtSearchInputMethodTextChanged

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        doSearch(1000);
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
            doSearch(100);
        }
    }//GEN-LAST:event_btnMinDateActionPerformed

    private void btnMaxDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaxDateActionPerformed
        JDatePanel datePanel = (JDatePanelImpl) JDateComponentFactory.createJDatePanel();
        int okCxl = JOptionPane.showConfirmDialog(null, datePanel, "Datum auswählen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            DateModel<?> dm = ((JDatePanel) datePanel).getModel();
            btnMaxDate.setText(dm.getDay() + "." + (1 + dm.getMonth()) + "." + dm.getYear());
            doSearch(100);
        }
    }//GEN-LAST:event_btnMaxDateActionPerformed

    private void ckbMinDateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ckbMinDateStateChanged
        btnMinDate.setEnabled(ckbMinDate.isSelected());
    }//GEN-LAST:event_ckbMinDateStateChanged

    private void ckbMaxDateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ckbMaxDateStateChanged
        btnMaxDate.setEnabled(ckbMaxDate.isSelected());
    }//GEN-LAST:event_ckbMaxDateStateChanged

    private void ckbInstitutionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbInstitutionsActionPerformed
        doSearch(100);
    }//GEN-LAST:event_ckbInstitutionsActionPerformed

    private void ckbDocsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbDocsActionPerformed
        doSearch(100);
    }//GEN-LAST:event_ckbDocsActionPerformed

    private void ckbFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbFilesActionPerformed
        doSearch(100);
    }//GEN-LAST:event_ckbFilesActionPerformed

    private void ckbTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbTagsActionPerformed
        doSearch(100);
    }//GEN-LAST:event_ckbTagsActionPerformed

    private void ckbRelationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbRelationsActionPerformed
        doSearch(100);
    }//GEN-LAST:event_ckbRelationsActionPerformed

    private void spMaxResultStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spMaxResultStateChanged

    }//GEN-LAST:event_spMaxResultStateChanged

    private void cbxSortingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxSortingItemStateChanged

    }//GEN-LAST:event_cbxSortingItemStateChanged

    private void ckbDescendingStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ckbDescendingStateChanged

    }//GEN-LAST:event_ckbDescendingStateChanged

    private void ckbDescendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbDescendingActionPerformed
        doSearch(100);
    }//GEN-LAST:event_ckbDescendingActionPerformed

    private void ckbMinDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbMinDateActionPerformed
        doSearch(100);
    }//GEN-LAST:event_ckbMinDateActionPerformed

    private void ckbMaxDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbMaxDateActionPerformed
        doSearch(100);
    }//GEN-LAST:event_ckbMaxDateActionPerformed

    private void spMaxResultInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_spMaxResultInputMethodTextChanged

    }//GEN-LAST:event_spMaxResultInputMethodTextChanged

    private void spMaxResultCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_spMaxResultCaretPositionChanged
        doSearch(1000);
    }//GEN-LAST:event_spMaxResultCaretPositionChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            //export dir
            File exportDir = new File("C:\\Users\\Michael\\Downloads\\export");
            File entityFile = new File("C:\\Users\\Michael\\Downloads\\export\\entities.xml");
            File relationFile = new File("C:\\Users\\Michael\\Downloads\\export\\relations.xml");
            File tagFile = new File("C:\\Users\\Michael\\Downloads\\export\\tags.xml");
            File fileFile = new File("C:\\Users\\Michael\\Downloads\\export\\files.xml");
            File fileDir = new File("C:\\Users\\Michael\\Downloads\\export\\files");
            fileDir.mkdirs();

            //entity xml doc
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.newDocument();
            Element root = document.createElement("entities");
            document.appendChild(root);

            DB.DBResult r = DB.select("SELECT id, title, description, date, created, type FROM entities");
            while(r.resultSet.next()){
                Element entity = document.createElement("entity");
                Element id = document.createElement("id");
                id.setTextContent(r.resultSet.getString(1));
                entity.appendChild(id);
                Element title = document.createElement("title");
                title.setTextContent(r.resultSet.getString(2));
                entity.appendChild(title);
                Element description = document.createElement("description");
                description.setTextContent(r.resultSet.getString(3));
                entity.appendChild(description);
                Element date = document.createElement("date");
                date.setTextContent(r.resultSet.getString(4));
                entity.appendChild(date);
                Element created = document.createElement("created");
                created.setTextContent(r.resultSet.getString(5));
                entity.appendChild(created);
                Element type = document.createElement("type");
                type.setTextContent(r.resultSet.getString(6));
                entity.appendChild(type);
                root.appendChild(entity);
            }
            
            saveXMLDocument(document, entityFile);
            r.close();
            
            
            //tags xml doc
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = db.newDocument();
            root = document.createElement("tags");
            document.appendChild(root);

            r = DB.select("SELECT id, tag FROM tags");
            while(r.resultSet.next()){
                Element tag = document.createElement("tag");
                Element id = document.createElement("id");
                id.setTextContent(r.resultSet.getString(1));
                tag.appendChild(id);
                Element tagname = document.createElement("tag");
                tagname.setTextContent(r.resultSet.getString(2));
                tag.appendChild(tagname);
                root.appendChild(tag);
            }
            
            saveXMLDocument(document, tagFile);
            r.close();
            
            
            //relation xml doc
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = db.newDocument();
            root = document.createElement("relations");
            document.appendChild(root);

            r = DB.select("SELECT id, title, description, created, entity1, entity2 FROM relations");
            while(r.resultSet.next()){
                Element relation = document.createElement("relation");
                Element id = document.createElement("id");
                id.setTextContent(r.resultSet.getString(1));
                relation.appendChild(id);
                Element title = document.createElement("title");
                title.setTextContent(r.resultSet.getString(2));
                relation.appendChild(title);
                Element description = document.createElement("description");
                description.setTextContent(r.resultSet.getString(3));
                relation.appendChild(description);
                Element created = document.createElement("created");
                created.setTextContent(r.resultSet.getString(4));
                relation.appendChild(created);
                Element entity1 = document.createElement("entity1");
                entity1.setTextContent(r.resultSet.getString(5));
                relation.appendChild(entity1);
                Element entity2 = document.createElement("entity2");
                entity2.setTextContent(r.resultSet.getString(6));
                relation.appendChild(entity2);
                root.appendChild(relation);
            }
            
            saveXMLDocument(document, relationFile);
            r.close();
            
            
            //relation xml doc
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = db.newDocument();
            root = document.createElement("relations");
            document.appendChild(root);

            r = DB.select("SELECT id, name, created, file, size, ocr FROM files");
            while(r.resultSet.next()){
                Element file = document.createElement("file");
                Element id = document.createElement("id");
                id.setTextContent(r.resultSet.getString(1));
                file.appendChild(id);
                Element name = document.createElement("name");
                name.setTextContent(r.resultSet.getString(2));
                file.appendChild(name);
                Element created = document.createElement("created");
                created.setTextContent(r.resultSet.getString(3));
                file.appendChild(created);
                Element size = document.createElement("size");
                size.setTextContent(r.resultSet.getString(4));
                file.appendChild(size);
                Element ocr = document.createElement("ocr");
                ocr.setTextContent(r.resultSet.getString(5));
                file.appendChild(ocr);
                root.appendChild(file);
                
                Entity e = DataHandler.instance.getEntityByID(r.resultSet.getLong(1));
                DataHandler.instance.writeFileToStream(e, r.resultSet.getString(2), new FileOutputStream(fileDir.getAbsolutePath()+File.separator+""));
            }
            
            saveXMLDocument(document, fileFile);
            r.close();

        } catch (Exception ex) {
            Log.l(ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void saveXMLDocument(Document document, File file) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);
        } catch (Exception ex) {
            Log.l(ex);
        }
    }

    public void doSearch(final long delay) {
        if (searchProgress != null && !searchProgress.isFinished()) {
            searchProgress.cancel(); //concurrency problems????
        }

        searchProgress = new WaitDialog.AsyncProcess("search") {
            Entity[] findings = null;

            @Override
            public void start() throws Exception {

                DataHandler.Sorting sorting = DataHandler.Sorting.Date;

                switch (cbxSorting.getSelectedItem().toString()) {
                    case "Date":
                        sorting = DataHandler.Sorting.Date;
                        break;
                    case "Created":
                        sorting = DataHandler.Sorting.Created;
                        break;
                    case "Title":
                        sorting = DataHandler.Sorting.Title;
                        break;
                }

                findings = DataHandler.instance.search(txtSearch.getText().split(" "),
                        ckbDocs.isSelected(),
                        ckbInstitutions.isSelected(),
                        ckbRelations.isSelected(),
                        ckbTags.isSelected(),
                        ckbMinDate.isSelected() ? DateFormat.getDateInstance().parse(btnMinDate.getText()) : null,
                        ckbMaxDate.isSelected() ? DateFormat.getDateInstance().parse(btnMaxDate.getText()) : null,
                        ckbFiles.isSelected(),
                        ((Integer) spMaxResult.getModel().getValue()),
                        sorting,
                        ckbDescending.isSelected() ? DataHandler.SortingOrder.Descending : DataHandler.SortingOrder.Ascending
                );
            }

            @Override
            public void finished(boolean success) {
                try {
                    if (!searchProgress.isCanceled() && findings != null) {
                        contentPanel.showResults(findings);
                    }
                } catch (Exception ex) {
                    Log.l(ex);
                }
            }
        };
        new WaitDialog(null, searchProgress, true, false, delay, "Searching for Entities");
    }

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
    private javax.swing.JComboBox cbxSorting;
    private javax.swing.JCheckBox ckbDescending;
    private javax.swing.JCheckBox ckbDocs;
    private javax.swing.JCheckBox ckbFiles;
    private javax.swing.JCheckBox ckbInstitutions;
    private javax.swing.JCheckBox ckbMaxDate;
    private javax.swing.JCheckBox ckbMinDate;
    private javax.swing.JCheckBox ckbRelations;
    private javax.swing.JCheckBox ckbTags;
    private docz.ContentPanel contentPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner spMaxResult;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
