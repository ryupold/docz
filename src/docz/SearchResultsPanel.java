/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

import de.realriu.riulib.helpers.ScaleImage;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Michael
 */
public class SearchResultsPanel extends JPanel{
    
    private List<Entity> resultEntities;
    private List<Relation> resultRelations;
    private Image[] thumbnails;
    private String[] names;
    private ScaleImage.Rectangle[] rects;
    private int prefW = 150, prefH=200, paddingLR=10, paddingTB=30;
    
    
    public void showResults(List<Entity> resultEntities, List<Relation> resultRelations) throws SQLException{
        this.resultEntities = resultEntities;
        this.resultRelations = resultRelations;
        
        thumbnails = new Image[resultEntities.size()];
        rects = new ScaleImage.Rectangle[thumbnails.length];
        names = new String[thumbnails.length];
        
        for (int i = 0; i < resultEntities.size(); i++) {
            thumbnails[i] = resultEntities.get(i).getThumbnail(prefW, prefH);
            rects[i] = ScaleImage.fitToRect(prefW, prefH, (BufferedImage)thumbnails[i]);
            names[i] = resultEntities.get(i).getTitle();
        }
        
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        for (int i = 0; i < thumbnails.length; i++) {
            int imgsPerRow = (int)(1f*getWidth()/(prefW + 2*paddingLR));
            int x = (i%imgsPerRow) * (prefW + 2*paddingLR) + rects[i].x;
            int y = (i/imgsPerRow) * (prefH + 2* paddingTB) + rects[i].y;
            
            g.drawImage(thumbnails[i], x, y, this);
        }
    }
    
    
}
