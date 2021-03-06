/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

/**
 *
 * @author Michael
 */
public class WaitDialog extends javax.swing.JDialog {

    private static interface ProcessingListener {

        void onProcess(double percent, String processingStep);
    }

    public static abstract class AsyncProcess {

        private ProcessingListener listener;
        private boolean finished = false, canceled = false;
        public final String title;

        public AsyncProcess(String title) {
            this.title = title;
        }

        public abstract void start() throws Exception;

        public void processing(double percent, String processingStep) {
            if (listener != null) {
                listener.onProcess(percent, processingStep);
            }
        }

        public abstract void finished(boolean success);

        public final boolean isFinished() {
            return finished;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public synchronized void cancel() {
            canceled = true;
            this.notifyAll();
        }
    }

    private final AsyncProcess process;
    private Thread thread;

    public WaitDialog(java.awt.Frame parent, final AsyncProcess async, String threadName) {
        this(parent, async, true, true, 0, threadName);
    }

    public WaitDialog(java.awt.Frame parent, final AsyncProcess async, final boolean cancleAble, String threadName) {
        this(parent, async, cancleAble, true, 0, threadName);
    }

    public WaitDialog(java.awt.Frame parent, final AsyncProcess async, long delay, String threadName) {
        this(parent, async, true, true, delay, threadName);
    }

    public WaitDialog(java.awt.Frame parent, final AsyncProcess async, final boolean cancleAble, long delay, String threadName) {
        this(parent, async, cancleAble, true, delay, threadName);
    }

    public WaitDialog(java.awt.Frame parent, final AsyncProcess async, final boolean cancleAble, final boolean showDialog, String threadName) {
        this(parent, async, cancleAble, showDialog, 0, threadName);
    }
    
    public WaitDialog(java.awt.Frame parent, final AsyncProcess async, final boolean cancleAble, final boolean showDialog, final long delay, String threadName) {
        this(parent, async, cancleAble, showDialog, delay, threadName, true);
    }
    /**
     * Creates new form WaitDialog
     *
     * @param parent
     * @param async
     */
    public WaitDialog(java.awt.Frame parent, final AsyncProcess async, final boolean cancleAble, final boolean showDialog, final long delay, String threadName, boolean modal) {
        super(parent, modal && showDialog);
        initComponents();
        btnCancel.setVisible(cancleAble);

        process = async;
        if (async == null) {
            throw new IllegalArgumentException("AsyncProcess was null!");
        }
        setTitle(process.title);
        process.listener = new ProcessingListener() {

            @Override
            public void onProcess(double percent, String processingStep) {
                onProcessUpdate(percent, processingStep);
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                //Log.l(this+" started");
                long delta = delay;
                try {
                    synchronized(process){
                        while (!process.canceled && delta > 0) {
                            long wait = delay;
                            delta -= wait;
                            process.wait(wait);                            
                        }
                    }
                    if(!process.canceled){
                        process.start();
                        process.finished = true;
                        process.finished(true);
                    }                    
                } catch (Exception e) {
                    process.finished = true;
                    process.finished(false);
                    Log.l(e);
                }finally{
                    setVisible(false);
                    dispose();
                    //Log.l(this+" finished");
                }
                
            }
        }, threadName!=null?threadName:"Thread that does things and waits "+delay+" ms before doing it.");

        thread.setDaemon(true);
        thread.start();
        if (showDialog) {
            setVisible(true);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        //Log.l(this+" finalized");
    }
    
    

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
    }

    private void onProcessUpdate(double percent, String processingStep) {
        pbProgress.setValue((int) (100 * percent));
        if (processingStep == null) {
            pbProgress.setString((percent * 100) + "%");
        } else {
            pbProgress.setString(processingStep);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method
     * is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCancel = new javax.swing.JButton();
        pbProgress = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        btnCancel.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        btnCancel.setText("cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        pbProgress.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        pbProgress.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel))
                    .addComponent(pbProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pbProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        process.cancel();
        setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JProgressBar pbProgress;
    // End of variables declaration//GEN-END:variables
}
