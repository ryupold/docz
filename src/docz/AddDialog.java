/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.gui.imagelist.AbstractImageList.Alignment;
import de.realriu.riulib.gui.imagelist.ImageListAdapter;
import de.realriu.riulib.gui.imagelist.ScaledImageList;
import java.awt.Font;
import java.awt.Image;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
    private final List<Relation> relations = new ArrayList<>();
    private Entity tmpEntity = new Entity();
    private WaitDialog.AsyncProcess searchProgress = null;

    //DnD
    FileDrop fileDrop = null;

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
                if (pos >= 0 && pos < files.size()) {
                    try {
                        previewPanel.setImg(files.get(pos), true);
                    } catch (IOException ex) {
                        Log.l(ex);
                    }
                }
            }
        });

        imlSimilarEntities.setImageListListener(new ImageList.ImageListListener() {

            @Override
            public void imageHovered(int index) {

            }

            @Override
            public void imageSelected(int index) {
                btnAddToRelations.setEnabled(index >= 0);
            }

            @Override
            public void doubleClicked(int index) {

            }
        });

        imlRelated.setImageListListener(new ImageList.ImageListListener() {

            @Override
            public void imageHovered(int index) {

            }

            @Override
            public void imageSelected(int index) {
                btnRemoveFromRelations.setEnabled(index >= 0);
            }

            @Override
            public void doubleClicked(int index) {

            }
        });

        new FileDrop(imgList, new FileDrop.Listener() {

            @Override
            public void filesDropped(final File[] droppedFiles) {
                addFiles(droppedFiles);
                if(droppedFiles.length > 0 && txtDocTitle.getText().trim().length() == 0){
                    txtDocTitle.setText(droppedFiles[0].getName());
                }
                
                if(droppedFiles.length > 0 && txaDocDescription.getText().trim().length() == 0){
                    for (int i = 0; i < droppedFiles.length; i++) {
                        if(i == droppedFiles.length-1){
                            txaDocDescription.append(droppedFiles[i].getName());
                        }else{
                            txaDocDescription.append(droppedFiles[i].getName()+", ");
                        }
                    }
                }
            }
        });

        new FileDrop(imgLogo, new FileDrop.Listener() {

            @Override
            public void filesDropped(final File[] droppedFiles) {
                for (File f : droppedFiles) {
                    if (f.exists() && f.isFile()) {
                        String filename = f.getAbsolutePath().toLowerCase();
                        if ((filename.endsWith(".jpg")
                                || filename.endsWith(".jpeg")
                                || filename.endsWith(".png")
                                || filename.endsWith(".bmp")
                                || filename.endsWith(".wbmp")
                                || filename.endsWith(".gif"))) {
                            try {
                                ((ImagePanel)imgLogo).setImg(f);
                                if(droppedFiles.length > 0 && txtInstitutionTitle.getText().trim().length() == 0){
                                    txtInstitutionTitle.setText(f.getName());
                                }
                                break;
                            } catch (IOException ex) {
                                Log.l(ex);
                            }
                        }
                    }
                }
            }
        });

        setVisible(true);
    }

    private void addFiles(final File... newFiles) {
        WaitDialog wait = new WaitDialog(null, new WaitDialog.AsyncProcess("Loading files...") {

            @Override
            public void start() throws Exception {
                int progress = 1;
                for (File f : newFiles) {
                    if (f.exists() && f.isFile()) {
                        String filename = f.getAbsolutePath().toLowerCase();
                        processing(((float) progress / (float) newFiles.length), filename);
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
                        } else {
                            imgList.addImage(Resources.createImageWithText(f.getName(), imgList.getHeight(), imgList.getHeight()), f.getName());
                        }

                        files.add(f);
                    }
                    progress++;
                }
            }

            @Override
            public void finished(boolean success) {
                
            }
        }, false, "Adding files to new Entity");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method
     * is always regenerated by the Form Editor.
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
        txtDocTags = new javax.swing.JTextField();
        txtDocTitle = new javax.swing.JTextField();
        btnDocRemoveFile = new javax.swing.JButton();
        btnDocAddFile = new javax.swing.JButton();
        imgListDocFiles = imgList = new ScaledImageList(Alignment.Horizontal, true, 200, 200);
        btnDocSave = new javax.swing.JButton();
        datePicker = (JDatePanelImpl)net.sourceforge.jdatepicker.JDateComponentFactory.createJDatePanel();
        lblDocDate = new javax.swing.JLabel();
        imgPanelPreview = previewPanel = new ImagePanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaDocDescription = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtInstitutionTags = new javax.swing.JTextField();
        txtInstitutionTitle = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        imgLogo = imgPanel = new ImagePanel();
        btnInstitutionLogo = new javax.swing.JButton();
        btnInstitutionSave = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txaInstitutionDescription = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        splitSimilarRelatives = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        imlSimilarEntities = new docz.ImageList();
        jScrollPane3 = new javax.swing.JScrollPane();
        imlRelated = new docz.ImageList();
        btnRemoveFromRelations = new javax.swing.JButton();
        btnAddToRelations = new javax.swing.JButton();

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

        txtDocTags.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtDocTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocTagsActionPerformed(evt);
            }
        });
        txtDocTags.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocTagsKeyTyped(evt);
            }
        });

        txtDocTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtDocTitle.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtDocTitleCaretUpdate(evt);
            }
        });
        txtDocTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocTitleActionPerformed(evt);
            }
        });
        txtDocTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocTitleKeyTyped(evt);
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
            .addGap(0, 529, Short.MAX_VALUE)
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

        imgPanelPreview.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Preview", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N

        javax.swing.GroupLayout imgPanelPreviewLayout = new javax.swing.GroupLayout(imgPanelPreview);
        imgPanelPreview.setLayout(imgPanelPreviewLayout);
        imgPanelPreviewLayout.setHorizontalGroup(
            imgPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 378, Short.MAX_VALUE)
        );
        imgPanelPreviewLayout.setVerticalGroup(
            imgPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setToolTipText("");
        jScrollPane1.setFocusable(false);
        jScrollPane1.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N

        txaDocDescription.setColumns(20);
        txaDocDescription.setRows(5);
        txaDocDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaDocDescriptionKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(txaDocDescription);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDocAddFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDocRemoveFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDocSave))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtDocTitle)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imgListDocFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(70, 70, 70)
                                .addComponent(txtDocTags, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(10, 10, 10)
                                .addComponent(lblDocDate, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(imgPanelPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDocTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imgListDocFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDocAddFile)
                            .addComponent(btnDocRemoveFile))
                        .addGap(34, 34, 34))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(imgPanelPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDocSave))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        imgList.setPadding(0, 0, 5, 10);

        jTabbedPane1.addTab("+ Doc", jPanel1);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Title:");

        txtInstitutionTags.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtInstitutionTags.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInstitutionTagsKeyTyped(evt);
            }
        });

        txtInstitutionTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtInstitutionTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInstitutionTitleActionPerformed(evt);
            }
        });
        txtInstitutionTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInstitutionTitleKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Description:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Tags:");

        imgLogo.setBackground(getBackground());
        imgLogo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Logo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N

        javax.swing.GroupLayout imgLogoLayout = new javax.swing.GroupLayout(imgLogo);
        imgLogo.setLayout(imgLogoLayout);
        imgLogoLayout.setHorizontalGroup(
            imgLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imgLogoLayout.setVerticalGroup(
            imgLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 223, Short.MAX_VALUE)
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

        txaInstitutionDescription.setColumns(20);
        txaInstitutionDescription.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txaInstitutionDescription.setRows(5);
        txaInstitutionDescription.setToolTipText("");
        txaInstitutionDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaInstitutionDescriptionKeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(txaInstitutionDescription);

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
                            .addComponent(txtInstitutionTitle)
                            .addComponent(jScrollPane4)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(71, 71, 71)
                        .addComponent(txtInstitutionTags, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(imgLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnInstitutionLogo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txtInstitutionTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInstitutionSave)
                            .addComponent(btnInstitutionLogo)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(imgLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("+ Institution", jPanel2);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setText("similarities / relations");

        splitSimilarRelatives.setDividerLocation(300);
        splitSimilarRelatives.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitSimilarRelatives.setDoubleBuffered(true);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout imlSimilarEntitiesLayout = new javax.swing.GroupLayout(imlSimilarEntities);
        imlSimilarEntities.setLayout(imlSimilarEntitiesLayout);
        imlSimilarEntitiesLayout.setHorizontalGroup(
            imlSimilarEntitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imlSimilarEntitiesLayout.setVerticalGroup(
            imlSimilarEntitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(imlSimilarEntities);

        splitSimilarRelatives.setTopComponent(jScrollPane2);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout imlRelatedLayout = new javax.swing.GroupLayout(imlRelated);
        imlRelated.setLayout(imlRelatedLayout);
        imlRelatedLayout.setHorizontalGroup(
            imlRelatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );
        imlRelatedLayout.setVerticalGroup(
            imlRelatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 635, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(imlRelated);

        splitSimilarRelatives.setRightComponent(jScrollPane3);

        btnRemoveFromRelations.setText("-");
        btnRemoveFromRelations.setEnabled(false);
        btnRemoveFromRelations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveFromRelationsActionPerformed(evt);
            }
        });

        btnAddToRelations.setText("+");
        btnAddToRelations.setEnabled(false);
        btnAddToRelations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToRelationsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(splitSimilarRelatives, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddToRelations)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveFromRelations)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(btnRemoveFromRelations)
                    .addComponent(btnAddToRelations))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitSimilarRelatives)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtInstitutionTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInstitutionTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInstitutionTitleActionPerformed

    private void btnDocSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocSaveActionPerformed
        if (txtDocTitle.getText().trim().length() > 0 && imgList.count() > 0) {
            WaitDialog waitDialog = new WaitDialog(null, new WaitDialog.AsyncProcess("Adding new Doc") {

                @Override
                public void start() throws Exception {
                    try {
                        processing(0.0f, "analysing tags...");
                        List<String> tags = new ArrayList<>();
                        String[] tagArray = txtDocTags.getText().split(",");
                        for (String t : tagArray) {
                            tags.add(t.trim().toLowerCase());
                        }
                        processing(0.1f, "saving doc data & files...");
                        Doc d = Doc.createDoc(txtDocTitle.getText(), txaDocDescription.getText(), tags, DateFormat.getDateInstance().parse(lblDocDate.getText()), files);
                        processing(0.9f, "adding relations...");
                        createRelations(relations, d);
                        processing(1f, "finished...");
                    } catch (SQLException | IOException | ParseException ex) {
                        Log.l(ex);
                        throw ex;
                    }
                }

                @Override
                public void finished(boolean success) {
                    AddDialog.this.setVisible(false);
                }

                @Override
                public void cancel() {
                    Log.l("adding Doc cannot be stopped");
                }
            }, false, "Saving new Doc");

            waitDialog.dispose();
            System.gc();
        }
    }//GEN-LAST:event_btnDocSaveActionPerformed

    private void createRelations(Collection<Relation> rels, Entity entity1) {
        for (Relation r : rels) {
            try {
                DataHandler.instance.createRelation(r.getTitle(), r.getDescription(), entity1, r.getEntity2());
            } catch (Exception e) {
                Log.l(e);
            }
        }
    }

    private void txtDocTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocTitleActionPerformed

    private void btnDocAddFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocAddFileActionPerformed
        String lastPath = DB.getSetting("lastpath", "");

        final JFileChooser fc = new JFileChooser(new File(lastPath));

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            addFiles(fc.getSelectedFiles());

            if (fc.getSelectedFiles().length > 0) {
                lastPath = fc.getSelectedFiles()[0].getParentFile().getPath();
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
            WaitDialog waitDialog = new WaitDialog(null, new WaitDialog.AsyncProcess("Adding new Institution") {

                @Override
                public void start() throws Exception {
                    try {
                        processing(0.0f, "analysing tags...");
                        List<String> tags = new ArrayList<>();
                        String[] tagArray = txtInstitutionTags.getText().split(",");
                        for (String t : tagArray) {
                            tags.add(t.trim().toLowerCase());
                        }

                        List<File> logo = new ArrayList<>();
                        logo.add(imgPanel.getImg());
                        processing(0.1f, "saving institution data & logo...");
                        Institution i = Institution.createInstitution(txtInstitutionTitle.getText().trim(), txaInstitutionDescription.getText().trim(), tags, logo);
                        processing(0.9f, "creating relations...");
                        createRelations(relations, i);
                        processing(1f, "finished...");
                    } catch (IOException | SQLException ex) {
                        Log.l(ex);
                    }
                }

                @Override
                public void finished(boolean success) {
                    AddDialog.this.setVisible(false);
                }

                @Override
                public void cancel() {
                    Log.l("adding Doc cannot be stopped");
                }
            }, false, "Saving new Institution");

            waitDialog.dispose();
            System.gc();
        }
    }//GEN-LAST:event_btnInstitutionSaveActionPerformed

    private void btnDocRemoveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocRemoveFileActionPerformed
        if (imgList.getSelectedImageIndex() >= 0) {
            files.remove(imgList.getSelectedImageIndex());
            imgList.removeImage(imgList.getSelectedImageIndex());
        }
    }//GEN-LAST:event_btnDocRemoveFileActionPerformed

    private void txtDocTitleCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtDocTitleCaretUpdate

    }//GEN-LAST:event_txtDocTitleCaretUpdate

    private void txtDocTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocTagsActionPerformed

    }//GEN-LAST:event_txtDocTagsActionPerformed

    private void btnRemoveFromRelationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveFromRelationsActionPerformed
        if (imlRelated.getSelectedIndex() >= 0) {
            Relation r = (Relation) imlRelated.getThumbnails()[imlRelated.getSelectedIndex()];
            relations.remove(r);
            try {
                imlRelated.setThumbnails(relations.toArray(new Relation[relations.size()]));
            } catch (Exception ex) {
                Log.l(ex);
            }
        }
    }//GEN-LAST:event_btnRemoveFromRelationsActionPerformed

    private void btnAddToRelationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToRelationsActionPerformed
        try {

            AddRelationDirectDialog addRelation = new AddRelationDirectDialog(null,
                    imlSimilarEntities.getThumbnails()[imlSimilarEntities.getSelectedIndex()]
                    .getThumbnail(450, 400, new Font("Arial", Font.BOLD, 20)));
            addRelation.setVisible(true);

            if (addRelation.isApproved()) {
                Relation newRelation = new Relation(-1, addRelation.getTitle(),
                        addRelation.getDescription(), new Date(), tmpEntity, (Entity) imlSimilarEntities.getThumbnails()[imlSimilarEntities.getSelectedIndex()]);
                relations.add(newRelation);
                try {
                    imlRelated.setThumbnails(relations.toArray(new Relation[relations.size()]));
                } catch (Exception ex) {
                    Log.l(ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AddDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddToRelationsActionPerformed

    private void txtDocTitleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocTitleKeyTyped
        showSimilarEntities(txtDocTitle.getText(), true, false);
    }//GEN-LAST:event_txtDocTitleKeyTyped

    private void txaDocDescriptionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaDocDescriptionKeyTyped
        showSimilarEntities(txaDocDescription.getText(), true, false);
    }//GEN-LAST:event_txaDocDescriptionKeyTyped

    private void txtDocTagsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocTagsKeyTyped
        showSimilarEntities(txtDocTags.getText(), false, true);
    }//GEN-LAST:event_txtDocTagsKeyTyped

    private void txtInstitutionTitleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInstitutionTitleKeyTyped
        showSimilarEntities(txtInstitutionTitle.getText(), true, false);
    }//GEN-LAST:event_txtInstitutionTitleKeyTyped

    private void txaInstitutionDescriptionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaInstitutionDescriptionKeyTyped
        showSimilarEntities(txaInstitutionDescription.getText(), true, false);
    }//GEN-LAST:event_txaInstitutionDescriptionKeyTyped

    private void txtInstitutionTagsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInstitutionTagsKeyTyped
        showSimilarEntities(txtInstitutionTags.getText(), false, true);
    }//GEN-LAST:event_txtInstitutionTagsKeyTyped

    private void showSimilarEntities(final String longSearchString, final boolean byTitleAndDescription, final boolean byTags) {

        if (searchProgress != null && !searchProgress.isFinished()) {
            searchProgress.cancel(); //concurrency problems????
        }

        searchProgress = new WaitDialog.AsyncProcess("search similarities") {
            Entity[] findings = null;

            @Override
            public void start() throws Exception {
                String[] searchWords = longSearchString.split(" ");
                findings = DataHandler.instance.search(searchWords, byTitleAndDescription, byTitleAndDescription, false, byTags, null, null, true, 10);
            }

            @Override
            public void finished(boolean success) {
                try {
                    if (!searchProgress.isCanceled() && findings != null) {
                        imlSimilarEntities.setThumbnails(findings);
                    }
                } catch (Exception ex) {
                    Log.l(ex);
                }
            }
        };
        new WaitDialog(null, searchProgress, true, false, 2000, "Searching for similarities");
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
            java.util.logging.Logger.getLogger(AddDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton btnAddToRelations;
    private javax.swing.JButton btnDocAddFile;
    private javax.swing.JButton btnDocRemoveFile;
    private javax.swing.JButton btnDocSave;
    private javax.swing.JButton btnInstitutionLogo;
    private javax.swing.JButton btnInstitutionSave;
    private javax.swing.JButton btnRemoveFromRelations;
    private javax.swing.JPanel datePicker;
    private javax.swing.JPanel imgListDocFiles;
    private javax.swing.JPanel imgLogo;
    private javax.swing.JPanel imgPanelPreview;
    private docz.ImageList imlRelated;
    private docz.ImageList imlSimilarEntities;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblDocDate;
    private javax.swing.JSplitPane splitSimilarRelatives;
    private javax.swing.JTextArea txaDocDescription;
    private javax.swing.JTextArea txaInstitutionDescription;
    private javax.swing.JTextField txtDocTags;
    private javax.swing.JTextField txtDocTitle;
    private javax.swing.JTextField txtInstitutionTags;
    private javax.swing.JTextField txtInstitutionTitle;
    // End of variables declaration//GEN-END:variables
}
