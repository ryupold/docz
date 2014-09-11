/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michael
 */
public class Doc extends Entity {

    private long id;
    private List<String> tags;
    private Date date, created;
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
            tags = new ArrayList<>();
            for (int i = 0; i < tagNodes.getLength(); i++) {
                tags.add(tagNodes.item(i).getTextContent());
            }
        } catch (Exception ex) {
            Log.l(ex);
        }
    }

    public static Doc createDoc(String title, String description, List<String> tags, Date date, List<File> files) {
        Element node = DataHandler.instance.DB.createElement("doc");
        final long docID = DataHandler.instance.getNewID();
        Element idN = DataHandler.instance.DB.createElement("id");
        idN.setTextContent(docID + "");
        node.appendChild(idN);

        Element titleN = DataHandler.instance.DB.createElement("title");
        titleN.setTextContent(title);
        node.appendChild(titleN);

        Element descriptionN = DataHandler.instance.DB.createElement("description");
        descriptionN.setTextContent(description);
        node.appendChild(descriptionN);

        Element tagsN = DataHandler.instance.DB.createElement("tags");
        if (tags != null) {
            for (String tag : tags) {
                Element tagN = DataHandler.instance.DB.createElement("tag");
                tagN.setTextContent(tag);
                tagsN.appendChild(tagN);
            }
        }
        node.appendChild(tagsN);

        Element dateN = DataHandler.instance.DB.createElement("date");
        dateN.setTextContent(DateFormat.getDateTimeInstance().format(date));
        node.appendChild(dateN);

        Element createdN = DataHandler.instance.DB.createElement("created");
        createdN.setTextContent(DateFormat.getDateTimeInstance().format(new Date()));
        node.appendChild(createdN);

        for (int i=0; i<files.size(); i++) {
            new File(DataHandler.instance.getDBDirectory()+"/doc_"+docID).mkdirs();
            File newPath = new File(DataHandler.instance.getDBDirectory()+"/doc_"+docID+"/"+files.get(i).getName());
            int fi=0;
            while(newPath.exists()){
                fi++;
                newPath = new File(DataHandler.instance.getDBDirectory()+"/doc_"+docID+"/"+fi+files.get(i).getName());
            }
            Resources.copyFile(files.get(i), newPath);
        }

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

    public File[] getFiles() {
        File docDir = new File("DB/doc_" + id + "/");
        if (docDir.exists() && docDir.isDirectory()) {
            return docDir.listFiles();
        } else {
            docDir.mkdirs();
        }

        return new File[0];
    }

    public long getID() {
        return id;
    }

    public Image getThumbnail() {
        if (thumbnail == null) {
            File[] files = getFiles();
            if (files.length > 0) {
                File imageFile = null;
                File pdfFile = null;
                for (File f : files) {
                    String filename = f.getName().toLowerCase();
                    if ((filename.endsWith(".jpg")
                            || filename.endsWith(".jpeg")
                            || filename.endsWith(".png")
                            || filename.endsWith(".bmp")
                            || filename.endsWith(".wbmp")
                            || filename.endsWith(".gif"))
                            && imageFile == null) {
                        imageFile = f;
                    }

                    if (filename.endsWith(".pdf") && pdfFile == null) {
                        pdfFile = f;
                    }
                }

                if (imageFile != null) {
                    try {
                        thumbnail = ImageIO.read(imageFile);
                    } catch (IOException ex) {
                        thumbnail = Resources.getImg_loading();
                        Log.l(ex);
                    }
                } else if (pdfFile != null) {
                    thumbnail = Resources.getImg_pdf();
                } else {
                    thumbnail = Resources.getImg_otherfile();
                }
            }
            
            ScaleImage.Rectangle rect = ScaleImage.fitToRect(DoczView.previewWIDTH, DoczView.previewHEIGHT, (BufferedImage)thumbnail);
            thumbnail = ScaleImage.scale((BufferedImage)thumbnail, rect.width, rect.heigth);
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

    public void setTags(List<String> tags) {
        this.tags = tags;
        isModified = true;
    }

    public void save(Document DB) {

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

        isModified = false;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof Doc))
            return false;
        
        return ((Doc)obj).getID() == id;
    }
    
    
    
    
}
