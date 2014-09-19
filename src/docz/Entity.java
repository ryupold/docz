/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import de.realriu.riulib.helpers.ScaleImage;
import de.realriu.riulib.helpers.ScaleImage.Rectangle;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.w3c.dom.Document;

/**
 *
 * @author Michael
 */
public class Entity {

    protected long id;
    protected int type = 0;
    protected String title, description;
    protected List<String> tags;
    protected Date date, created;
    protected ImageFile[] images;

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

        Date created = new Date();
        if (date == null) {
            date = created;
        }
        try {
            Long id = DB.insert("insert into entities(title, description, date, created, type) values('" + title + "', '" + description + "', '" + date.getTime() + "', '" + created.getTime() + "', '" + type + "');", true);

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

    public void addFiles(File... files) throws SQLException, IOException {

        List<String> fileNames = new ArrayList<>();
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

                Blob b = c.createBlob();
                OutputStream os = b.setBinaryStream(0);
                FileInputStream fi = new FileInputStream(files[i]);
                byte[] buf = new byte[1024];
                int len;
                while ((len = fi.read(buf)) > 0) {
                    os.write(buf, 0, len);
                }
                fi.close();
                os.close();

                ps.setLong(1, this.id);
                ps.setString(2, fileNames.get(i));
                ps.setLong(3, created.getTime());
                ps.setBlob(4, b);
                ps.execute();
            }
        } finally {
            ps.close();
            c.close();
        }
    }

    public ImageFile[] getImages() throws IOException {
        try {
            DB.DBResult r = DB.select("select name, created, file from files where id='" + id + "'");
            List<ImageFile> imgs = new LinkedList<>();
            while (r.resultSet.next()) {
                byte[] bytes = r.resultSet.getBytes(4);
                imgs.add(new ImageFile(this, r.resultSet.getString(1), new Date(r.resultSet.getLong(2)), ImageIO.read(new ByteArrayInputStream(bytes))));
            }
            r.close();
            return images = imgs.toArray(new ImageFile[imgs.size()]);
        } catch (SQLException ex) {
            Log.l(ex);
            return null;
        }
    }

    /**
     * returns a representive thumbnail of the Doc by taking one of the attached
     * image files. if there is no image linked to this document,
     * Resources.img_other is returned.
     *
     * @param preferedWidth
     * @param preferedHeight
     * @return
     * @throws SQLException
     */
    public Image getThumbnail(int preferedWidth, int preferedHeight) throws SQLException {
        DB.DBResult r = DB.select("select name, file from files where id='" + id + "' and ("
                + "name like '%.png' or"
                + "name like '%.jpg' or"
                + "name like '%.jpeg' or"
                + "name like '%.bmp' or"
                + "name like '%.wbmp' or"
                + "name like '%.gif'"
                + ") limit 1;");
        try {

            if (r.resultSet.next()) {
                byte[] buffer = r.resultSet.getBytes(1);
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
                return Resources.getImg_otherfile();
            }
        } finally {
            r.close();
        }
    }

    public void save() {

    }
}
