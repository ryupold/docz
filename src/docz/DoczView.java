/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Michael
 */
public class DoczView extends javax.swing.JPanel {

    private Mode mode = Mode.None;

    private Entity mainEntity;
    private Relation[] relations;
    private Entity[] relatedEntities;
    private Entity[] searchResults;
    

    public void showSearchResults(Entity[] results) {
        mode = Mode.Search;
        searchResults = results;
        repaint();
    }

    /**
     * Creates new form DoczView
     */
    public DoczView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 421, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.

        int paddingX = 10, paddingY = 10, width = 100, height = 200;

        if (mode == Mode.Search && searchResults != null) {
            for (int i = 0; i < searchResults.length; i++) {
                Image img = searchResults[i] instanceof Doc ? ((Doc)searchResults[i]).getThumbnail() : searchResults[i] instanceof Institution ? ((Institution)searchResults[i]).getLogo() : Resources.getImg_relation();
                g.drawImage(img, paddingX+(i*(width+paddingX)), paddingY, width, height, this);
            }
        } else if (mode == Mode.Relations) {

        } else if (mode == Mode.Preview) {

        }
    }

    public enum Mode {

        None,
        Search,
        Relations,
        Preview
    }
}
