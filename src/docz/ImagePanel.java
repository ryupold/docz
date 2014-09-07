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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Michael
 */
public class ImagePanel extends JPanel{

    private File imgFile = null;
    private Image scaledImg = null;
    private Rectangle preferedSize;

    public File getImg() {
        return imgFile;
    }

    public void setImg(File imgFile) throws IOException {
        this.imgFile = imgFile;
        BufferedImage img = ImageIO.read(imgFile);
        preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, getWidth(), getHeight()), (BufferedImage)img);
        this.scaledImg = ScaleImage.scale((BufferedImage)img, preferedSize.width, preferedSize.heigth);
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        if(scaledImg != null && getWidth()>0 && getHeight()>0){
            g.drawImage(scaledImg, preferedSize.x, preferedSize.y, preferedSize.width, preferedSize.heigth, this);
        }
    }
    
}
