/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hr.algebra.views;

import hr.algebra.dal.RepositoryFactory;
import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.MovieArchive;
import hr.algebra.rss.MovieParser;
import static hr.algebra.utilities.FileUtils.copyFromUrl;
import hr.algebra.utilities.JAXBUtils;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ljubo
 */
public class AdminPanel extends javax.swing.JPanel {

    /**
     * Creates new form AdminPanel
     */
    public AdminPanel() {
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

        btnXMLsave = new javax.swing.JButton();
        btnDeleteAll = new javax.swing.JButton();
        btnRssParser = new javax.swing.JButton();

        btnXMLsave.setText("XML download");
        btnXMLsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXMLsaveActionPerformed(evt);
            }
        });

        btnDeleteAll.setText("Erase DB");
        btnDeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAllActionPerformed(evt);
            }
        });

        btnRssParser.setText("Parse Movies");
        btnRssParser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRssParserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRssParser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnXMLsave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeleteAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(btnXMLsave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(btnDeleteAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(btnRssParser)
                .addGap(52, 52, 52))
        );
    }// </editor-fold>//GEN-END:initComponents

    public static String FILENAME = "src/main/resources/xmlmovie.xml";
    public static String PHOTO_PATH = "src/main/resources/assets";

    private void btnXMLsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXMLsaveActionPerformed
        new Thread(() ->{
            try {
            
            MovieArchive moviesArchive = new MovieArchive(RepositoryFactory.getRepository().getMovies());
            JAXBUtils.save(moviesArchive, FILENAME);

        } catch (Exception ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        }).start();
       
    }//GEN-LAST:event_btnXMLsaveActionPerformed

    private void btnDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAllActionPerformed
        try {
            RepositoryFactory.getRepository().deleteAll();
        } catch (Exception ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Tu sam trebo jos rjesit da brise i slike..
    }//GEN-LAST:event_btnDeleteAllActionPerformed

    private void btnRssParserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRssParserActionPerformed
        
        new Thread(() -> {
            try {
            List<Movie> movieList = MovieParser.parse();
            for (Movie movie : movieList) {
                copyFromUrl(movie.getPhotoPath(), (PHOTO_PATH + File.separator + getPhotoTitle(movie.getPhotoPath())));
                movie.setPhotoPath(PHOTO_PATH + File.separator + getPhotoTitle(movie.getPhotoPath()));
                RepositoryFactory.getRepository().saveMovie(movie);

            }
        } catch (Exception ex) {
            //Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
        }).start();
    }//GEN-LAST:event_btnRssParserActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteAll;
    private javax.swing.JButton btnRssParser;
    private javax.swing.JButton btnXMLsave;
    // End of variables declaration//GEN-END:variables

    private String getPhotoTitle(String photoPath) {
        String[] splitetPath = photoPath.split("/");
        return splitetPath[splitetPath.length - 1];

    }

}
