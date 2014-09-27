/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Michael
 */
public abstract class Resources {

    private static Image img_loading;
    private static Image img_nofiles;
    private static Image img_pdf;
    private static Image img_otherfile;
    private static Image img_doc;
    private static Image img_institution;
    private static Image img_relation;
    private static String allowedTitleChars = "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase() + "!#$(){}[]0123456789.,+=-_ \t";

    private Resources() {
    }

    private static void loadImages() {
        String file = "none";
        Image loadingTmp = null, nofilesTmp = null, pdfTmp = null, otherfileTmp = null, docTmp = null, institutionTmp = null, relationTmp = null;
        try {
//            paypal = ImageIO.read(new File("paypal.jpg"));
//            nopaypal = ImageIO.read(new File("nopaypal.jpg"));
//            loading = ImageIO.read(new File("loading.png"));            
            file = "loading.png";
            loadingTmp = ImageIO.read(DocZMainFrame.class.getResource("/loading.png"));
            file = "nofiles.png";
            nofilesTmp = ImageIO.read(DocZMainFrame.class.getResource("/nofiles.png"));
            file = "pdf.png";
            pdfTmp = ImageIO.read(DocZMainFrame.class.getResource("/pdf.png"));
            file = "otherfile.png";
            otherfileTmp = ImageIO.read(DocZMainFrame.class.getResource("/otherfile.png"));
            file = "doc.png";
            docTmp = ImageIO.read(DocZMainFrame.class.getResource("/doc.png"));
            file = "institution.png";
            institutionTmp = ImageIO.read(DocZMainFrame.class.getResource("/institution.png"));
            file = "relation.png";
            relationTmp = ImageIO.read(DocZMainFrame.class.getResource("/relation.png"));
        } catch (IOException ex) {
            Log.l(ex);
        }

        img_loading = loadingTmp;
        img_nofiles = nofilesTmp;
        img_pdf = pdfTmp;
        img_otherfile = otherfileTmp;
        img_doc = docTmp;
        img_institution = institutionTmp;
        img_relation = relationTmp;
    }

    public static Image getImg_loading() {
        if (img_loading == null) {
            loadImages();
        }

        return img_loading;
    }

    public static Image getImg_nofiles() {
        if (img_nofiles == null) {
            loadImages();
        }

        return img_nofiles;
    }

    public static Image getImg_otherfile() {
        if (img_otherfile == null) {
            loadImages();
        }

        return img_otherfile;
    }

    public static Image getImg_pdf() {
        if (img_pdf == null) {
            loadImages();
        }

        return img_pdf;
    }

    public static Image getImg_doc() {
        if (img_doc == null) {
            loadImages();
        }

        return img_doc;
    }

    public static Image getImg_institution() {
        if (img_institution == null) {
            loadImages();
        }
        return img_institution;
    }

    public static Image getImg_relation() {
        if (img_relation == null) {
            loadImages();
        }

        return img_relation;
    }

    public static String getAllowedTitleChars() {
        return allowedTitleChars;
    }

    public static boolean copyFile(File from, File to) {
        try {
            InputStream in = new FileInputStream(from);
            OutputStream out = new FileOutputStream(to);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            return true;
        } catch (IOException ex) {
            Log.l(ex);
            return false;
        }
    }
    
    public static BufferedImage createRelationThumbnail(Entity entity1, Entity entity2, String title){
        return (BufferedImage)Resources.getImg_relation();
    }
    
    public static BufferedImage createRelationThumbnail(Relation relation){
        return (BufferedImage)Resources.getImg_relation();
    }

    public static BufferedImage createImageWithText(String text, int x, int y, int width, int height, Font font, Color textColor, Color background) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setColor(background);
        g.fillRect(0, 0, width, height);
        g.setColor(textColor);
        if(font != null) g.setFont(font);
        g.drawString(text, x, y);
        return img;
    }
            
    public static BufferedImage createImageWithText(String text, int width, int height, Color textColor, Color background) {
        return createImageWithText(text, 10, (int)(height / 2f + 5), width, height, null, textColor, background);
    }
    
    public static BufferedImage createImageWithText(String text, int width, int height) {
        return createImageWithText(text, 10, (int)(height / 2f + 5), width, height, null, Color.red, Color.black);
    }
    
    public static BufferedImage createImageWithText(String text, int width, int height, Font font, Color textColor, Color background) {
        return createImageWithText(text, 10, (int)(height / 2f + 5), width, height, font, textColor, background);
    }
    
    public static BufferedImage createImageWithText(String text, int width, int height, Font font) {
        return createImageWithText(text, 10, (int)(height / 2f + 5), width, height, font, Color.red, Color.black);
    }

}
