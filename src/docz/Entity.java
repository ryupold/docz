/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

import org.w3c.dom.Document;

/**
 *
 * @author Michael
 */
public class Entity {
    protected String title, description;
    
    
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void save(Document DB){
        
    }
}
