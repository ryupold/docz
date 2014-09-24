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

    static {

        instance = new DataHandler();
    }

    private DataHandler() {
        init();
    }

    public void init() {
        File databaseFile = new File(DB.getDBPath());

        try {
            if (testConnection()) {
                createTables();
                Log.l("connection to database established successfully.");
            } else {
                Log.l("no DB file could be created, password is needed!");
            }
        } catch (SQLException ex) {
            Log.l(ex);
        }
    }

    public boolean testConnection() throws SQLException {
        try {
            DB.createConnection().close();
            return true;
        } catch (SQLException ex) {
            throw ex;
        } catch (SecurityException se) {
            return false;
        }
    }

    public void updateEntity(Entity entityWithNewValues) {
        try {
            if (DB.update("update entities set "
                    + "title='" + entityWithNewValues.title + "', "
                    + "description='" + entityWithNewValues.description + "', "
                    + "date='" + entityWithNewValues.date.getTime() + "' "
                    + "where id='" + entityWithNewValues.id + "';"
            ) > 0) {
                DB.update("delete from tags where id='" + entityWithNewValues.id + "'");
                for (String t : entityWithNewValues.tags) {
                    DB.insert("insert into tags(id, tag) values('" + entityWithNewValues.id + "', '" + t + "');", false);
                }
            }
        } catch (SQLException ex) {
            Log.l(ex);
        }
    }

    private void createTables() {
        try {

            Log.l("first start...");

            DB.insert("CREATE TABLE IF NOT EXISTS entities"
                    + "("
                    + "id           IDENTITY   AUTO_INCREMENT   PRIMARY KEY,"
                    + "title        VARCHAR       not null,"
                    + "description  VARCHAR,"
                    + "date         BIGINT,"
                    + "created      BIGINT     not null,"
                    + "type         INT        not null" //1 = Doc, 2=Institution
                    + ");", false);

            DB.insert("create table IF NOT EXISTS tags"
                    + "("
                    + "id         BIGINT,"
                    + "tag        VARCHAR       not null,"
                    + "FOREIGN KEY(id) REFERENCES entities(id) ON DELETE CASCADE"
                    + ");", false);

            DB.insert("create table IF NOT EXISTS relations"
                    + "("
                    + "title           VARCHAR       not null,"
                    + "description     VARCHAR,"
                    + "created         BIGINT        not null,"
                    + "entity1         BIGINT,"
                    + "entity2         BIGINT,"
                    + "FOREIGN KEY(entity2) REFERENCES entities(id) ON DELETE CASCADE,"
                    + "FOREIGN KEY(entity2) REFERENCES entities(id) ON DELETE CASCADE"
                    + ");", false);

            DB.insert("create table IF NOT EXISTS files"
                    + "("
                    + "id              BIGINT,"
                    + "name            VARCHAR,"
                    + "created         BIGINT           not null,"
                    + "file            BLOB             not null,"
                    + "FOREIGN KEY(id) REFERENCES entities(id) ON DELETE CASCADE"
                    + ");", false);

            DB.insert("create table IF NOT EXISTS settings"
                    + "("
                    + "name             VARCHAR     primary key,"
                    + "value            VARCHAR"
                    + ");", false);

            Log.l("... new DB file created.");

        } catch (Exception ex) {
            Log.l("ERROR during table creation");
            Log.l(ex);
        }
    }


    /**
     * currently only working for title, description,
     *
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

        if (docsAllowed | institutionsAllowed) {
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
            while (r.resultSet.next()) {
                long id = r.resultSet.getLong(1);
                String title = r.resultSet.getString(2);
                String description = r.resultSet.getString(3);
                Date date = new Date(r.resultSet.getLong(4));
                Date created = new Date(r.resultSet.getLong(5));
                int type = r.resultSet.getInt(6);
                List<String> tags = new LinkedList<>();

                String tmpSQL = "SELECT tag FROM tags where id='" + id + "'";
                DB.DBResult tagR = DB.select(tmpSQL);

                while (tagR.resultSet.next()) {
                    tags.add(tagR.resultSet.getString(1));
                }
                tagR.close();

                if (type == 1) {
                    resultTmp.add(new Doc(id, title, description, tags, date, created));
                } else if (type == 2) {
                    resultTmp.add(new Institution(id, title, description, tags, created));
                }
            }
            r.close();
        }

        Entity[] results = resultTmp.toArray(new Entity[resultTmp.size()]);
        return results;
    }

    @Override
    public String toString() {
        return DB.getDBPath();
    }

}
