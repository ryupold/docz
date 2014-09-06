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
public class Doc extends Entity{
    
    private long id;
    private List<String> tags;
    private Date date, created;
    private List<String> files;
    private Image thumbnail = null;
    private final Element node;
    private boolean isModified = false;

    public Doc(Element docNode) {
        node = docNode;

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
            date = DateFormat.getDateTimeInstance().parse(node.getElementsByTagName("date").item(0).getTextContent());
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

        try {
            Node filesNode = node.getElementsByTagName("files").item(0);
            NodeList fileNodes = filesNode.getChildNodes();
            for (int i = 0; i < fileNodes.getLength(); i++) {
                files.add(fileNodes.item(i).getTextContent());
            }
        } catch (Exception ex) {
            Log.l(ex);
        }
    }

    public static Doc createDoc(Document DB, String title, String description, List<String> tags, Date date, List<String> files) {
        Element node = DB.createElement("doc");

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
        
        Element dateN = DB.createElement("date");
        dateN.setTextContent(DateFormat.getDateTimeInstance().format(date));
        node.appendChild(dateN);
        
        Element createdN = DB.createElement("created");
        createdN.setTextContent(DateFormat.getDateTimeInstance().format(new Date()));
        node.appendChild(createdN);

        Element filesN = DB.createElement("files");
        if (files != null) {
            for (String file : files) {
                Element fileN = DB.createElement("file");
                fileN.setTextContent(file);
                filesN.appendChild(fileN);
            }
        }
        node.appendChild(filesN);

        return new Doc(node);
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

    public Date getDate() {
        return date;
    }
    
    public List<String> getFiles() {
        return files;
    }

    public long getID() {
        return id;
    }

    public Image getThumbnail() {
        if (thumbnail == null) {
            if (files.size() > 0) {
                File imageFile = null;
                File pdfFile = null;
                for (String f : files) {
                    String filename = f.toLowerCase();
                    if ((filename.endsWith(".jpg")
                            || filename.endsWith(".jpeg")
                            || filename.endsWith(".png")
                            || filename.endsWith(".bmp")
                            || filename.endsWith(".wbmp")
                            || filename.endsWith(".gif"))
                            && imageFile == null) {
                        imageFile = new File(f);
                    }

                    if (filename.endsWith(".pdf") && pdfFile == null) {
                        pdfFile = new File(f);
                    }
                }

                if (imageFile != null) {
                    try {
                        thumbnail = ImageIO.read(imageFile);
                    } catch (IOException ex) {
                        thumbnail = Resources.img_loading;
                        Log.l(ex);
                    }
                } else if (pdfFile != null) {
                    thumbnail = Resources.img_pdf;
                } else {
                    thumbnail = Resources.img_otherfile;
                }
            }

        } else {
            thumbnail = Resources.img_nofiles;
        }

        return thumbnail;
    }

    public boolean isIsModified() {
        return isModified;
    }

    public void setTitle(String title) {
        this.title = title;
        isModified = true;
    }

    public void setDate(Date date) {
        this.date = date;
        isModified = true;
    }

    public void setDescription(String description) {
        this.description = description;
        isModified = true;
    }

    public void setFiles(List<String> files) {
        this.files = files;
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
            node.getElementsByTagName("date").item(0).setTextContent(DateFormat.getDateTimeInstance().format(date));
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

        try {
            
            Node filesNode = node.getElementsByTagName("files").item(0);
            NodeList tagNodes = filesNode.getChildNodes();
            for (int i = 0; i < tagNodes.getLength(); i++) {
                filesNode.removeChild(tagNodes.item(i));
                i--;
            }
            for (String file : files) {
                Element fileN = DB.createElement("file");
                fileN.setTextContent(file);
                filesNode.appendChild(fileN);
            }
        } catch (Exception ex) {
            Log.l(ex);
        }
        
        isModified = false;
    }    
}
