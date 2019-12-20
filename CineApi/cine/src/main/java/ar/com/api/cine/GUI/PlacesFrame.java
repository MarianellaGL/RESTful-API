package ar.com.api.cine.GUI;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import ar.com.api.cine.maps.java.Places;
import ar.com.api.cine.maps.java.ShowMaps;
import ar.com.api.cine.maps.java.StaticMaps;

/**
 *
 * @author Luis Marcos
 */
public class PlacesFrame extends javax.swing.JFrame {

    /**
     * Creates new form PlacesFrame
     */
    private String referencePlace;
    private Places ObjPlaces=new Places();
    private String[][] datosReview;
    private ArrayList<String> fotosReferencia;
    private ShowMaps ObjShowMaps=new ShowMaps();
    private StaticMaps ObjStaticMaps=new StaticMaps();
    
    private Icon cargarImagen(String url) throws IOException{
        Image imagen;
        imagen=ImageIO.read(new URL(url));
        imagen=imagen.getScaledInstance(35,35,Image.SCALE_FAST);
        ImageIcon imgIcon=new ImageIcon(imagen);
        Icon iconImage=(Icon)imgIcon;
        return iconImage;
    }
            
    private void cargarInfo() throws UnsupportedEncodingException, MalformedURLException, IOException{
        String[] datos=ObjPlaces.getPlacesDetails(this.referencePlace);
        this.JText_DP_Nombre.setText(datos[0]);
        this.JText_DP_Vecindad.setText(datos[1]);
        this.JText_DP_Telefono.setText(datos[2]);
        this.JText_DP_Direccion.setText(datos[3]);
        this.JText_DP_URLGoogle.setText(datos[4]);
        this.JText_DP_Puntuacion.setText(datos[5]);
        this.JText_DP_Web.setText(datos[7]);
        this.JLabel_DP_NombrePr.setText(datos[0]);
        this.JLabel_DP_Icono.setText("");
        this.JLabel_DP_Icono.setIcon(cargarImagen(datos[6]));
         this.jLabel_DP_MapaEst.setText("");
        Image mapaEst=ObjStaticMaps.getStaticMap(datos[3],15,new Dimension(480,80),1, StaticMaps.Format.jpg, StaticMaps.Maptype.roadmap);
        ImageIcon imgIcon=new ImageIcon(mapaEst);
        Icon iconImage=(Icon)imgIcon;   
        this.jLabel_DP_MapaEst.setIcon(iconImage);
        
    }
    
    private void seleccionarReview(){
         if(datosReview.length>0){
             int indice=this.jList_Review.getSelectedIndex();
             JText_Autor.setText(datosReview[indice][1]);
             JText_URLAutor.setText(datosReview[indice][3]);
             jTextArea_Review.setText(datosReview[indice][2]);
         }
    }
        
    private void cargarReview() throws UnsupportedEncodingException, MalformedURLException, IOException{
        datosReview=ObjPlaces.getPlaceReview(this.referencePlace);
        DefaultListModel modelo = new DefaultListModel();
        if(datosReview.length>0){
            for(int i=0;i<datosReview.length;i++){
                modelo.add(i, "Review por: " + datosReview[i][1]);
            }
            this.jList_Review.setModel(modelo);
            this.jList_Review.setSelectedIndex(0);
            
        }else{
            modelo.add(0,"No hay datos disponibles");
             this.jList_Review.setModel(modelo);
        }
    }
    
    private void cargarFoto() throws UnsupportedEncodingException, MalformedURLException, IOException{
        this.fotosReferencia=ObjPlaces.getPhotosReference();
        DefaultListModel modelo = new DefaultListModel();
        if(fotosReferencia.size()>0){
            for(int i=0;i<fotosReferencia.size();i++){
                modelo.add(i, "Foto " + (i+1));
            }
            this.jList_Fotos.setModel(modelo);
            this.jList_Fotos.setSelectedIndex(0);
            
        }else{
            modelo.add(0,"No hay datos disponibles");
            this.jList_Fotos.setModel(modelo);
        }
    }
       
    private void seleccionarFoto() throws MalformedURLException{
         if(fotosReferencia.size()>0){
             int indice=this.jList_Fotos.getSelectedIndex();
             Image imagenAux=ObjPlaces.getPhoto(fotosReferencia.get(indice), 800);
             ImageIcon imgIcon=new ImageIcon(imagenAux);
             Icon iconImage=(Icon)imgIcon;   
             this.jLabe_Imagen.setIcon(iconImage);
         }
    }
            
    public PlacesFrame(String _referencePlace) throws UnsupportedEncodingException {
        initComponents();
        referencePlace=_referencePlace;
        try {
          cargarInfo();  
          cargarReview();
          cargarFoto();
        } catch (Exception e) {
        }
    }

