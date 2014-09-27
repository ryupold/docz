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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import javax.swing.JScrollPane;

/**
 *
 * @author Michael
 */
public class ImageList extends javax.swing.JPanel {

    private Thumbnail[] thumbnails;
    private Image[] images;
    private String[] names, descriptions;
    private ScaleImage.Rectangle[] rects;
    private ScaleImage.Rectangle[] bounds;
    private int hoveredIndex = -1, selectedIndex = -1;
    private int preferedWidth = 150, preferedHeight = 150, paddingLR = 20, paddingTB = 30;

    private ImageListListener listener;

    /**
     * Creates new form ImageList
     */
    public ImageList() {
        initComponents();

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (bounds != null) {
                    boolean hit = false;
                    for (int i = 0; i < bounds.length && !hit; i++) {
                        Point p = e.getPoint();
                        if (p.x > bounds[i].x && p.x < bounds[i].x + bounds[i].width
                                && p.y > bounds[i].y && p.y < bounds[i].y + bounds[i].heigth) {
                            hit = true;
                            boolean newHover = hoveredIndex != i;
                            hoveredIndex = i;
                            if (newHover) {
                                setToolTipText(descriptions[hoveredIndex]);
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

    public void setImageListListener(ImageListListener listener) {
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

    public void setPreferedWidth(int preferedWidth) {
        this.preferedWidth = preferedWidth;
    }

    public void setPreferedHeight(int preferedHeight) {
        this.preferedHeight = preferedHeight;
    }

    public void setThumbnails(Thumbnail[] thumbnails) throws Exception {
        this.thumbnails = thumbnails;

        images = new Image[thumbnails.length];
        rects = new ScaleImage.Rectangle[images.length];
        bounds = new ScaleImage.Rectangle[images.length];
        names = new String[images.length];
        descriptions = new String[images.length];

        for (int i = 0; i < thumbnails.length; i++) {
            images[i] = thumbnails[i].getThumbnail(preferedWidth, preferedHeight, null);
            rects[i] = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, preferedWidth, preferedHeight), (BufferedImage) images[i]);
            names[i] = thumbnails[i].getTitle();
            descriptions[i] = thumbnails[i].getDescription();
        }

        if (getParent().getParent() instanceof JScrollPane) {
            ((JScrollPane) getParent().getParent()).getVerticalScrollBar().setUnitIncrement(20);
        }

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

        recalcPositions();
    }

    public Thumbnail[] getThumbnails() {
        return thumbnails;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public int getHoveredIndex() {
        return hoveredIndex;
    }

    private void recalcPositions() throws SQLException {
        if (thumbnails.length > 0) {
            for (int i = 0; i < thumbnails.length; i++) {
                int imgsPerRow = Math.max((int) (1f * getParent().getWidth() / (preferedWidth + 2 * paddingLR)), 1);
                int x = (i % imgsPerRow) * (preferedWidth + 2 * paddingLR);
                int y = (i / imgsPerRow) * (preferedHeight + 2 * paddingTB);
                bounds[i] = new ScaleImage.Rectangle(x, y, preferedWidth, preferedHeight);
            }

            Rectangle r = new Rectangle(bounds[0].x, bounds[0].y, bounds[bounds.length - 1].x - bounds[0].x + bounds[bounds.length - 1].width, bounds[bounds.length - 1].y - bounds[0].y + bounds[bounds.length - 1].heigth);
            repaint();
            setPreferredSize(new Dimension(r.width, r.height));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        try{
        if (images != null && names != null && rects != null) {
            for (int i = 0; i < images.length; i++) {
                g.drawRect(bounds[i].x, bounds[i].y, bounds[i].width, bounds[i].heigth);
                g.drawImage(images[i], bounds[i].x + rects[i].x, bounds[i].y + rects[i].y, this);
                double shorteningFactor = g.getFontMetrics().stringWidth(names[i]) * 1.0 / preferedWidth;
                names[i] = shorteningFactor <= 1.0 ? names[i]
                        : (names[i].length() >= (int) (names[i].length() / shorteningFactor)
                        ? names[i].substring(0, (int) (names[i].length() / shorteningFactor)) : names[i]);
                g.drawString(names[i], bounds[i].x, bounds[i].y + bounds[i].heigth + 12);
            }
        }

        if (hoveredIndex >= 0) {
            g.setColor(Color.yellow);
            g.drawRect(bounds[hoveredIndex].x, bounds[hoveredIndex].y, bounds[hoveredIndex].width, bounds[hoveredIndex].heigth);
            g.drawRect(bounds[hoveredIndex].x - 1, bounds[hoveredIndex].y - 1, bounds[hoveredIndex].width + 2, bounds[hoveredIndex].heigth + 2);
        }

        if (selectedIndex >= 0 && selectedIndex < bounds.length) {
            g.setColor(Color.cyan);
            g.drawRect(bounds[selectedIndex].x, bounds[selectedIndex].y, bounds[selectedIndex].width, bounds[selectedIndex].heigth);
            g.drawRect(bounds[selectedIndex].x - 1, bounds[selectedIndex].y - 1, bounds[selectedIndex].width + 2, bounds[selectedIndex].heigth + 2);
        }
        }catch(Exception e){
            Log.l(e, false);
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
