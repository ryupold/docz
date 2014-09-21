/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Image;

/**
 *
 * @author Michael
 */
public interface Thumbnail {
    Image getThumbnail(int preferedWidth, int preferedHeight) throws Exception;
    String getTitle();
}
