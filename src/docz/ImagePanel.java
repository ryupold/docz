/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import de.realriu.riulib.helpers.ScaleImage.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Michael
 */
public class ImagePanel extends JPanel {

    private File imgFile = null;
    private Image scaledImg = null;
    private Rectangle preferedSize;
    private int camX = 0, camY = 0;
    private float zoom = 1f;
    private Point startDrag = null;

    public ImagePanel() {
        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    if (imgFile != null) {
                        setImg(imgFile);
                    } else {
                        repaint();
                    }
                } catch (IOException ex) {
                    Log.l(ex);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                zoom -= e.getPreciseWheelRotation();
                zoom = Math.max(Math.min(10f, zoom), 1f);
                repaint();
            }
        });
        
        

        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                //Log.l("drag:" + e.getButton());
                camX = getWidth() - e.getX();
                camY = getHeight() - e.getY();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    public File getImg() {
        return imgFile;
    }

    public Image getScaledImg() {
        return scaledImg;
    }

    private void resetCam() {
        camX = getWidth() / 2;
        camY = getHeight() / 2;
        zoom = 1f;
    }

    public void setImg(File imgFile) throws IOException {
        this.imgFile = imgFile;
        if (imgFile != null) {
            BufferedImage img = ImageIO.read(imgFile);
            if (img != null) {
                preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, getWidth(), getHeight()), (BufferedImage) img);
                this.scaledImg = ScaleImage.scale((BufferedImage) img, preferedSize.width, preferedSize.heigth);
            } else {
                scaledImg = null;
                this.imgFile = null;
            }
        }

        resetCam();

        repaint();
    }

    public void setImg(Image img) {
        scaledImg = img;
        preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, getWidth(), getHeight()), (BufferedImage) img);

        resetCam();

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (scaledImg != null && getWidth() > 0 && getHeight() > 0) {
            Log.l("zoom = " + zoom + "    X = " + camX + "    Y = " + camY);
            g.drawImage(scaledImg,
                    /*X*/ (int) (preferedSize.x - (camX * zoom - getWidth() / 2f)),
                    /*Y*/ (int) (preferedSize.y - (camY * zoom - getHeight() / 2f)),
                    /*W*/ (int) (preferedSize.width * zoom),
                    /*H*/ (int) (preferedSize.heigth * zoom),
                    this);
        }
    }

}
