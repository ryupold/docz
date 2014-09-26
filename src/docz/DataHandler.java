/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
    
    public Entity createEntity(String title, String description, List<String> tags, Date date, List<File> files, int type) throws SQLException, FileNotFoundException, IOException {
        //generate unique file names
        List<String> fileNames = new ArrayList<>();
        int fn = 0;
        for (File file : files) {
            fn = 0;
            while (fileNames.contains((fn == 0 ? "" : "d" + fn + "_") + file.getName())) {
                fn++;
            }

            if (fn == 0) {
                fileNames.add(file.getName());
            } else {
                fileNames.add("d" + fn + "_" + file.getName());
            }
        }

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

                PreparedStatement ps = c.prepareStatement("insert into files(id, name, created, file) values(?, ?, ?, ?)");
                for (int i = 0; i < files.size(); i++) {
                    FileInputStream fi = new FileInputStream(files.get(i));
                    byte[] buf = new byte[fi.available()];
                    fi.read(buf);
                    fi.close();

                    ps.setLong(1, id);
                    ps.setString(2, fileNames.get(i));
                    ps.setLong(3, created.getTime());
                    ps.setBytes(4, buf);
                    ps.execute();
                }
                ps.close();

                if (type == 1) {
                    return new Doc(id, title, description, tags, date, created);
                } else if (type == 2) {
                    return new Institution(id, title, description, tags, created);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } finally {
            c.close();
        }
    }
    
    public boolean removeFile(Entity entity, String name) throws SQLException {
        return DB.update("delete from files where name='" + name + "' and id='" + entity.id + "'") > 0;
    }

    public String[] addFiles(Entity entity, File... files) throws SQLException, IOException {

        List<String> fileNames = new ArrayList<>();
        try (DB.DBResult r = DB.select("SELECT name from files where id='" + entity.id + "';")) {
            while (r.resultSet.next()) {
                fileNames.add(r.resultSet.getString(1));
            }
        }

        int fn = 0;
        for (File file : files) {
            fn = 0;
            while (fileNames.contains(file.getName())) {
                fn++;
            }

            if (fn == 0) {
                fileNames.add(file.getName());
            } else {
                fileNames.add("d" + fn + "_" + file.getName());
            }
        }
        Date created = new Date();
        Connection c = DB.createConnection();
        PreparedStatement ps = c.prepareStatement("insert into files(id, name, created, file) values(?, ?, ?, ?)");
        try {
            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                ps.setLong(1, entity.id);
                ps.setString(2, fileNames.get(i));
                ps.setLong(3, created.getTime());
                ps.setBinaryStream(4, fi);
                ps.execute();
            }
        } finally {
            ps.close();
            c.close();
        }

        return fileNames.toArray(new String[fileNames.size()]);
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
    
    public byte[] getFile(Entity entity, String name) throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created, file from files where id='" + entity.id + "' AND name='" + name + "'");
            byte[] bytes = null;
            if (r.resultSet.next()) {
                bytes = r.resultSet.getBytes(3);

            }
            r.close();
            return bytes;
        } catch (SQLException ex) {
            Log.l(ex);
            return null;
        }
    }

    public ImageFile[] getThumbnails(Entity entity, int preferedWidth, int preferedHeight) throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created, file from files where id='" + entity.id + "'");
            List<ImageFile> imgs = new LinkedList<>();
            while (r.resultSet.next()) {
                BufferedImage sImg;
                try {
                    byte[] bytes = r.resultSet.getBytes(3);
                    ByteArrayInputStream bias = new ByteArrayInputStream(bytes);
                    sImg = ImageIO.read(bias);
                    bias.close();
                    ScaleImage.Rectangle rec = ScaleImage.fitToRect(preferedWidth, preferedHeight, sImg);
                    sImg = ScaleImage.scale(sImg, rec.width, rec.heigth);
                } catch (IllegalArgumentException iae) {
                    sImg = Resources.createImageWithText(r.resultSet.getString(1), preferedWidth, preferedHeight / 2);
                }
                imgs.add(new ImageFile(entity, r.resultSet.getString(1), new Date(r.resultSet.getLong(2)), sImg));

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
    public Image getThumbnail(Entity entity, int preferedWidth, int preferedHeight) throws SQLException {
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
                byte[] buffer = r.resultSet.getBytes(2);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                try {
                    BufferedImage img = ImageIO.read(bais);
                    ScaleImage.Rectangle newSize = ScaleImage.fitToRect(preferedWidth, preferedHeight, img);
                    img = ScaleImage.scale(img, newSize.width, newSize.heigth);
                    return img;
                } catch (IOException ex) {
                    return Resources.getImg_otherfile();
                } finally {
                    try {
                        bais.close();
                    } catch (IOException ex) {
                        Log.l(ex);
                    }
                }
            } else {
                r.close();
                r = DB.select("select name from files where id='" + entity.id + "' limit 1;");
                if (r.resultSet.next()) {
                    BufferedImage img = new BufferedImage(preferedWidth, preferedHeight / 4, BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D g = (Graphics2D) img.getGraphics();
//                    g.setColor(Color.black);
//                    g.fillRect(0, 0, img.getWidth(), img.getHeight());
                    g.setColor(Color.red);
                    g.drawString(r.resultSet.getString(1), 10, img.getHeight() / 2 + 5);
                    //Rectangle newSize = ScaleImage.fitToRect(preferedWidth, preferedHeight, img);
                    //img = ScaleImage.scale(img, newSize.width, newSize.heigth);
                    return img;
                } else {
                    return Resources.getImg_nofiles();
                }
            }
        } finally {
            r.close();
        }
    }

    public Image getThumbnail(Entity entity, String name, int preferedWidth, int preferedHeight) throws SQLException {
        try (DB.DBResult r = DB.select("select name, file from files where id='" + entity.id + "' and name='" + name + "'")) {

            if (r.resultSet.next()) {
                String fileName = r.resultSet.getString(1);
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".bmp") || fileName.endsWith(".wbmp") || fileName.endsWith(".gif") || fileName.endsWith(".png")) {
                    try {
                        byte[] buffer = r.resultSet.getBytes(2);
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                        BufferedImage img = ImageIO.read(bais);
                        ScaleImage.Rectangle newSize = ScaleImage.fitToRect(preferedWidth, preferedHeight, img);
                        img = ScaleImage.scale(img, newSize.width, newSize.heigth);
                        bais.close();
                        return img;
                    } catch (IOException ex) {
                        return Resources.createImageWithText(name, preferedWidth, preferedHeight);
                    }
                } else {
                    return Resources.createImageWithText(name, preferedWidth, preferedHeight);
                }
            } else {
                return Resources.getImg_nofiles();
            }
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
