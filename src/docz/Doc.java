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
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Michael
 */
public class Doc extends Entity {

    private long id;
    private List<String> tags;
    private Date date, created;
    private Image thumbnail = null;

    private boolean isModified = false;

    public Doc() {

    }

    public static Doc createDoc(String title, String description, List<String> tags, Date date, List<File> files) {
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

        

        return null;
    }

    public List<String> getTags() {
        return tags;
    }

    public Date getCreated() {
        return created;
    }

    public Date getDate() {
        return date;
    }

    public Blob[] getFiles() {
        return null;
    }

    public long getID() {
        return id;
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
