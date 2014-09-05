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
public class Canvas extends JPanel{
    public enum Mode{
        Search,
        Relations,
        Preview
    }
    
    private Mode mode = Mode.Search;
    
    public void showSearchResults(List<Entity> results)
    {
        mode = Mode.Search;
    }
    
    public void showRelations(Doc doc, List<Doc> docs, List<Relation> relations){
        mode = Mode.Relations;
    }
    
    public void showPreview(Doc doc){
        mode = Mode.Preview;
    }
    
    
    @Override
    public void paint(Graphics g) {
        switch(mode){
            case Search:
                
                break;
                
            case Relations:
                
                break;
                
            case Preview:
                
                break;
        }
    }
}
