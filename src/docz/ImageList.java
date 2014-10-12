/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;

/**
 *
 * @author Michael
 */
public class ImageList extends javax.swing.JPanel {

    private List<Thumbnail> thumbnails = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
    private List<String> names = new ArrayList<>(), descriptions = new ArrayList<>();
    private List<ScaleImage.Rectangle> rects = new ArrayList<>();
    private List<ScaleImage.Rectangle> bounds = new ArrayList<>();
    private int hoveredIndex = -1, selectedIndex = -1;
    private int preferedWidth = 150, preferedHeight = 150, paddingLR = 20, paddingTB = 30;
    private Task task = null;

    private abstract class Task extends Thread {

        private boolean canceled = false;

        public Task(String name) {
            super(name);
            setDaemon(true);
        }

        @Override
        public abstract void run();

        public boolean isCanceled() {
            return canceled;
        }

        public void cancel() {
            canceled = true;
        }

    }

    private ImageListListener listener;

    /**
     * Creates new form ImageList
     */
    public ImageList() {
        initComponents();
        if (getParent() != null) {
            getParent().addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    try {
                        recalcPositions();
                    } catch (SQLException ex) {
                        Log.l(ex);
                    }
                }
            });
        }

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    if (bounds != null) {
                        boolean hit = false;
                        for (int i = 0; i < bounds.size() && !hit; i++) {
                            Point p = e.getPoint();
                            if (p.x > bounds.get(i).x && p.x < bounds.get(i).x + bounds.get(i).width
                                    && p.y > bounds.get(i).y && p.y < bounds.get(i).y + bounds.get(i).heigth) {
                                hit = true;
                                boolean newHover = hoveredIndex != i;
                                hoveredIndex = i;
                                if (newHover) {
                                    setToolTipText(descriptions.get(hoveredIndex));
                                    if (listener != null) {
                                        listener.imageHovered(hoveredIndex);
                                    }
                                }
                            }
                        }

                        if (!hit) {
                            hoveredIndex = -1;
                            setToolTipText(null);
                        }

                        repaint();
                    }
                } catch (Exception ex) {

                }
            }
        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (hoveredIndex >= 0) {
                    if (hoveredIndex != selectedIndex) {
                        selectedIndex = hoveredIndex;
                        if (listener != null) {
                            listener.imageSelected(selectedIndex);
                        }
                    } else {
                        if (listener != null) {
                            listener.doubleClicked(selectedIndex);
                        }
                    }
                }

                repaint();
            }
        });
    }

    public synchronized void setImageListListener(ImageListListener listener) {
        this.listener = listener;
    }

    public ImageListListener getImageListListener() {
        return this.listener;
    }

    public static interface ImageListListener {

        void imageHovered(int index);

        void imageSelected(int index);

        void doubleClicked(int index);
    }

    public synchronized void setPreferedWidth(int preferedWidth) {
        this.preferedWidth = preferedWidth;
    }

    public synchronized void setPreferedHeight(int preferedHeight) {
        this.preferedHeight = preferedHeight;
    }

    public void setThumbnails(final Thumbnail[] thumbnails) throws Exception {
        synchronized (this) {
            this.thumbnails.clear();
            images.clear();
            names.clear();
            descriptions.clear();
            rects.clear();
            bounds.clear();
        }
        if (task != null) {
            task.cancel();
        }

        repaint();

        task = new Task("setting thumbnails") {

            @Override
            public void run() {
                for (int i = 0; !isCanceled() && i < thumbnails.length; i++) {
                    try {
                        if (!isCanceled()) {
                            addThumbnail(thumbnails[i], false);
                        }
                    } catch (Exception ex) {
                        Log.l(ex);
                    }
                }
            }
        };

        task.start();
    }

    public Thumbnail[] getThumbnails() {
        return thumbnails.toArray(new Thumbnail[thumbnails.size()]);
    }

    public void addThumbnail(final Thumbnail t, boolean async) {
        if (async) {
            task = new Task("adding thumbnail") {

                @Override
                public void run() {
                    synchronized (ImageList.this) {
                        addThumbnail(t, false);
                    }
                }

            };

            if (getParent().getParent() instanceof JScrollPane) {
                ((JScrollPane) getParent().getParent()).getVerticalScrollBar().setUnitIncrement(20);
            }

            task.start();
        } else {
            try {
                Image im = t.getThumbnail(preferedWidth, preferedHeight, null);
                synchronized (this) {
                    thumbnails.add(t);
                    images.add(im);
                    rects.add(ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, preferedWidth, preferedHeight), (BufferedImage) images.get(images.size() - 1)));
                    names.add(t.getTitle());
                    descriptions.add(t.getDescription().trim().isEmpty() ? t.getTitle() : t.getDescription());
                    recalcPositions();
                }
                
                if (getParent().getParent() instanceof JScrollPane) {
                    ((JScrollPane) getParent().getParent()).getVerticalScrollBar().setUnitIncrement(20);
                }

                repaint();
            } catch (Exception ex) {
                Log.l(ex);
            }
        }
    }

    public void removeThumbnail(Thumbnail t) throws Exception {
        synchronized (this) {
            int index = thumbnails.indexOf(t);

            thumbnails.remove(index);
            images.remove(index);
            rects.remove(index);
            bounds.remove(index);
            names.remove(index);
            descriptions.remove(index);

            recalcPositions();
        }
        repaint();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public int getHoveredIndex() {
        return hoveredIndex;
    }

    private synchronized void recalcPositions() throws SQLException {
        if (thumbnails.size() > 0) {
            bounds.clear();
            for (int i = 0; i < thumbnails.size(); i++) {
                int imgsPerRow = Math.max((int) (1f * getParent().getWidth() / (preferedWidth + 2 * paddingLR)), 1);
                int x = (i % imgsPerRow) * (preferedWidth + 2 * paddingLR);
                int y = (i / imgsPerRow) * (preferedHeight + 2 * paddingTB);
                bounds.add(new ScaleImage.Rectangle(x, y, preferedWidth, preferedHeight));
            }

            Rectangle r = new Rectangle(bounds.get(0).x, bounds.get(0).y, bounds.get(bounds.size() - 1).x - bounds.get(0).x + bounds.get(bounds.size() - 1).width, bounds.get(bounds.size() - 1).y - bounds.get(0).y + bounds.get(bounds.size() - 1).heigth + 20);
            repaint();
            setPreferredSize(new Dimension(r.width, r.height));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image[] images;
        String[] names;
        String[] descriptions;
        ScaleImage.Rectangle[] bounds;
        ScaleImage.Rectangle[] rects;

        synchronized (this) {
            images = this.images.toArray(new Image[this.images.size()]);
            names = this.names.toArray(new String[this.names.size()]);
            descriptions = this.descriptions.toArray(new String[this.descriptions.size()]);
            bounds = this.bounds.toArray(new ScaleImage.Rectangle[this.bounds.size()]);
            rects = this.rects.toArray(new ScaleImage.Rectangle[this.rects.size()]);
        }

        try {
            if (images != null && names != null && rects != null) {
                for (int i = 0; i < bounds.length; i++) {
                    g.drawRect(bounds[i].x, bounds[i].y, bounds[i].width, bounds[i].heigth);
                    g.drawImage(images[i], bounds[i].x + rects[i].x, bounds[i].y + rects[i].y, this);
                    double shorteningFactor = g.getFontMetrics().stringWidth(names[i]) * 1.0 / preferedWidth;
                    names[i] = shorteningFactor <= 1.0 ? names[i]
                            : (names[i].length() >= (int) (names[i].length() / shorteningFactor)
                            ? names[i].substring(0, (int) (names[i].length() / shorteningFactor)) : names[i]);
                    g.drawString(names[i], bounds[i].x, bounds[i].y + bounds[i].heigth + 12);
                }
            }

            if (hoveredIndex >= 0 && hoveredIndex < bounds.length) {
                g.setColor(Color.yellow);
                g.drawRect(bounds[hoveredIndex].x, bounds[hoveredIndex].y, bounds[hoveredIndex].width, bounds[hoveredIndex].heigth);
                g.drawRect(bounds[hoveredIndex].x - 1, bounds[hoveredIndex].y - 1, bounds[hoveredIndex].width + 2, bounds[hoveredIndex].heigth + 2);
            }

            if (selectedIndex >= 0 && selectedIndex < bounds.length) {
                g.setColor(Color.cyan);
                g.drawRect(bounds[selectedIndex].x, bounds[selectedIndex].y, bounds[selectedIndex].width, bounds[selectedIndex].heigth);
                g.drawRect(bounds[selectedIndex].x - 1, bounds[selectedIndex].y - 1, bounds[selectedIndex].width + 2, bounds[selectedIndex].heigth + 2);
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ne) {
            //do nothing,this happens sometimes
            Log.l(ne, true);
        } catch (Exception e) {
            Log.l(e, true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 890, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 597, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
