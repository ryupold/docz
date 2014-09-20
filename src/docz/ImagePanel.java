/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import de.realriu.riulib.helpers.ScaleImage.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public ImagePanel() {
        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    setImg(imgFile);
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
    }

    public File getImg() {
        return imgFile;
    }

    public void setImg(File imgFile) throws IOException {
        this.imgFile = imgFile;
        if (imgFile != null) {
            BufferedImage img = ImageIO.read(imgFile);
            if(img != null){
                preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, getWidth(), getHeight()), (BufferedImage) img);
                this.scaledImg = ScaleImage.scale((BufferedImage) img, preferedSize.width, preferedSize.heigth);
            }else{
                scaledImg = null;
                this.imgFile = null;
            }
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (scaledImg != null && getWidth() > 0 && getHeight() > 0) {
            g.drawImage(scaledImg, preferedSize.x, preferedSize.y, preferedSize.width, preferedSize.heigth, this);
        }
    }

}
