/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Image;
import java.util.Date;

/**
 *
 * @author Michael
 */
public class ImageFile {
    public final Entity doc;
    public final String name;
    public final Date created;
    public final Image image;

    public ImageFile(Entity entity, String name, Date created, Image image) {
        this.doc = entity;
        this.name = name;
        this.created = created;
        this.image = image;
    }
    
}
