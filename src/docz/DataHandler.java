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
import java.io.FileOutputStream;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
                if (fname.toLowerCase().endsWith(".jpg") || fname.toLowerCase().endsWith(".jpeg") || fname.toLowerCase().endsWith(".bmp") || fname.toLowerCase().endsWith(".wbmp") || fname.toLowerCase().endsWith(".gif") || fname.toLowerCase().endsWith(".png")) {
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
                + "LOWER(name) like '%.png' or "
                + "LOWER(name) like '%.jpg' or "
                + "LOWER(name) like '%.jpeg' or "
                + "LOWER(name) like '%.bmp' or "
                + "LOWER(name) like '%.wbmp' or "
                + "LOWER(name) like '%.gif'"
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
                if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg") || fileName.toLowerCase().endsWith(".bmp") || fileName.toLowerCase().endsWith(".wbmp") || fileName.toLowerCase().endsWith(".gif") || fileName.toLowerCase().endsWith(".png")) {
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
            searchWords[i] = searchWords[i].trim();
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
    
    public WaitDialog.AsyncProcess createExportProcess(final File exportDir){
        return new WaitDialog.AsyncProcess("exporting database...") {

                @Override
                public void finished(boolean success) {

                }

                @Override
                public void start() throws Exception {

                    try {
                        this.processing(0.0, "initializing destination");
                        if (isCanceled()) {
                            Log.l("exporting aborted");
                            return;
                        }

                        //export dir
                        File dbFile = new File(exportDir.getPath() + File.separator + "db.xml");
                        File fileDir = new File(exportDir.getPath() + File.separator + "files");
                        fileDir.mkdirs();

                        if (isCanceled()) {
                            Log.l("exporting aborted");
                            return;
                        }
                        //entity xml doc
                        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        Document document = db.newDocument();
                        Element root = document.createElement("docz");
                        document.appendChild(root);

                        if (isCanceled()) {
                            Log.l("exporting aborted");
                            return;
                        }
                        this.processing(0.01, "exporting entity data");
                        Element entities = document.createElement("entities");
                        root.appendChild(entities);

                        DB.DBResult r = DB.select("SELECT id, title, description, date, created, type FROM entities ORDER BY id");
                        try {
                            while (r.resultSet.next()) {
                                Element entity = document.createElement("entity");
                                Element id = document.createElement("id");
                                id.setTextContent(r.resultSet.getString(1));
                                entity.appendChild(id);
                                Element title = document.createElement("title");
                                title.setTextContent(r.resultSet.getString(2));
                                entity.appendChild(title);
                                Element description = document.createElement("description");
                                description.setTextContent(r.resultSet.getString(3));
                                entity.appendChild(description);
                                Element date = document.createElement("date");
                                date.setTextContent(r.resultSet.getString(4));
                                entity.appendChild(date);
                                Element created = document.createElement("created");
                                created.setTextContent(r.resultSet.getString(5));
                                entity.appendChild(created);
                                Element type = document.createElement("type");
                                type.setTextContent(r.resultSet.getString(6));
                                entity.appendChild(type);
                                entities.appendChild(entity);
                            }

                            r.close();

                            if (isCanceled()) {
                                Log.l("exporting aborted");
                                return;
                            }
                            this.processing(0.2, "exporting tags...");
                            //tags xml doc
                            Element tags = document.createElement("tags");
                            root.appendChild(tags);

                            r = DB.select("SELECT id, tag FROM tags ORDER BY id");
                            while (r.resultSet.next()) {
                                if (isCanceled()) {
                                    Log.l("exporting aborted");
                                    return;
                                }
                                Element tag = document.createElement("tag");
                                Element id = document.createElement("id");
                                id.setTextContent(r.resultSet.getString(1));
                                tag.appendChild(id);
                                Element tagname = document.createElement("tag");
                                tagname.setTextContent(r.resultSet.getString(2));
                                tag.appendChild(tagname);
                                tags.appendChild(tag);
                            }
                            r.close();

                            if(isCanceled()) {Log.l("exporting aborted"); return;}
                            this.processing(0.3, "exporting relations...");
                            //relation xml doc
                            Element relations = document.createElement("relations");
                            root.appendChild(relations);

                            r = DB.select("SELECT id, title, description, created, entity1, entity2 FROM relations ORDER BY id");
                            while (r.resultSet.next()) {
                                Element relation = document.createElement("relation");
                                Element id = document.createElement("id");
                                id.setTextContent(r.resultSet.getString(1));
                                relation.appendChild(id);
                                Element title = document.createElement("title");
                                title.setTextContent(r.resultSet.getString(2));
                                relation.appendChild(title);
                                Element description = document.createElement("description");
                                description.setTextContent(r.resultSet.getString(3));
                                relation.appendChild(description);
                                Element created = document.createElement("created");
                                created.setTextContent(r.resultSet.getString(4));
                                relation.appendChild(created);
                                Element entity1 = document.createElement("entity1");
                                entity1.setTextContent(r.resultSet.getString(5));
                                relation.appendChild(entity1);
                                Element entity2 = document.createElement("entity2");
                                entity2.setTextContent(r.resultSet.getString(6));
                                relation.appendChild(entity2);
                                relations.appendChild(relation);
                            }
                            r.close();

                            if(isCanceled()) {Log.l("exporting aborted"); return;}
                            this.processing(0.5, "exporting files...");
                            //files xml doc
                            Element files = document.createElement("files");
                            root.appendChild(files);

                            r = DB.select("SELECT count(*) FROM files");
                            r.resultSet.next();
                            long fileCount = r.resultSet.getLong(1);
                            r.close();

                            double i = 0;
                            r = DB.select("SELECT id, name, created, file, size, ocr FROM files ORDER BY id");
                            while (r.resultSet.next()) {
                                String fname = r.resultSet.getString(2);
                                if(isCanceled()) {Log.l("exporting aborted"); return;}
                                this.processing(i / fileCount * 0.5 + 0.5, "exporting file " + fname);
                                Element file = document.createElement("file");
                                Element id = document.createElement("id");
                                id.setTextContent(r.resultSet.getString(1));
                                file.appendChild(id);
                                Element name = document.createElement("name");
                                name.setTextContent(fname);
                                file.appendChild(name);
                                Element created = document.createElement("created");
                                created.setTextContent(r.resultSet.getString(3));
                                file.appendChild(created);
                                Element size = document.createElement("size");
                                size.setTextContent(r.resultSet.getString(5));
                                file.appendChild(size);
                                Element ocr = document.createElement("ocr");
                                ocr.setTextContent(r.resultSet.getString(6));
                                file.appendChild(ocr);
                                files.appendChild(file);

                                Entity e = DataHandler.instance.getEntityByID(r.resultSet.getLong(1));
                                new File(fileDir.getAbsolutePath() + File.separator + "id_" + e.id).mkdirs();
                                File f = new File(fileDir.getAbsolutePath() + File.separator + "id_" + e.id + File.separator + fname);

                                InputStream byteStream = null;
                                FileOutputStream fos = new FileOutputStream(f);
                                byteStream = r.resultSet.getBinaryStream(4);

                                long bytesRead = 0;
                                long fileSize = r.resultSet.getLong(5);
                                byte[] buffer = new byte[1024];
                                int tmpCount = 0;
                                while ((tmpCount = byteStream.read(buffer)) > 0) {
                                    bytesRead += tmpCount;
                                    fos.write(buffer);
                                    double percent = (double) bytesRead / (double) fileSize;
                                    if(isCanceled()) {Log.l("exporting aborted"); return;}
                                    processing(((double) bytesRead / (double) fileSize) * 1.0 / fileCount + i / fileCount * 0.5 + 0.5, "exporting file " + fname);
                                }
                                byteStream.close();
                                fos.close();

                                i += 1;
                            }
                        } finally {
                            r.close();
                        }
                        
                        if(isCanceled()) {Log.l("exporting aborted"); return;}
                        this.processing(0.99, "saving db file...");
                        saveXMLDocument(document, dbFile);
                        if(isCanceled()) {Log.l("exporting aborted"); return;}
                        this.processing(1.0, "finished!");

                    } catch (Exception ex) {
                        Log.l(ex);
                    }
                }
            };
    }
    
    public WaitDialog.AsyncProcess createImportProcess(final File importDir){
        return new WaitDialog.AsyncProcess("importing database...") {
                @Override
                public void finished(boolean success) {

                }

                @Override
                public void start() throws Exception {
                    try {
                        this.processing(0.0, "loading id range...");
                        long nextID_entity = 0;
                        long nextID_relation = 0;
                        
                        DB.DBResult r = DB.select("SELECT max(id) FROM entities");
                        if(r.resultSet.next()){
                            nextID_entity = r.resultSet.getLong(1) + 1000;
                        }
                        r.close();
                        
                        r = DB.select("SELECT max(id) FROM relations");
                        if(r.resultSet.next()){
                            nextID_relation = r.resultSet.getLong(1) + 1000;
                        }
                        r.close();
                        
                        Map<Long, Long> oldAndNewEntityIDLookupTable = new HashMap<>();
                        
                        
                        this.processing(0.0, "loading database file...");
                        //load document
                        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        Document document = db.parse(importDir+File.separator+"db.xml");
                        Element root = document.getDocumentElement();
                        XPathFactory xPathfactory = XPathFactory.newInstance();
                        XPath xpath = xPathfactory.newXPath();
                        XPathExpression xAllEntities = xpath.compile("/docz/entities/entity");
                        NodeList entityNodes = (NodeList)xAllEntities.evaluate(document, XPathConstants.NODESET);
                        
                        List<Relation> relations = new LinkedList<>();
                        
                        for(int i=0; i<entityNodes.getLength(); i++){
                            this.processing((double)(i+1)/(double)entityNodes.getLength(), "importing entity "+(i+1)+"/"+entityNodes.getLength());

                            //entity data
                            Node entityN = entityNodes.item(i);
                            Entity entity = new Entity();
                            
                            entity.setId(Long.parseLong((String)xpath.compile("id").evaluate(entityN, XPathConstants.STRING)));
                            entity.setTitle((String)xpath.compile("title").evaluate(entityN, XPathConstants.STRING));
                            entity.setDescription((String)xpath.compile("description").evaluate(entityN, XPathConstants.STRING));
                            entity.setDate(new Date(Long.parseLong((String)xpath.compile("date").evaluate(entityN, XPathConstants.STRING))));
                            entity.setCreated(new Date(Long.parseLong((String)xpath.compile("created").evaluate(entityN, XPathConstants.STRING))));
                            entity.setType(Integer.parseInt((String)xpath.compile("type").evaluate(entityN, XPathConstants.STRING)));
                            
                            
                            //tags
                            NodeList allTags = (NodeList)xpath.compile("/docz/tags/tag[id='"+entity.id+"']").evaluate(document, XPathConstants.NODESET);
                            XPathExpression xTag = xpath.compile("tag");
                            String tagString = "";
                            for(int j=0; j<allTags.getLength(); j++){
                                Node tagN = allTags.item(j);
                                tagString += xTag.evaluate(tagN, XPathConstants.STRING) + (j!=allTags.getLength()-1 ? ", " : "");
                            }                          
                            entity.setTagsAsString(tagString);
                            
                            
                            //relations
                            NodeList allRelations = (NodeList) xpath.compile("/docz/relations/relation[entity1='"+entity.id+"']").evaluate(document, XPathConstants.NODESET);
                            int tmp = allRelations.getLength();
                            if(tmp > 0) Log.l(tmp+" relations");
                            for(int j=0; j<allRelations.getLength(); j++){
                                Relation relation = new Relation();
                                Node relationN = allRelations.item(j);
                                
                                relation.setID(Long.parseLong((String)xpath.compile("id").evaluate(relationN, XPathConstants.STRING)));
                                relation.setTitle((String)xpath.compile("title").evaluate(relationN, XPathConstants.STRING));
                                relation.setDescription((String)xpath.compile("description").evaluate(relationN, XPathConstants.STRING));
                                relation.setCreated(new Date(Long.parseLong((String)xpath.compile("created").evaluate(relationN, XPathConstants.STRING))));
                                relation.setEntityID1(Long.parseLong((String)xpath.compile("entity1").evaluate(relationN, XPathConstants.STRING)));
                                relation.setEntityID2(Long.parseLong((String)xpath.compile("entity2").evaluate(relationN, XPathConstants.STRING)));
                                relations.add(relation);
                            }
                            
                            
                            //files                           
                            List<File> files = new ArrayList<>();
                            NodeList allFiles = (NodeList) xpath.compile("/docz/files/file[id='"+entity.id+"']").evaluate(document, XPathConstants.NODESET);
                            for(int j=0; j<allFiles.getLength(); j++){
                                Node fileN = allFiles.item(j);
                                String name = (String)xpath.compile("name").evaluate(fileN, XPathConstants.STRING);
                                Date created = new Date(Long.parseLong((String)xpath.compile("created").evaluate(fileN, XPathConstants.STRING)));
                                File file = new File(importDir+File.separator+"files"+File.separator+"id_"+entity.id+File.separator+name);
                                long size = Long.parseLong((String)xpath.compile("size").evaluate(fileN, XPathConstants.STRING));
                                String ocr = (String)xpath.compile("ocr").evaluate(fileN, XPathConstants.STRING);
                                files.add(file);
                            }
                            
                            
                            //save to DB
                            long oldID = entity.id;
                            entity = DataHandler.instance.createEntity(entity.title, entity.description, Arrays.asList(entity.getTags()), entity.date, entity.type);
                            oldAndNewEntityIDLookupTable.put(oldID, entity.id);
                            
                            //save files
                            DataHandler.instance.addFiles(entity, files.toArray(new File[files.size()]));
                            
                            Log.l("imported: "+entity);
                        }
                        
                        this.processing(0.99, "saving relations...");
                        for(Relation relation : relations){
                            DataHandler.instance.createRelation(relation.title, relation.description, 
                                    DataHandler.instance.getEntityByID(oldAndNewEntityIDLookupTable.get(relation.entityID1)), 
                                    DataHandler.instance.getEntityByID(oldAndNewEntityIDLookupTable.get(relation.entityID2))
                            );
                        }
                                              
                        this.processing(1.0, "finished importing...");
                                                
                        
                    } catch (Exception e) {
                        Log.l(e);
                    }
                }
            };
    }
    
    private void saveXMLDocument(Document document, File file) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException ex) {
            Log.l(ex);
        }
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
