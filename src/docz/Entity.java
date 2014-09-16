/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.w3c.dom.Document;

/**
 *
 * @author Michael
 */
public class Entity {
    protected long id;
    protected String title, description;
    protected List<String> tags;
    protected Date date, created;

    public Date getCreated() {
        return created;
    }

    public Date getDate() {
        return date;
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
    
    public String[] getTags(){
        return tags.toArray(new String[tags.size()]);
    }
    
    public void setTags(String... tags){
        this.tags = new ArrayList<>(tags.length);
        for(String tag : tags){
            this.tags.add(tag);
        }
    }
    
    public void setTags(List<String> tags){
        this.tags = tags;
    }
    
    public void save(Document DB){
        
    }
}
