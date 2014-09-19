/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

    /**
     * currently only working for title, description, 
     * @param searchWords
     * @param docsAllowed
     * @param institutionsAllowed
     * @param relationsAllowed
     * @param tagsAllowed
     * @return
     * @throws SQLException 
     */
    public Entity[] search(String[] searchWords, boolean docsAllowed, boolean institutionsAllowed, boolean relationsAllowed, boolean tagsAllowed) throws SQLException {
        List<Entity> resultTmp = new LinkedList<>();

        if (!(docsAllowed | institutionsAllowed)) {
            String sql = "SELECT id, title, description, date, created, type FROM entities WHERE ";
            if (searchWords.length > 0) {
                sql += "(";
                sql += "(";
                {
                    sql += " title LIKE '%" + searchWords[0] + "%' ";
                    for (int i = 1; i < searchWords.length; i++) {
                        sql += " OR title LIKE '%" + searchWords[i] + "%' ";
                    }
                }
                sql += ")";
                
                sql += " OR (";
                {
                    sql += " description LIKE '%" + searchWords[0] + "%' ";
                    for (int i = 1; i < searchWords.length; i++) {
                        sql += " OR description LIKE '%" + searchWords[i] + "%' ";
                    }
                }
                sql += ")";
                sql += ")";
            }

            if (docsAllowed ^ institutionsAllowed) {
                sql += (searchWords.length > 0 ? " AND " : "") + (docsAllowed ? "type='1'" : "type='2'");
            }

            DB.DBResult r = DB.select(sql);
            while(r.resultSet.next()){
                long id = r.resultSet.getLong(1);
                String title = r.resultSet.getString(2);
                String description = r.resultSet.getString(3);
                Date date = new Date(r.resultSet.getLong(4));
                Date created = new Date(r.resultSet.getLong(5));
                int type = r.resultSet.getInt(5);
                List<String> tags = new LinkedList<>();
                
                DB.DBResult tagR = DB.select("SELECT tag FROM tags where id='"+id+"'");
                while(tagR.resultSet.next()){
                    tags.add(tagR.resultSet.getString(1));
                }
                tagR.close();
                
                
                if(type == 1){
                    resultTmp.add(new Doc(lastID, title, description, tags, date, created));
                }
                else if(type == 2){
                    resultTmp.add(new Institution(lastID, title, description, tags, created));
                }
            }
            r.close();
        }

        Entity[] results = resultTmp.toArray(new Entity[resultTmp.size()]);
        return results;
    }

}
