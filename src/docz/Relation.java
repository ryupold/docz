/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

import java.text.DateFormat;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Michael
 */
public class Relation extends Entity{
    private String title, description;
    private Date created;
    private Element node;
    private long from, to;
    private boolean isModified = false;

    public Relation(Element relationNode) {
        node = relationNode;

        try {
            title = node.getElementsByTagName("title").item(0).getTextContent();
        } catch (Exception ex) {
            Log.l(ex);
        }
        try {
            description = node.getElementsByTagName("description").item(0).getTextContent();
        } catch (Exception ex) {
            Log.l(ex);
        }
        
        try {
            created = DateFormat.getDateTimeInstance().parse(node.getElementsByTagName("created").item(0).getTextContent());
        } catch (Exception ex) {
            Log.l(ex);
        }

        try {
            from = Long.parseLong(node.getElementsByTagName("from").item(0).getTextContent());
        } catch (Exception ex) {
            Log.l(ex);
        }
        
        try {
            to = Long.parseLong(node.getElementsByTagName("to").item(0).getTextContent());
        } catch (Exception ex) {
            Log.l(ex);
        }
    }

    public static Doc createRelation(Document DB, String title, String description, Doc From, Doc To) {
        Element node = DB.createElement("doc");
        
        Element titleN = DB.createElement("title");
        titleN.setTextContent(title);
        node.appendChild(titleN);

        Element descriptionN = DB.createElement("description");
        descriptionN.setTextContent(description);
        node.appendChild(descriptionN);      
        
        Element createdN = DB.createElement("created");
        createdN.setTextContent(DateFormat.getDateTimeInstance().format(new Date()));
        node.appendChild(createdN);

        Element fromN = DB.createElement("from");
        fromN.setTextContent(From.getID()+"");
        node.appendChild(fromN);
        
        Element toN = DB.createElement("to");
        toN.setTextContent(From.getID()+"");
        node.appendChild(toN);

        return new Doc(node);
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public Element getNode() {
        return node;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreated() {
        return created;
    }
    
    public boolean isIsModified() {
        return isModified;
    }

    public void setTitle(String title) {
        this.title = title;
        isModified = true;
    }

    public void setDescription(String description) {
        this.description = description;
        isModified = true;
    }

    public void setFrom(long from) {
        this.from = from;
        isModified = true;
    }

    public void setTo(long to) {
        this.to = to;
        isModified = true;
    }
    
    public void save(){
        isModified = false;
    }    
}
