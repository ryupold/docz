/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Image;
import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Michael
 */
public class Doc extends Entity {
    private Image thumbnail = null;
    private boolean isModified = false;

    public Doc(int id, String title, String description, List<String> tags, Date date, Date created) {
        
    }

    public static Doc createDoc(String title, String description, List<String> tags, Date date, List<File> files) throws SQLException {
        //generate unique file names
        List<String> fileNames = new ArrayList<>();
        int fn = 0;
        for (File file : files) {
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
        try{
            
            
            
        }finally{
            c.close();
        }
        
        
        
        List<Integer> id = DB.insert("", true);
        

        return null;
    }

    public Blob[] getFiles() {
        return null;
    }

    public Image getThumbnail() {
        return null;
    }

    public boolean isIsModified() {
        return isModified;
    }

    public void setTitle(String title) {
        this.title = title;
        isModified = true;
    }

    public void setDate(Date date) {
        this.date = date;
        isModified = true;
    }

    public void setDescription(String description) {
        this.description = description;
        isModified = true;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        isModified = true;
    }

    public void save() {

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