    private PlacesFrame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        JText_DP_Nombre = new javax.swing.JTextField();
        JText_DP_Vecindad = new javax.swing.JTextField();
        JText_DP_Direccion = new javax.swing.JTextField();
        JText_DP_Telefono = new javax.swing.JTextField();
        JText_DP_URLGoogle = new javax.swing.JTextField();
        JText_DP_Puntuacion = new javax.swing.JTextField();
        JText_DP_Web = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        JLabel_DP_NombrePr = new javax.swing.JLabel();
        JLabel_DP_Icono = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel_DP_MapaEst = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_Review = new javax.swing.JList();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        JText_Autor = new javax.swing.JTextField();
        JText_URLAutor = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_Review = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList_Fotos = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        jLabe_Imagen = new javax.swing.JLabel();

        setTitle("Detalles Place");
        setAlwaysOnTop(true);
        setType(java.awt.Window.Type.UTILITY);

        jLabel1.setText("Nombre:");

        jLabel2.setText("Vecindad:");

        jLabel3.setText("Dirección:");

        jLabel4.setText("Teléfono:");

        jLabel5.setText("URL Google+:");

        jLabel6.setText("Puntuación (/5):");

        jLabel7.setText("Página web:");

        JText_DP_Nombre.setEditable(false);

        JText_DP_Vecindad.setEditable(false);

        JText_DP_Direccion.setEditable(false);

        JText_DP_Telefono.setEditable(false);

        JText_DP_URLGoogle.setEditable(false);

        JText_DP_Puntuacion.setEditable(false);

        JText_DP_Web.setEditable(false);

        jButton1.setText("Abrir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Abrir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        JLabel_DP_NombrePr.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        JLabel_DP_NombrePr.setText("Nombre");

        JLabel_DP_Icono.setText("Icono");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JLabel_DP_NombrePr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JLabel_DP_Icono)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(JLabel_DP_NombrePr))
            .addComponent(JLabel_DP_Icono)
        );

        jButton4.setText("Abrir");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel_DP_MapaEst.setText("jLabel11");
        jLabel_DP_MapaEst.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JText_DP_Nombre)
                            .addComponent(JText_DP_Vecindad)
                            .addComponent(JText_DP_Telefono)
                            .addComponent(JText_DP_Puntuacion)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(JText_DP_URLGoogle, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(JText_DP_Web)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(JText_DP_Direccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel_DP_MapaEst, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JText_DP_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JText_DP_Vecindad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(JText_DP_Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(JText_DP_Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(JText_DP_URLGoogle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(JText_DP_Puntuacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(JText_DP_Web, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jLabel_DP_MapaEst, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Datos place", jPanel2);

        jList_Review.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_ReviewValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList_Review);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Autor:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("URL autor:");

        JText_Autor.setEditable(false);

        JText_URLAutor.setEditable(false);

        jButton3.setText("Abrir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextArea_Review.setEditable(false);
        jTextArea_Review.setColumns(20);
        jTextArea_Review.setLineWrap(true);
        jTextArea_Review.setRows(5);
        jTextArea_Review.setToolTipText("");
        jScrollPane2.setViewportView(jTextArea_Review);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Review:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JText_Autor)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(JText_URLAutor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(JText_Autor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(JText_URLAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Review place", jPanel1);

        jList_Fotos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_FotosValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList_Fotos);

        jLabe_Imagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPane4.setViewportView(jLabe_Imagen);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Fotografías place", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Datos place");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void abrirWeb(String url) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI(url));
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(!JText_DP_URLGoogle.getText().isEmpty() && !"NO DATA".equals(JText_DP_URLGoogle.getText())){
            try {
                abrirWeb(JText_DP_URLGoogle.getText());
            } catch (Exception ex) {
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       if(!JText_DP_Web.getText().isEmpty() && !"NO DATA".equals(JText_DP_Web.getText())){
            try {
                abrirWeb(JText_DP_Web.getText());
            } catch (Exception ex) {
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(!JText_URLAutor.getText().isEmpty()){
            try {
                abrirWeb(JText_URLAutor.getText());
            } catch (Exception ex) {
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jList_ReviewValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_ReviewValueChanged
        seleccionarReview();
    }//GEN-LAST:event_jList_ReviewValueChanged

    private void jList_FotosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_FotosValueChanged
        try {
            seleccionarFoto();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jList_FotosValueChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(!JText_DP_Direccion.getText().isEmpty()){
            try {
                String direccionMapa = ObjShowMaps.getURLMap(JText_DP_Direccion.getText());
                Desktop.getDesktop().browse(new URI(direccionMapa));
            } catch (Exception ex) {
            }
        }  
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PlacesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlacesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlacesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlacesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PlacesFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel_DP_Icono;
    private javax.swing.JLabel JLabel_DP_NombrePr;
    private javax.swing.JTextField JText_Autor;
    private javax.swing.JTextField JText_DP_Direccion;
    private javax.swing.JTextField JText_DP_Nombre;
    private javax.swing.JTextField JText_DP_Puntuacion;
    private javax.swing.JTextField JText_DP_Telefono;
    private javax.swing.JTextField JText_DP_URLGoogle;
    private javax.swing.JTextField JText_DP_Vecindad;
    private javax.swing.JTextField JText_DP_Web;
    private javax.swing.JTextField JText_URLAutor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabe_Imagen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_DP_MapaEst;
    private javax.swing.JList jList_Fotos;
    private javax.swing.JList jList_Review;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea_Review;
    // End of variables declaration//GEN-END:variables
}
