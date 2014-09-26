/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import de.realriu.riulib.helpers.ScaleImage.Rectangle;
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
public class Entity implements Thumbnail {

    protected long id;
    protected int type = 0;
    protected String title, description;
    protected List<String> tags;
    protected Date date, created;
    //protected ImageFile[] images;

    public Date getCreated() {
        return created;
    }

    public Date getDate() {
        return date;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags.toArray(new String[tags.size()]);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTagsAsString() {
        String tagsString = "";
        boolean first = true;
        for (String t : tags) {
            if (first) {
                first = false;
            } else {
                tagsString += ", ";
            }
            tagsString += t;
        }
        return tagsString;
    }

    public void setTags(String... tags) {
        this.tags = new ArrayList<>(tags.length);
        for (String tag : tags) {
            this.tags.add(tag);
        }
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public static Entity createEntity(String title, String description, List<String> tags, Date date, List<File> files, int type) throws SQLException, FileNotFoundException, IOException {
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

    public boolean removeFile(String name) throws SQLException {
        return DB.update("delete from files where name='" + name + "' and id='" + this.id + "'") > 0;
    }

    public String[] addFiles(File... files) throws SQLException, IOException {

        List<String> fileNames = new ArrayList<>();
        try (DB.DBResult r = DB.select("SELECT name from files where id='" + id + "';")) {
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

        Connection c = DB.createConnection();
        PreparedStatement ps = c.prepareStatement("insert into files(id, name, created, file) values(?, ?, ?, ?)");
        try {
            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                ps.setLong(1, this.id);
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

    public int countFiles() throws IOException {
        int count = 0;
        try {
            DB.DBResult r = DB.select("SELECT COUNT(*) FROM files WHERE id='" + id + "'");

            if (r.resultSet.next()) {
                count = r.resultSet.getInt(1);
            }
            r.close();
        } catch (SQLException ex) {
            Log.l(ex);
        }
        return count;
    }

    public String[] getFileNames() throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created from files where id='" + id + "';");
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
    
    public byte[] getFile(String name) throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created, file from files where id='" + id + "' AND name='" + name + "'");
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

    public ImageFile[] getThumbnails(int preferedWidth, int preferedHeight) throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created, file from files where id='" + id + "'");
            List<ImageFile> imgs = new LinkedList<>();
            while (r.resultSet.next()) {
                BufferedImage sImg;
                try {
                    byte[] bytes = r.resultSet.getBytes(3);
                    ByteArrayInputStream bias = new ByteArrayInputStream(bytes);
                    sImg = ImageIO.read(bias);
                    bias.close();
                    Rectangle rec = ScaleImage.fitToRect(preferedWidth, preferedHeight, sImg);
                    sImg = ScaleImage.scale(sImg, rec.width, rec.heigth);
                } catch (IllegalArgumentException iae) {
                    sImg = Resources.createImageWithText(r.resultSet.getString(1), preferedWidth, preferedHeight / 2);
                }
                imgs.add(new ImageFile(this, r.resultSet.getString(1), new Date(r.resultSet.getLong(2)), sImg));

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
    public Image getThumbnail(int preferedWidth, int preferedHeight) throws SQLException {
        DB.DBResult r = DB.select("select name, file from files where id='" + id + "' and ("
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
                    Rectangle newSize = ScaleImage.fitToRect(preferedWidth, preferedHeight, img);
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
                r = DB.select("select name from files where id='" + id + "' limit 1;");
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

    public Image getThumbnail(String name, int preferedWidth, int preferedHeight) throws SQLException {
        try (DB.DBResult r = DB.select("select name, file from files where id='" + id + "' and name='" + name + "'")) {

            if (r.resultSet.next()) {
                String fileName = r.resultSet.getString(1);
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".bmp") || fileName.endsWith(".wbmp") || fileName.endsWith(".gif") || fileName.endsWith(".png")) {
                    try {
                        byte[] buffer = r.resultSet.getBytes(2);
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                        BufferedImage img = ImageIO.read(bais);
                        Rectangle newSize = ScaleImage.fitToRect(preferedWidth, preferedHeight, img);
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

    public void save() {

    }
}
