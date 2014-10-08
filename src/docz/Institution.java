/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Michael
 */
public class Institution extends Entity {

    public Institution(Entity entity){
        this(entity.id, entity.title, entity.description, entity.tags, entity.created);
    }
    
    public Institution(long id, String title, String description, List<String> tags, Date created) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.date = created;
        this.created = created;
    }

    public static Institution createInstitution(String title, String description, List<String> tags) throws SQLException, FileNotFoundException, IOException {
        return new Institution(DataHandler.instance.createEntity(title, description, tags, null, 2));
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
