/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    private String dbFile = "db.xml";
    private String dbDir = "db";
    public Document DB;
    private Element root, docz, relationz, institutionz;
    private Map<Long, Doc> docs = new HashMap<>();
    private Map<Long, Institution> institutions = new HashMap<>();
    private List<Relation> relations = new ArrayList<Relation>();

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
            File databaseFile = new File(dbDir + "/" + dbFile);
            new File(dbDir).mkdirs();

            if (databaseFile.createNewFile()) {
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

                Log.l("first start... new DB file created.");
            } else {
                DB = builder.parse(dbDir + "/" + dbFile);
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

    public void reloadData() {
        NodeList docList = docz.getChildNodes();
        NodeList institutionList = institutionz.getChildNodes();
        NodeList relationList = relationz.getChildNodes();

        for (int i = 0; i < docList.getLength(); i++) {
            if (docList.item(i).getNodeName().equalsIgnoreCase("doc")) {
                Doc doc = new Doc((Element) docList.item(i));
                docs.put(doc.getID(), doc);
            }
        }

        for (int i = 0; i < institutionList.getLength(); i++) {
            if (institutionList.item(i).getNodeName().equalsIgnoreCase("institution")) {
                Institution inst = new Institution((Element) institutionList.item(i));
                institutions.put(inst.getID(), inst);
            }
        }

        for (int i = 0; i < relationList.getLength(); i++) {
            if (relationList.item(i).getNodeName().equalsIgnoreCase("relation")) {
                Relation relation = new Relation((Element) relationList.item(i));
                relations.add(relation);
            }
        }
    }

    public String getDBDirectory() {
        return dbDir;
    }

    public synchronized void addDoc(Doc doc) {
        docz.appendChild(doc.getNode());
    }

    public synchronized void addRelation(Relation relation) {
        relationz.appendChild(relation.getNode());
    }

    public synchronized void addInstitution(Institution institution) {
        institutionz.appendChild(institution.getNode());
    }

    public synchronized void removeDoc(Doc doc) {
        docz.removeChild(doc.getNode());
    }

    public synchronized void removeRelation(Relation relation) {
        relationz.removeChild(relation.getNode());
    }

    public synchronized void removeInstitution(Institution institution) {
        institutionz.removeChild(institution.getNode());
    }

    public synchronized void save() {
        try {
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(DB);
            StreamResult streamResult = new StreamResult(new File(
                    dbDir + "/" + dbFile));

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

    public synchronized void updateLastID(long id) {
        if (lastID < id) {
            lastID = id;
        }
    }

    public Entity[] search(String[] searchWords, boolean docsAllowed, boolean institutionsAllowed, boolean relationsAllowed, boolean tagsAllowed) {
        List<Entity> searchResults = new LinkedList<>();

        if (docsAllowed) {
            for (Doc d : docs.values()) {
                for (String s : searchWords) {
                    if (d.getTitle().toLowerCase().contains(s.toLowerCase())
                            || d.getDescription().toLowerCase().contains(s.toLowerCase())
                            || (tagsAllowed && d.getTags().contains(s.toLowerCase()))) {
                        searchResults.add(d);
                        break;
                    }
                }
            }
        }

        if (relationsAllowed) {
            for (Relation r : relations) {
                for (String s : searchWords) {
                    if (r.getTitle().toLowerCase().contains(s.toLowerCase())
                            || r.getDescription().toLowerCase().contains(s.toLowerCase())) {
                        searchResults.add(r);
                        break;
                    }
                }
            }
        }
        
        if (institutionsAllowed) {
            for (Institution i : institutions.values()) {
                for (String s : searchWords) {
                    if (i.getTitle().toLowerCase().contains(s.toLowerCase())
                            || i.getDescription().toLowerCase().contains(s.toLowerCase())
                            || (tagsAllowed && i.getTags().contains(s.toLowerCase()))) {
                        searchResults.add(i);
                        break;
                    }
                }
            }
        }

        return searchResults.toArray(new Entity[searchResults.size()]);
    }

    @Override
    public String toString() {
        return dbFile;
    }

}
