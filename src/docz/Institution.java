/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michael
 */
public class Institution extends Entity{
    
    private long id;
    private List<String> tags;
    private Date created;
    private Image logo;
    private final Element node;
    private boolean isModified = false;

    public Institution(Element institutionNode) {
        node = institutionNode;

        try {
            id = Long.parseLong(node.getElementsByTagName("id").item(0).getTextContent());
            DataHandler.instance.updateLastID(id);
        } catch (Exception ex) {
            Log.l(ex);
        }
        
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
            Node tagsNode = node.getElementsByTagName("tags").item(0);
            NodeList tagNodes = tagsNode.getChildNodes();
            for (int i = 0; i < tagNodes.getLength(); i++) {
                tags.add(tagNodes.item(i).getTextContent());
            }
        } catch (Exception ex) { 
            Log.l(ex);
        }
    }

    public static Institution createInstitution(Document DB, String title, String description, List<String> tags, Date date, List<File> files) {
        Element node = DB.createElement("institution");

        Element idN = DB.createElement("id");
        idN.setTextContent(DataHandler.instance.getNewID()+"");
        node.appendChild(idN);
        
        Element titleN = DB.createElement("title");
        titleN.setTextContent(title);
        node.appendChild(titleN);

        Element descriptionN = DB.createElement("description");
        descriptionN.setTextContent(description);
        node.appendChild(descriptionN);

        Element tagsN = DB.createElement("tags");
        if (tags != null) {
            for (String tag : tags) {
                Element tagN = DB.createElement("tag");
                tagN.setTextContent(tag);
                tagsN.appendChild(tagN);
            }
        }
        node.appendChild(tagsN);
        
        Element createdN = DB.createElement("created");
        createdN.setTextContent(DateFormat.getDateTimeInstance().format(new Date()));
        node.appendChild(createdN);     
        
        Element logoN = DB.createElement("logo");
        node.appendChild(logoN);

        return new Institution(node);
    }

    public Element getNode() {
        return node;
    }

    public List<String> getTags() {
        return tags;
    }

    public Date getCreated() {
        return created;
    }
    public long getID() {
        return id;
    }

    public Image getLogo() {
        if (logo == null) {
            try {
                String path = node.getElementsByTagName("logo").item(0).getTextContent();
                logo = ImageIO.read(new File(path));
            } catch (Exception ex) {
                Log.l(ex);
                logo = Resources.img_nofiles;
            }
        }
            
        return logo;
    }
    
    public void setLogo(String file){
        try {
            node.getElementsByTagName("logo").item(0).setTextContent(file);
            logo = null;
            isModified = true;
        } catch (Exception ex) {
            Log.l(ex);
        }
    }

    public boolean isIsModified() {
        return isModified;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        isModified = true;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        isModified = true;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        isModified = true;
    }
    
    public void save(Document DB){
        
        try {
            node.getElementsByTagName("title").item(0).setTextContent(title);
        } catch (Exception ex) {
            Log.l(ex);
        }
        try {
            node.getElementsByTagName("description").item(0).setTextContent(description);
        } catch (Exception ex) {
            Log.l(ex);
        }

        try {
            node.getElementsByTagName("created").item(0).setTextContent(DateFormat.getDateTimeInstance().format(created));
        } catch (Exception ex) {
            Log.l(ex);
        }

        try {
            
            Node tagsNode = node.getElementsByTagName("tags").item(0);
            NodeList tagNodes = tagsNode.getChildNodes();
            for (int i = 0; i < tagNodes.getLength(); i++) {
                tagsNode.removeChild(tagNodes.item(i));
                i--;
            }
            for (String tag : tags) {
                Element tagN = DB.createElement("tag");
                tagN.setTextContent(tag);
                tagsNode.appendChild(tagN);
            }
        } catch (Exception ex) { 
            Log.l(ex);
        }
                
        isModified = false;
    }
    
}
