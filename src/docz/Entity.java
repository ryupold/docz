/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

import java.sql.Blob;
import java.util.Date;
import java.util.List;
import org.w3c.dom.Document;

/**
 *
 * @author Michael
 */
public class Entity {
    protected int id;
    protected String title, description;
    private List<String> tags;
    private Date date, created;
    
    public List<String> getTags() {
        return tags;
    }

    public Date getCreated() {
        return created;
    }

    public Date getDate() {
        return date;
    }

    public Blob[] getFiles() {
        return null;
    }

    public long getID() {
        return id;
    }
    
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
