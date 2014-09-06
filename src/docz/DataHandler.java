/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 *
 * @author Michael
 */
public class DataHandler {

    public static final DataHandler instance;
    private static DocumentBuilder builder = null;
    private static long lastID = 0;

    private String dbPath;
    private Document DB;
    private Element root, docz, relationz, institutionz;
    private List<Doc> docs = new ArrayList<Doc>();
    private List<Relation> relation = new ArrayList<Relation>();

    static {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Log.l(ex);
        }
        instance = new DataHandler();
    }

    private DataHandler() {
        try {
            File dbFile = new File(dbPath);

            if (dbFile.createNewFile()) {
                DB = builder.newDocument();

                //create root node
                root = DB.createElement("database");
                DB.appendChild(root);

                //DocZ node
                docz = DB.createElement("docz");
                root.appendChild(docz);

                //Relations node
                relationz = DB.createElement("relationz");
                root.appendChild(relationz);
                
                //Relations node
                institutionz = DB.createElement("institutionz");
                root.appendChild(institutionz);

//                Element tmp = DB.createElement("");
//                tmp.setTextContent("");
//                root.appendChild(tmp);
                Log.l("first start... new DB file created.");
            } else {
                DB = builder.parse(dbFile);
                root = DB.getDocumentElement();

                NodeList nl = root.getElementsByTagName("docz");
                if (nl.getLength() > 0) {
                    docz = (Element) nl.item(0);
                }

                nl = root.getElementsByTagName("relationz");
                if (nl.getLength() > 0) {
                    relationz = (Element) nl.item(0);
                }
                
                nl = root.getElementsByTagName("institutionz");
                if (nl.getLength() > 0) {
                    institutionz = (Element) nl.item(0);
                }

                Log.l("DB file loaded.");
            }

        } catch (Exception ex) {
            Log.l(ex);
        }
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    public String getDbPath() {
        return dbPath;
    }
    
    public synchronized void addDoc(Doc doc)
    {
        docz.appendChild(doc.getNode());
    }
    
    public synchronized void addRelation(Relation relation)
    {
        relationz.appendChild(relation.getNode());
    }
    
    public synchronized void addInstitution(Institution institution)
    {
        institutionz.appendChild(institution.getNode());
    }
    
    public synchronized void removeDoc(Doc doc)
    {
        docz.removeChild(doc.getNode());
    }
    
    public synchronized void removeRelation(Relation relation)
    {
        relationz.removeChild(relation.getNode());
    }
    
    public synchronized void removeInstitution(Institution institution)
    {
        institutionz.removeChild(institution.getNode());
    }
    
    public synchronized void save(){
        try {
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(DB);
            StreamResult streamResult = new StreamResult(new File(
                    dbPath));
            
            transformer.transform(domSource, streamResult);
        } catch (TransformerConfigurationException ex) {
            Log.l(ex);
        } catch (TransformerException ex) {
            Log.l(ex);
        }
    }

    public synchronized long getNewID() {
        return ++lastID;
    }
    
    public synchronized void updateLastID(long id){
        if(lastID < id){
            lastID = id;
        }
    }

}
