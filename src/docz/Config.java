/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Michael
 */
public final class Config {

    private Config() {
    }
    private final static String configPath;
    private final static File configFile;
    private static Document document;
    private static Element doc, databases;

    static {
        configPath = System.getProperty("user.home") + File.pathSeparator + ".docz" + File.pathSeparator + "docz_config.xml";
        configFile = new File(configPath);
        try {
            loadConfig();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Log.l(ex);
        }
    }

    public static void loadConfig() throws ParserConfigurationException, SAXException, IOException {
        if (configFile.exists() && configFile.isFile()) {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = db.parse(configFile);
            doc = document.getDocumentElement();
            NodeList nl = doc.getElementsByTagName("databases");
            if(nl.getLength() > 0){
                databases = (Element)nl.item(0);
            }
        } else if (configFile.exists()) { //config file is not a file, probably a directory
            configFile.delete();
            writeDefaultConfigFile();
        } else { //file does not exist
            configFile.getParentFile().mkdirs();
            writeDefaultConfigFile();
        }
    }

    private static void writeDefaultConfigFile() throws ParserConfigurationException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = db.newDocument();

        doc = document.createElement("config");
        document.appendChild(doc);

        databases = document.createElement("databases");
        doc.appendChild(databases);

        save();
    }

    public static boolean save() {
        if (document != null) {
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(document);
                StreamResult streamResult = new StreamResult(configFile);
                transformer.transform(domSource, streamResult);

                return true;
            } catch (TransformerConfigurationException ex) {
                return false;
            } catch (TransformerException ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean addDatabase(String path) {
        path = path.trim();
        boolean alreadyAdded = false;
        String[] dbs = getDatabases();
        for(int i=0; i<dbs.length && !alreadyAdded; i++){
            if(path.equals(dbs[i])){
                alreadyAdded = true;
            }
        }
        
        if(!alreadyAdded)
        {
            Element db = document.createElement("database");
            db.setTextContent(path);
            databases.appendChild(db);
            
            save();
            
            return true;
        }
        else{
            return false;
        }
    }
    
    public static boolean deleteDatabase(String path) {
        path = path.trim();
        File dbFile = new File(path);
        
        boolean contains = false;
        String[] dbs = getDatabases();
        for(int i=0; i<dbs.length && !contains; i++){
            if(path.equals(dbs[i])){
                contains = true;
            }
        }
        
        if(contains)
        {
            NodeList nl = databases.getElementsByTagName("database");
            for (int i=0; i<nl.getLength(); i++) {
                Node ele = nl.item(i);
                if(ele.getTextContent().equals(path)){
                    databases.removeChild(ele);
                }
            }
            save();
            
            return dbFile.delete();
        }
        else{
            return false;
        }
    }

    public static String[] getDatabases() {
        NodeList nl = databases.getElementsByTagName("database");
        List<String> dbs = new ArrayList<>();
        for (int i=0; i<nl.getLength(); i++) {
            Node ele = nl.item(i);
            dbs.add(ele.getTextContent());
        }

        return dbs.toArray(new String[dbs.size()]);
    }
    
    
}
