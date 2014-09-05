/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
    private Element root, docz, relations;
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
                relations = DB.createElement("relations");
                root.appendChild(relations);

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

                nl = root.getElementsByTagName("relations");
                if (nl.getLength() > 0) {
                    relations = (Element) nl.item(0);
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

    public synchronized long getNewID() {
        return ++lastID;
    }

}
