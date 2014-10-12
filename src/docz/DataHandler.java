/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Michael
 */
public class DataHandler {

    public static final DataHandler instance;
    public static final int DEFAULT_LIMIT = 50;
    public static final String filterChars = ",.;:'\"!@#$%^&*~`<>/*-+/\\?";

    static {

        instance = new DataHandler();
    }

    private DataHandler() {
        
    }

    /**
     * 1 = no DB path specified, 2 = wrong password, 3 = no password, 4 = error
     * @return 
     */
    public int init() {
        if(DB.getDBPath() == null){
            return 1;
        }
        
        File databaseFile = new File(DB.getDBPath());

        try {
            if (testConnection()) {
                createTables();
                Log.l("connection to database established successfully.");
                
                return 3;
            } else {
                Log.l("no DB file could be created, password is needed!");
                return 2;
            }
        } catch (SQLException ex) {
            Log.l(ex);
            return 4;
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
                    + "id              IDENTITY   AUTO_INCREMENT   PRIMARY KEY,"
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
                    + "size            BIGINT           not null,"
                    + "ocr             BLOB,"
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

    public Entity createEntity(String title, String description, List<String> tags, Date date, int type) throws SQLException, FileNotFoundException, IOException {
        Connection c = DB.createConnection();

        Date created = new Date();
        if (date == null) {
            date = created;
        }
        try {
            Long id = DB.insert("insert into entities(title, description, date, created, type) values('" + title + "', '" + description + "', '" + date.getTime() + "', '" + created.getTime() + "', '" + type + "');", true).get(0);

            if (id != null) {
                for (String tag : tags) {
                    DB.insert("insert into tags(id, tag) values('" + id + "', '" + tag + "');", false);
                }

                Entity entity = null;
                if (type == 1) {
                    entity = new Doc(id, title, description, tags, date, created);
                } else if (type == 2) {
                    entity = new Institution(id, title, description, tags, created);
                }

                return entity;
            } else {
                return null;
            }
        } finally {
            c.close();
        }
    }

    public boolean deleteEntity(Entity entity) {
        try {
            boolean deleted = DB.update("DELETE FROM entities WHERE id='" + entity.getID() + "'") > 0;
            DB.update("DELETE FROM relations WHERE entity1='" + entity.getID() + "' OR entity2='" + entity.getID() + "'");
            DB.update("DELETE FROM tags WHERE id='" + entity.getID() + "'");
            DB.update("DELETE FROM files WHERE id='" + entity.getID() + "'");
            return deleted;
        } catch (SQLException ex) {
            Log.l(ex);
            return false;
        }
    }

    public Relation createRelation(String title, String description, Entity entity1, Entity entity2) throws SQLException {
        Connection c = DB.createConnection();
        Date created = new Date();
        try {
            Long id = DB.insert("insert into relations(title, description, created, entity1, entity2) values('" + title + "', '" + description + "', '" + created.getTime() + "', '" + entity1.id + "', '" + entity2.id + "');", true).get(0);

            return new Relation(id, title, description, created, entity1, entity2);
        } finally {
            c.close();
        }
    }

    public boolean deleteRelation(Relation relation) {
        try {
            return DB.update("DELETE FROM relations WHERE id='" + relation.getID() + "'") > 0;
        } catch (SQLException ex) {
            Log.l(ex);
            return false;
        }
    }

    public boolean removeFile(Entity entity, String name) throws SQLException {
        return DB.update("delete from files where name='" + name + "' and id='" + entity.id + "'") > 0;
    }

    public String[] addFiles(Entity entity, File... files) throws SQLException, IOException {

        List<String> fileNames = new ArrayList<>();
        List<String> newFileNames = new ArrayList<>();
        try (DB.DBResult r = DB.select("SELECT name from files where id='" + entity.id + "';")) {
            while (r.resultSet.next()) {
                fileNames.add(r.resultSet.getString(1));
            }
        }

        int fn = 0;
        for (File file : files) {
            fn = 0;
            while (fn == 0 ? fileNames.contains(file.getName()) : fileNames.contains("d" + fn + "_" + file.getName())) {
                fn++;
            }

            if (fn == 0) {
                fileNames.add(file.getName());
                newFileNames.add(file.getName());
            } else {
                fileNames.add("d" + fn + "_" + file.getName());
                newFileNames.add("d" + fn + "_" + file.getName());
            }
        }
        Date created = new Date();
        Connection c = DB.createConnection();
        PreparedStatement ps = c.prepareStatement("insert into files(id, name, created, file, size, ocr) values(?, ?, ?, ?, ?, ?)");
        try {
            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                ByteArrayInputStream ocrStream = new ByteArrayInputStream(DataHandler.instance.getOCR(files[i]).getBytes());
                ps.setLong(1, entity.id);
                ps.setString(2, newFileNames.get(i));
                ps.setLong(3, created.getTime());
                ps.setBinaryStream(4, fi);
                ps.setLong(5, fi.getChannel().size());
                ps.setBinaryStream(6, ocrStream);
                ps.execute();
            }
        } finally {
            ps.close();
            c.close();
        }

        return newFileNames.toArray(new String[fileNames.size()]);
    }

    public Entity getEntityByID(long id) {
        try {
            DB.DBResult rEntity = DB.select("SELECT id, title, description, date, created, type FROM entities WHERE id='" + id + "';");
            if (rEntity.resultSet.next()) {
                DB.DBResult rTags = DB.select("SELECT tag FROM tags WHERE id='" + id + "'");
                Entity e = null;

                List<String> tags = new LinkedList<>();
                while (rTags.resultSet.next()) {
                    tags.add(rTags.resultSet.getString(1));
                }

                if (rEntity.resultSet.getInt(6) == 1) {
                    e = new Doc(rEntity.resultSet.getLong(1), rEntity.resultSet.getString(2), rEntity.resultSet.getString(3), tags, new Date(rEntity.resultSet.getLong(4)), new Date(rEntity.resultSet.getLong(5)));
                } else if (rEntity.resultSet.getInt(6) == 2) {
                    e = new Institution(rEntity.resultSet.getLong(1), rEntity.resultSet.getString(2), rEntity.resultSet.getString(3), tags, new Date(rEntity.resultSet.getLong(5)));
                }

                rEntity.close();
                rTags.close();

                return e;
            }
        } catch (SQLException ex) {
            Log.l(ex);
        }

        return null;
    }

    public Relation[] getRelations(Entity entity) {
        Relation[] relations = null;
        try {
            DB.DBResult r = DB.select("SELECT id, title, description, created, entity1, entity2 FROM relations WHERE entity1='" + entity.getID() + "' OR entity2='" + entity.getID() + "'");
            List<Relation> tmpRelations = new LinkedList<>();
            while (r.resultSet.next()) {
                Entity e1 = r.resultSet.getLong(5) == entity.id ? entity : getEntityByID(r.resultSet.getLong(6));
                Entity e2 = r.resultSet.getLong(5) == entity.id ? getEntityByID(r.resultSet.getLong(6)) : getEntityByID(r.resultSet.getLong(5));
                tmpRelations.add(new Relation(r.resultSet.getLong(1), r.resultSet.getString(2), r.resultSet.getString(3), new Date(r.resultSet.getLong(4)), e1, e2));
            }
            r.close();
            relations = tmpRelations.toArray(new Relation[tmpRelations.size()]);
        } catch (SQLException ex) {
            Log.l(ex);
        }

        return relations;
    }

    public int countFiles(Entity entity) throws IOException {
        int count = 0;
        try {
            DB.DBResult r = DB.select("SELECT COUNT(*) FROM files WHERE id='" + entity.id + "'");

            if (r.resultSet.next()) {
                count = r.resultSet.getInt(1);
            }
            r.close();
        } catch (SQLException ex) {
            Log.l(ex);
        }
        return count;
    }

    public String[] getFileNames(Entity entity) throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created from files where id='" + entity.id + "';");
            List<String> names = new ArrayList<>();
            while (r.resultSet.next()) {
                names.add(r.resultSet.getString(1));

            }
            r.close();
            return names.toArray(new String[names.size()]);
        } catch (SQLException ex) {
            Log.l(ex);
            return null;
        }
    }

    public void writeFileToStream(Entity entity, String name, OutputStream output) throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created, file from files where id='" + entity.id + "' AND name='" + name + "'");
            InputStream byteStream = null;
            if (r.resultSet.next()) {
                byteStream = r.resultSet.getBinaryStream(3);
            }
            r.close();
        } catch (SQLException ex) {
            Log.l(ex);
        }
    }

    /**
     * returns null ich the byte stream dowsnt hold an image
     *
     * @param byteStream
     * @return
     */
    public Image isImage(InputStream byteStream) {
        try {
            BufferedImage img = ImageIO.read(byteStream);
            return img;
        } catch (IOException ex) {
            return null;
        }
    }

    public DB.DBResultWithStream getFileStream(Entity entity, String name) throws SQLException {
        DB.DBResultWithStream r = new DB.DBResultWithStream(
                DB.select("select name, created, file from files where id='" + entity.id + "' AND name='" + name + "'"),
                3);
        return r;
    }

    public ImageFile[] getThumbnails(Entity entity, int preferedWidth, int preferedHeight, Font font) throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created, file from files where id='" + entity.id + "'");
            List<ImageFile> imgs = new LinkedList<>();
            while (r.resultSet.next()) {
                BufferedImage sImg;
                String fname = r.resultSet.getString(1);
                if (fname.endsWith(".jpg") || fname.endsWith(".jpeg") || fname.endsWith(".bmp") || fname.endsWith(".wbmp") || fname.endsWith(".gif") || fname.endsWith(".png")) {
                    try {
                        InputStream byteStream = r.resultSet.getBinaryStream(3);
                        sImg = ImageIO.read(byteStream);
                        ScaleImage.Rectangle rec = ScaleImage.fitToRect(preferedWidth, preferedHeight, sImg);
                        sImg = ScaleImage.scale(sImg, rec.width, rec.heigth);
                    } catch (IllegalArgumentException iae) {
                        sImg = Resources.createImageWithText(fname, preferedWidth, preferedHeight / 2, font);
                    }
                } else {
                    sImg = Resources.createImageWithText(fname, preferedWidth, preferedHeight / 2, font);
                }
                imgs.add(new ImageFile(entity, fname, new Date(r.resultSet.getLong(2)), sImg));

            }
            r.close();
            return imgs.toArray(new ImageFile[imgs.size()]);
        } catch (SQLException ex) {
            Log.l(ex);
            return null;
        }
    }

    /**
     * returns a representive thumbnail of the Doc by taking one of the attached image files. if there is no image linked to this document,
     * Resources.img_other is returned.
     *
     * @param preferedWidth
     * @param preferedHeight
     * @return
     * @throws SQLException
     */
    public Image getThumbnail(Entity entity, int preferedWidth, int preferedHeight, Font font) throws SQLException {
        DB.DBResult r = DB.select("select name, file from files where id='" + entity.id + "' and ("
                + "name like '%.png' or "
                + "name like '%.jpg' or "
                + "name like '%.jpeg' or "
                + "name like '%.bmp' or "
                + "name like '%.wbmp' or "
                + "name like '%.gif'"
                + ") limit 1;");

        try {

            if (r.resultSet.next()) {
                InputStream byteStream = r.resultSet.getBinaryStream(2);
                try {
                    BufferedImage img = ImageIO.read(byteStream);
                    ScaleImage.Rectangle newSize = ScaleImage.fitToRect(preferedWidth, preferedHeight, img);
                    img = ScaleImage.scale(img, newSize.width, newSize.heigth);
                    return img;
                } catch (IOException ex) {
                    return Resources.createImageWithText(entity.title, preferedWidth, preferedHeight, font);
                }
            } else {
                r.close();
                r = DB.select("select name from files where id='" + entity.id + "' limit 1;");
                if (r.resultSet.next()) {
                    return Resources.createImageWithText(entity.title, preferedWidth, preferedHeight, font);
                } else {
                    return Resources.getImg_nofiles();
                }
            }
        } finally {
            r.close();
        }
    }

    public Image getThumbnail(Entity entity, String name, int preferedWidth, int preferedHeight, Font font) throws SQLException {
        try (DB.DBResult r = DB.select("select name, file from files where id='" + entity.id + "' and name='" + name + "'")) {

            if (r.resultSet.next()) {
                String fileName = r.resultSet.getString(1);
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".bmp") || fileName.endsWith(".wbmp") || fileName.endsWith(".gif") || fileName.endsWith(".png")) {
                    try {
                        InputStream byteStream = r.resultSet.getBinaryStream(2);
                        BufferedImage img = ImageIO.read(byteStream);
                        ScaleImage.Rectangle newSize = ScaleImage.fitToRect(preferedWidth, preferedHeight, img);
                        img = ScaleImage.scale(img, newSize.width, newSize.heigth);
                        return img;
                    } catch (IOException ex) {
                        return Resources.createImageWithText(name, preferedWidth, preferedHeight, font);
                    }
                } else {
                    return Resources.createImageWithText(name, preferedWidth, preferedHeight, font);
                }
            } else {
                return Resources.getImg_nofiles();
            }
        }
    }

    public Image getFileAsImage(Entity entity, String name) throws SQLException {
        try (DB.DBResult r = DB.select("select name, file from files where id='" + entity.id + "' and name='" + name + "'")) {

            if (r.resultSet.next()) {
                String fileName = r.resultSet.getString(1);
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".bmp") || fileName.endsWith(".wbmp") || fileName.endsWith(".gif") || fileName.endsWith(".png")) {
                    try {
                        InputStream byteStream = r.resultSet.getBinaryStream(2);
                        BufferedImage img = ImageIO.read(byteStream);
                        return img;
                    } catch (IOException ex) {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return Resources.getImg_nofiles();
            }
        }
    }

    public static enum Sorting {

        Title,
        Date,
        Created
    }

    public static enum SortingOrder {

        Ascending,
        Descending
    }

    public static class EntityComparator implements Comparator<Entity> {

        public final Sorting sorting;
        public final SortingOrder order;

        public EntityComparator(Sorting sorting, SortingOrder order) {
            this.sorting = sorting;
            this.order = order;
        }

        @Override
        public int compare(Entity o1, Entity o2) {
            switch (sorting) {
                case Created:
                    if (o1 == null || o2 == null) {
                        return 0;
                    }
                    if (order == SortingOrder.Ascending) {
                        return o1.created.compareTo(o2.created);
                    } else {
                        return o2.created.compareTo(o1.created);
                    }
                case Date:
                    if (o1 == null || o2 == null) {
                        return 0;
                    }

                    if (order == SortingOrder.Ascending) {
                        return (o1.date != null ? o1.date : o1.created).compareTo((o2.date != null ? o2.date : o2.created));
                    } else {
                        return (o2.date != null ? o2.date : o2.created).compareTo((o1.date != null ? o1.date : o1.created));
                    }
                case Title:
                    if (order == SortingOrder.Ascending) {
                        return o1.title.compareTo(o2.title);
                    } else {
                        return o2.title.compareTo(o1.title);
                    }
                default:
                    return 0;
            }
        }

    }

    public Entity[] search(String[] searchWords, boolean docsAllowed, boolean institutionsAllowed, boolean relationsAllowed, boolean tagsAllowed) throws SQLException {
        return search(searchWords, docsAllowed, institutionsAllowed, relationsAllowed, tagsAllowed, null, null);
    }

    public Entity[] search(String[] searchWords, boolean docsAllowed, boolean institutionsAllowed, boolean relationsAllowed, boolean tagsAllowed, Date minDate, Date maxDate) throws SQLException {
        return search(searchWords, docsAllowed, institutionsAllowed, relationsAllowed, tagsAllowed, minDate, maxDate, true);
    }

    public Entity[] search(String[] searchWords, boolean docsAllowed, boolean institutionsAllowed, boolean relationsAllowed, boolean tagsAllowed, Date minDate, Date maxDate, boolean filesAllowd) throws SQLException {
        return search(searchWords, docsAllowed, institutionsAllowed, relationsAllowed, tagsAllowed, minDate, maxDate, filesAllowd, DEFAULT_LIMIT);
    }

    public Entity[] search(String[] searchWords, boolean docsAllowed, boolean institutionsAllowed, boolean relationsAllowed, boolean tagsAllowed, Date minDate, Date maxDate, boolean filesAllowed, int limit) throws SQLException {
        return search(searchWords, docsAllowed, institutionsAllowed, relationsAllowed, tagsAllowed, minDate, maxDate, filesAllowed, limit, Sorting.Date, SortingOrder.Descending);
    }

    public Entity[] search(String[] searchWords, boolean docsAllowed, boolean institutionsAllowed, boolean relationsAllowed, boolean tagsAllowed, Date minDate, Date maxDate, boolean filesAllowed, int limit, Sorting sorting, SortingOrder order) throws SQLException {
        List<Entity> resultTmp = new LinkedList<>();

        for (int i = 0; i < searchWords.length; i++) {
            for (int j = 0; j < filterChars.length(); j++) {
                searchWords[i] = searchWords[i].replace(filterChars.charAt(j) + "", "");
            }
        }

        //search by title/description
        if (docsAllowed | institutionsAllowed) {
            String sql = "SELECT id, title, description, date, created, type FROM entities WHERE ";
            if (searchWords.length > 0) {
                sql += "(";
                {
                    sql += " LOWER(title) LIKE LOWER('%" + searchWords[0] + "%') ";
                    for (int i = 1; i < searchWords.length; i++) {
                        sql += " OR title LIKE '%" + searchWords[i] + "%' ";
                    }
                }

                sql += " OR";
                {
                    sql += " LOWER(description) LIKE LOWER('%" + searchWords[0] + "%') ";
                    for (int i = 1; i < searchWords.length; i++) {
                        sql += " OR LOWER(description) LIKE LOWER('%" + searchWords[i] + "%') ";
                    }
                }

                sql += ")";
            }

            if (docsAllowed ^ institutionsAllowed) {
                sql += (searchWords.length > 0 ? " AND " : "") + (docsAllowed ? "type='1'" : "type='2'");
            }

            //search by date
            if (minDate != null || maxDate != null) {
                if (minDate != null && maxDate == null) {
                    sql += " AND date >= '" + minDate.getTime() + "'";
                } else if (minDate == null && maxDate != null) {
                    sql += " AND date <= '" + maxDate.getTime() + "'";
                } else {
                    sql += " AND date >= '" + minDate.getTime() + "' AND date <= '" + maxDate.getTime() + "'";
                }
            }

            //odering
            switch (sorting) {
                case Created:
                    sql += " ORDER BY created " + (order == SortingOrder.Descending ? "DESC" : "");
                    break;
                case Date:
                    sql += " ORDER BY date " + (order == SortingOrder.Descending ? "DESC" : "");
                    break;
                case Title:
                    sql += " ORDER BY title " + (order == SortingOrder.Descending ? "DESC" : "");
                    break;
                default:
                    break;
            }

            //limit
            if (limit > 0) {
                sql += " LIMIT " + limit;
            }

            //Log.l(sql);
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

                Entity resultEntity = null;

                if (type == 1) {
                    resultEntity = new Doc(id, title, description, tags, date, created);
                } else if (type == 2) {
                    resultEntity = new Institution(id, title, description, tags, created);
                }

                if (!resultTmp.contains(resultEntity)) {
                    resultTmp.add(resultEntity);
                }
            }
            r.close();
        }

        //search by tags
        if (resultTmp.size() < limit && tagsAllowed && searchWords.length > 0) {
            String sql = "SELECT id FROM tags where ";
            for (int i = 0; i < searchWords.length; i++) {
                if (i == 0) {
                    sql += "LOWER(tag) LIKE '%" + searchWords[i].toLowerCase() + "%' ";
                } else {
                    sql += "OR LOWER(tag) LIKE '%" + searchWords[i].toLowerCase() + "%' ";
                }
            }

            DB.DBResult r = DB.select(sql);
            List<Long> tagIDs = new ArrayList<>();
            while (r.resultSet.next()) {
                if (!tagIDs.contains(r.resultSet.getLong(1))) {
                    tagIDs.add(r.resultSet.getLong(1));
                }
            }
            for (Long id : tagIDs) {
                Entity resultEntity = getEntityByID(id);
                if (!resultTmp.contains(resultEntity)) {
                    resultTmp.add(resultEntity);
                }
            }
            r.close();
        }

        //search by files
        if (resultTmp.size() < limit && filesAllowed && searchWords.length > 0) {
            String sql = "SELECT id, name, ocr FROM files where ";
            for (int i = 0; i < searchWords.length; i++) {
                if (i == 0) {
                    sql += "LOWER(name) LIKE '%" + searchWords[i].toLowerCase() + "%' ";
                } else {
                    sql += "OR LOWER(name) LIKE '%" + searchWords[i].toLowerCase() + "%' ";
                }
            }

            DB.DBResult r = DB.select(sql);
            List<Long> fileIDs = new ArrayList<>();
            while (r.resultSet.next()) {
                if (!fileIDs.contains(r.resultSet.getLong(1))) {
                    fileIDs.add(r.resultSet.getLong(1));
                }
            }
            for (Long id : fileIDs) {
                Entity resultEntity = getEntityByID(id);
                if (!resultTmp.contains(resultEntity)) {
                    resultTmp.add(resultEntity);
                }
            }
            r.close();
        }

        //search by relation title&description
        if (resultTmp.size() < limit && relationsAllowed && searchWords.length > 0) {
            String sql = "SELECT entity1, entity2 FROM relations where ";
            for (int i = 0; i < searchWords.length; i++) {
                if (i == 0) {
                    sql += "LOWER(title) LIKE '%" + searchWords[i].toLowerCase() + "%' ";
                } else {
                    sql += "OR LOWER(title) LIKE '%" + searchWords[i].toLowerCase() + "%' ";
                }
            }

            for (int i = 0; i < searchWords.length; i++) {
                sql += "OR LOWER(description) LIKE '%" + searchWords[i].toLowerCase() + "%' ";
            }

            DB.DBResult r = DB.select(sql);
            List<Long> relationIDs = new ArrayList<>();
            while (r.resultSet.next()) {
                if (!relationIDs.contains(r.resultSet.getLong(1))) {
                    relationIDs.add(r.resultSet.getLong(1));
                }

                if (!relationIDs.contains(r.resultSet.getLong(2))) {
                    relationIDs.add(r.resultSet.getLong(2));
                }
            }

            for (Long id : relationIDs) {
                Entity resultEntity = getEntityByID(id);
                if (!resultTmp.contains(resultEntity)) {
                    resultTmp.add(resultEntity);
                }
            }
            r.close();
        }

        //filter by date
        for (int i = 0; i < resultTmp.size(); i++) {
            if (minDate != null) {
                if (resultTmp.get(i).date != null) {
                    if (resultTmp.get(i).date.compareTo(minDate) < 0) {
                        resultTmp.remove(i);
                        i--;
                    }
                } else {
                    if (resultTmp.get(i).created.compareTo(minDate) < 0) {
                        resultTmp.remove(i);
                        i--;
                    }
                }
            }

            if (maxDate != null) {
                if (resultTmp.get(i).date != null) {
                    if (resultTmp.get(i).date.compareTo(maxDate) > 0) {
                        resultTmp.remove(i);
                        i--;
                    }
                } else {
                    if (resultTmp.get(i).created.compareTo(maxDate) > 0) {
                        resultTmp.remove(i);
                        i--;
                    }
                }
            }
        }

        Collections.sort(resultTmp, new EntityComparator(sorting, order));
        Entity[] results = resultTmp.toArray(new Entity[resultTmp.size()]);
        return results.length > limit ? Arrays.copyOf(results, limit) : results;
    }

    @Override
    public String toString() {
        return DB.getDBPath();
    }

    /**
     * deactivated until I find a way to load a pretrained OCR library
     *
     * @param file
     * @return
     */
    public String getOCR(File file) {
//        OCRScanner scanner = new OCRScanner();
//        String filename = file.getAbsolutePath().toLowerCase();
//        if ((filename.endsWith(".jpg")
//                || filename.endsWith(".jpeg")
//                || filename.endsWith(".png")
//                || filename.endsWith(".bmp")
//                || filename.endsWith(".wbmp")
//                || filename.endsWith(".gif"))) {
//            try {
//                Image img = ImageIO.read(file);
//                String ocr = scanner.scan(img, 0, 0, 0, 0, null);
//                if(ocr != null) ocr = ocr.trim();
//                Log.l("OCR: "+ocr);
//                return ocr;
//                
//            } catch (IOException ex) {
//                Log.l(ex);
//            }
//        }
//        
//        Log.l("OCR: nothing");
        return "";
    }

}
