/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import java.awt.CardLayout;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Michael
 */
public class SearchResultsPanel extends JPanel {

    private Entity[] resultEntities;
    private Relation[] resultRelations;
    private Image[] thumbnails;
    private String[] names;
    private ScaleImage.Rectangle[] rects;
    private ScaleImage.Rectangle[] bounds;
    private int hoveredIndex = -1, selectedIndex = -1;
    private int prefW = 150, prefH = 150, paddingLR = 10, paddingTB = 30;
    private ContentPanel contentPanel;

    public void setContentPanel(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    public SearchResultsPanel() {
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
                            hoveredIndex = i;
                        }
                    }

                    if (!hit) {
                        hoveredIndex = -1;
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
                    } else if (contentPanel != null) {
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "card4");
                    }
                }
            }
        });
    }

    public void showResults(Entity[] resultEntities, Relation[] resultRelations) throws SQLException {
        this.resultEntities = resultEntities;
        this.resultRelations = resultRelations;

        thumbnails = new Image[resultEntities.length];
        rects = new ScaleImage.Rectangle[thumbnails.length];
        bounds = new ScaleImage.Rectangle[thumbnails.length];
        names = new String[thumbnails.length];

        for (int i = 0; i < resultEntities.length; i++) {
            thumbnails[i] = resultEntities[i].getThumbnail(prefW, prefH);
            rects[i] = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, prefW, prefH), (BufferedImage) thumbnails[i]);
            names[i] = resultEntities[i].getTitle();
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

    private void recalcPositions() throws SQLException {
        for (int i = 0; i < resultEntities.length; i++) {
            int imgsPerRow = Math.max((int) (1f * getParent().getWidth() / (prefW + 2 * paddingLR)), 1);
            int x = (i % imgsPerRow) * (prefW + 2 * paddingLR);
            int y = (i / imgsPerRow) * (prefH + 2 * paddingTB);
            bounds[i] = new ScaleImage.Rectangle(x, y, prefW, prefH);
        }

        Rectangle r = new Rectangle(bounds[0].x, bounds[0].y, bounds[bounds.length - 1].x - bounds[0].x + bounds[bounds.length - 1].width, bounds[bounds.length - 1].y - bounds[0].y + bounds[bounds.length - 1].heigth);
        repaint();
        setPreferredSize(new Dimension(r.width, r.height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (thumbnails != null && names != null && rects != null) {
            for (int i = 0; i < thumbnails.length; i++) {
                g.drawRect(bounds[i].x, bounds[i].y, bounds[i].width, bounds[i].heigth);
                g.drawImage(thumbnails[i], bounds[i].x + rects[i].x, bounds[i].y + rects[i].y, this);
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
    }

}
