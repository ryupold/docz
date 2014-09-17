/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michael
 */
public class Relation {

    protected String title, description;
    protected Date created;
    private Entity entity1, entity2;
    private boolean isModified = false;

    public Relation(String title, String description, Date created, Entity entity1, Entity entity2) {
        this.title = title;
        this.description = description;
        this.created = created;
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public static Relation createRelation(String title, String description, Entity entity1, Entity entity2) throws SQLException {
        Connection c = DB.createConnection();
        Date created = new Date();
        try {
            DB.insert("insert into relations(title, description, created, entity1, entity2) values('"+title+"', '"+description+"', '"+created.getTime()+"', '"+entity1.id+"', '"+entity2.id+"');", false);

            return new Relation(title, description, created, entity1, entity2);
        } finally {
            c.close();
        }
    }

    public Date getCreated() {
        return created;
    }

    public boolean isIsModified() {
        return isModified;
    }

    public void setTitle(String title) {
        this.title = title;
        isModified = true;
    }

    public void setDescription(String description) {
        this.description = description;
        isModified = true;
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

    public void setEntity1(Entity entity1) {
        this.entity1 = entity1;
        isModified = true;
    }

    public void setEntity2(Entity entity2) {
        this.entity2 = entity2;
        isModified = true;
    }

    public void save(Document DB) {

        isModified = false;
    }
}
