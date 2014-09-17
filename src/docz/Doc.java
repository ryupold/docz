/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import docz.DB.DBResult;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
public class Doc extends Entity {

    public Doc(Entity entity){
        this(entity.id, entity.title, entity.description, entity.tags, entity.date, entity.created);
    }
    
    public Doc(long id, String title, String description, List<String> tags, Date date, Date created) {
        this.type = 1;
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.date = date;
        this.created = created;
        this.images = null;
    }

    public static Doc createDoc(String title, String description, List<String> tags, Date date, List<File> files) throws SQLException, FileNotFoundException, IOException {
        return new Doc(Entity.createEntity(title, description, tags, date, files, 1));
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
        if (!(obj instanceof Doc)) {
            return false;
        }

        return ((Doc) obj).getID() == id;
    }

}
