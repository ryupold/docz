/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }    

    public void setCreated(Date created) {
        this.created = created;
    }
    
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
    
    public void setTagsAsString(String tagsSeperatedByComma) {
        tags = new ArrayList<>();
        String[] tagSplit = tagsSeperatedByComma.split(",");
        for(String t : tagSplit){
            tags.add(t.trim());
        }
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public Image getThumbnail(int preferedWidth, int preferedHeight, Font font) throws Exception {
        return DataHandler.instance.getThumbnail(this, preferedWidth, preferedHeight, font);
    }

    @Override
    public String toString() {
        return (type == 1 ? "Doc" : type == 2 ? "Institution" : "Entity") + ":[ID="+id+", Title="+title+", Description="+description
                +", Date="+date+", Created="+created+", Tags=("+getTagsAsString()+")";
    }
}
