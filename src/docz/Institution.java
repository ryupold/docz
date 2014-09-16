/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
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
public class Institution extends Entity {

    protected ImageFile[] images;

    public Institution(long id, String title, String description, List<String> tags, Date created) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.date = created;
        this.created = created;
        this.images = null;
    }

    public static Institution createInstitution(String title, String description, List<String> tags, List<File> files) throws SQLException, FileNotFoundException, IOException {
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
        try {
            Long id = DB.insert("insert into entities(title, description, date, created, type) values('" + title + "', '" + description + "', '" + created.getTime() + "', '" + created.getTime() + "', '2');", true);

            if (id != null) {
                for (String tag : tags) {
                    DB.insert("insert into tags(id, tag) values('" + id + "', '" + tag + "');", false);
                }

                PreparedStatement ps = c.prepareStatement("insert into files(id, name, created, file) values(?, ?, ?, ?)");
                for (int i = 0; i < files.size(); i++) {

                    Blob b = c.createBlob();
                    OutputStream os = b.setBinaryStream(0);
                    FileInputStream fi = new FileInputStream(files.get(i));
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = fi.read(buf)) > 0) {
                        os.write(buf, 0, len);
                    }
                    fi.close();
                    os.close();

                    ps.setLong(1, id);
                    ps.setString(2, fileNames.get(i));
                    ps.setLong(3, created.getTime());
                    ps.setBlob(4, b);
                    ps.execute();
                }
                ps.close();

                return new Institution(id, title, description, tags, created);
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
        if (images == null) {
            try {
                DB.DBResult r = DB.select("select name, created, file from files where id='" + id + "'");
                List<ImageFile> imgs = new LinkedList<>();
                while (r.resultSet.next()) {
                    Blob blob = r.resultSet.getBlob(3);
                    InputStream is = blob.getBinaryStream();
                    imgs.add(new ImageFile(this, r.resultSet.getString(1), new Date(r.resultSet.getLong(2)), ImageIO.read(is)));
                    is.close();
                }
                r.close();
                return images = imgs.toArray(new ImageFile[imgs.size()]);
            } catch (SQLException ex) {
                Log.l(ex);
            }
        }

        return images;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Institution)) {
            return false;
        }

        return ((Institution) obj).getID() == id;
    }
}
