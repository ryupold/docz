/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;

/**
 *
 * @author Michael
 */
public class Relation implements Thumbnail {

    protected long id;
    protected String title, description;
    protected Date created;
    protected Entity entity1, entity2;
    protected long entityID1, entityID2;
    

    public Relation() {
    }
    
    public Relation(long id, String title, String description, Date created, Entity entity1, Entity entity2) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created = created;
        this.entity1 = entity1;
        this.entityID1 = entity1.id;
        this.entity2 = entity2;
        this.entityID2 = entity2.id;
    }

    public Date getCreated() {
        return created;
    }

    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

    public void setEntity1(Entity entity1) {
        this.entity1 = entity1;
        this.entityID1 = entity1.id;
    }

    public void setEntity2(Entity entity2) {
        this.entity2 = entity2;
        this.entityID2 = entity2.id;
    }

    public void setEntityID1(long entityID1) {
        this.entityID1 = entityID1;
    }

    public void setEntityID2(long entityID2) {
        this.entityID2 = entityID2;
    }

    public long getEntityID1() {
        return entityID1;
    }

    public long getEntityID2() {
        return entityID2;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Image getThumbnail(int preferedWidth, int preferedHeight, Font font) {
        try {
            return entity2 != null ? entity2.getThumbnail(preferedWidth, preferedHeight, font) : Resources.getImg_relation();
        } catch (Exception ex) {
            Log.l(ex);
            return Resources.getImg_relation();
        }
    }
}
