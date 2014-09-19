/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.File;

/**
 *
 * @author Michael
 */
public class DataHandler {

    public static final DataHandler instance;
    private static long lastID = 0;

    static {

        instance = new DataHandler();
    }

    private DataHandler() {
        File databaseFile = new File(DB.dbPath);
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Log.l(ex);
            Log.l("ERROR: no sqlite driver found");
        }
        if (!databaseFile.exists()) {
            createTables();
        } else {
            //TODO
            Log.l("DB file loaded.");
        }
    }

    private void createTables() {
        try {
            
            Log.l("first start...");
            
            DB.insert("create table entities"
                    + "("
                    + "id           integer    primary key AUTOINCREMENT,"
                    + "title        text       not null,"
                    + "description  text,"
                    + "date         bigint,"
                    + "created      bigint     not null,"
                    + "type         integer    not null" //1 = Doc, 2=Institution
                    + ");", false);
            
            DB.insert("create table tags"
                    + "("
                    + "id         integer,"
                    + "tag        char(255)       not null,"
                    + "FOREIGN KEY(id) REFERENCES entities(id)"
                    + ");", false);
            

            DB.insert("create table relations"
                    + "("
                    + "title           text       not null,"
                    + "description     text,"
                    + "created         bigint     not null,"
                    + "entity1         integer,"
                    + "entity2         integer,"
                    + "FOREIGN KEY(entity1) REFERENCES entities(id),"
                    + "FOREIGN KEY(entity2) REFERENCES entities(id)"
                    + ");", false);
            
            DB.insert("create table files"
                    + "("
                    + "id              integer,"
                    + "name            text,"
                    + "created         bigint           not null,"
                    + "file            blob             not null,"
                    + "FOREIGN KEY(id) REFERENCES entities(id)"
                    + ");", false);
            
            DB.insert("create table settings"
                    + "("
                    + "name             varchar(255) primary key,"
                    + "value            text"
                    + ");", false);
                            
            Log.l("... new DB file created.");

        } catch (Exception ex) {
            Log.l("ERROR during table creation");
            Log.l(ex);
        }
    }
    

    public synchronized void addDoc(Doc doc) {
        
    }

    public synchronized void addRelation(Relation relation) {

    }

    public synchronized void addInstitution(Institution institution) {

    }

    public synchronized void removeDoc(Doc doc) {

    }

    public synchronized void removeRelation(Relation relation) {

    }

    public synchronized void removeInstitution(Institution institution) {

    }

    public synchronized void save() {

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
        return null;
    }

}
