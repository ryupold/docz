/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Michael
 */
public class Resources {
    public static final Image img_loading;
    public static final Image img_nofiles;
    public static final Image img_pdf;
    public static final Image img_otherfile;
    public static final String allowedTitleChars = "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase() + "!#$(){}[]0123456789.,+=-_ \t";
    
    static{
        String file = "none";
        Image loadingTmp = null, nofilesTmp = null, pdfTmp = null, otherfileTmp=null;
        try {
//            paypal = ImageIO.read(new File("paypal.jpg"));
//            nopaypal = ImageIO.read(new File("nopaypal.jpg"));
//            loading = ImageIO.read(new File("loading.png"));            
            file = "loading.png";
            loadingTmp = ImageIO.read(DocZ.class.getResource("/loading.png"));
            file = "nofiles.png";
            nofilesTmp = ImageIO.read(DocZ.class.getResource("/nofiles.png"));
            file = "pdf.png";
            pdfTmp = ImageIO.read(DocZ.class.getResource("/pdf.png"));
            file = "otherfile.png";
            otherfileTmp = ImageIO.read(DocZ.class.getResource("/otherfile.png"));
        } catch (IOException ex) {
            Log.l(ex);
        }
        
        img_loading = loadingTmp;
        img_nofiles = nofilesTmp;
        img_pdf = pdfTmp;
        img_otherfile = otherfileTmp;
    }
}
