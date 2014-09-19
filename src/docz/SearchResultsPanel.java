/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Michael
 */
public class SearchResultsPanel extends JPanel{
    
    private List<Entity> resultEntities;
    private List<Relation> resultRelations;
    
    
    public void showResults(List<Entity> resultEntities, List<Relation> resultRelations){
        this.resultEntities = resultEntities;
        this.resultRelations = resultRelations;
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
    
    
}
