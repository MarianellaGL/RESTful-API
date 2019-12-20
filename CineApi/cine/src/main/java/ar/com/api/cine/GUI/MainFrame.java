package ar.com.api.cine.GUI;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Point2D;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jsoup.*;


import ar.com.api.cine.maps.java.Elevation;
import ar.com.api.cine.maps.java.Geocoding;
import ar.com.api.cine.maps.java.MapsJava;
import ar.com.api.cine.maps.java.Places;
import ar.com.api.cine.maps.java.Route;
import ar.com.api.cine.maps.java.ShowMaps;
import ar.com.api.cine.maps.java.StaticMaps;
import ar.com.api.cine.maps.java.StreetView;

/**
 *
 * @author Luis Marcos
 */
public class MainFrame extends javax.swing.JFrame {
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        capturarEventos();
    }

    private EventsStatusBar ObjStatusBar;
  
    
    private Geocoding ObjGeocoding=new Geocoding();
    private Elevation ObjElevation=new Elevation();
    private ShowMaps ObjShowMaps=new ShowMaps();
    private Route ObjRoute=new Route();
    private StreetView ObjStreetView=new StreetView();
    private StaticMaps ObjStaticMaps=new StaticMaps();
    private Places ObjPlaces=new Places();
    
    private void capturarEventos(){
        ObjStatusBar=new EventsStatusBar(this.jPanel5);
        recorrerComponentes(jTabbedPane1.getComponents());
        recorrerComponentes(jPanel1.getComponents());
        recorrerComponentes(jPanel2.getComponents());
        recorrerComponentes(jPanel3.getComponents());
        recorrerComponentes(jPanel4.getComponents());
        recorrerComponentes(jPanel6.getComponents());
        recorrerComponentes(jPanel7.getComponents());
        recorrerComponentes(jPanel8.getComponents());
        recorrerComponentes(jPanel9.getComponents());
    }
    double redondeoDosDecimales(double d) {
        return Math.rint(d*1000)/1000;
    }
    private void recorrerComponentes(Component[] componentes){
        for(int i=0; i<componentes.length;i++){ 
            componentes[i].addMouseListener(ObjStatusBar);
        }
    }
    private void actualizarPropiedades(){
        JText_Conexion.setText(String.valueOf(MapsJava.getConnectTimeout()));
        JText_idioma.setText(MapsJava.getLanguage());
        JText_Region.setText(MapsJava.getRegion());
        
        if(MapsJava.getSensor()==true){
            JCombo_Sensor.setSelectedIndex(1);
        }else{
            JCombo_Sensor.setSelectedIndex(0);
        }
        JText_Clave.setText(MapsJava.getKey());
    }
    private void pegarTexto() throws ClassNotFoundException, UnsupportedFlavorException, IOException{
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = cb.getContents(this);
        DataFlavor dataFlavorStringJava = new DataFlavor("application/x-java-serialized-object; class=java.lang.String");
        if (t.isDataFlavorSupported(dataFlavorStringJava)) {
           String claveApi = (String) t.getTransferData(dataFlavorStringJava);
           JText_Clave.setText(claveApi);
        }
    }
    
    private void comprobarClaveApi(){
        String status=MapsJava.APIkeyCheck(JText_Clave.getText());
        if("OK".equals(status)){
            this.JLabel_Clave.setText("Válida");
        }else{
            this.JLabel_Clave.setText("No Válida");
        }
    }
    private void comprobarStatus(JLabel label){
         label.setText(MapsJava.getLastRequestStatus());
    }
    private void cargarJList(ArrayList<String> arrayList, JList jlist){
        DefaultListModel listModel = new DefaultListModel();
        for(int i=0; i<arrayList.size(); i++) {
            listModel.add(i, arrayList.get(i));
        }
        jlist.setModel(listModel);
    }
    private void seleccionarItemList(){
        String itemSelecionado=(String)this.jList_CI_DirEncon.getSelectedValue();
        this.JText_CI_DireEnc.setText(itemSelecionado);
    }
    
    private void rellenarPeticiones(){
        String[][] peticiones=MapsJava.getStockRequest();
        String[] columnas=new String[6];
        columnas[0]="Número";columnas[1]="Hora";columnas[2]="Status";columnas[3]="URL";columnas[4]="Información";columnas[5]="Excepción";
        TableModel tableModel=new DefaultTableModel(peticiones, columnas);
       this.jTable_Peticiones.setModel(tableModel);
    }
    
    
    private void mostrarMapa(String direccion) throws IOException, URISyntaxException{
        String direccionMapa=ObjShowMaps.getURLMap(direccion);
        Desktop.getDesktop().browse(new URI(direccionMapa));
    }
    private void mostrarMapa(Double latitud, Double longitud) throws URISyntaxException, IOException{
        String direccionMapa=ObjShowMaps.getURLMap(latitud,longitud);
        Desktop.getDesktop().browse(new URI(direccionMapa));
    }
    private void CodiGeografica() throws UnsupportedEncodingException, MalformedURLException{
        if(!this.JText_CD_Direc.getText().isEmpty()){
            JText_CD_DireEnc.setText("");
            Point2D.Double resultado=ObjGeocoding.getCoordinates(this.JText_CD_Direc.getText());
            JText_CD_Lati.setText(String.valueOf(resultado.x));
            JText_CD_Long.setText(String.valueOf(resultado.y));
            JText_CD_DireEnc.setText(String.valueOf(ObjGeocoding.getAddressFound()));
            JText_CD_CodigPost.setText(ObjGeocoding.getPostalcode());
            JText_CD_Resolucion.setText(ObjGeocoding.getPostalcode());
        }
    }
    
    private void CodiGeograficaInver() throws UnsupportedEncodingException, MalformedURLException{
        if(!this.JText_CI_Lati.getText().isEmpty() && !this.JText_CI_Long.getText().isEmpty()){
            JText_CI_DireEnc.setText("");
            DefaultListModel model = new DefaultListModel(); jList_CI_DirEncon.setModel(model);
            ArrayList<String> resultado=ObjGeocoding.getAddress(Double.valueOf(this.JText_CI_Lati.getText()),
                    Double.valueOf(this.JText_CI_Long.getText()));
            if(resultado.size()>0){
                JText_CI_DireEnc.setText(resultado.get(0));
            }
            JText_CI_CodigPost.setText(ObjGeocoding.getPostalcode());
            cargarJList(resultado,jList_CI_DirEncon);
        }
    }

    private void elevacionCD(JTextField txtLati, JTextField txtLong) throws MalformedURLException{
        if(!txtLati.getText().isEmpty() && !txtLong.getText().isEmpty()){
            double resultado=ObjElevation.getElevation(Double.valueOf(txtLati.getText()),
                    Double.valueOf(txtLong.getText()));
            JText_CD_Elevacion.setText(String.valueOf(resultado));
            JText_CD_Resolucion.setText(String.valueOf(ObjElevation.getResolution()));

        }
    }
     private void elevacionCI(JTextField txtLati, JTextField txtLong) throws MalformedURLException{
        if(!txtLati.getText().isEmpty() && !txtLong.getText().isEmpty()){
            double resultado=ObjElevation.getElevation(Double.valueOf(txtLati.getText()),
                    Double.valueOf(txtLong.getText()));
            JText_CI_Elevacion.setText(String.valueOf(resultado));
            JText_CI_Resolucion.setText(String.valueOf(ObjElevation.getResolution()));

        }
    }
    private Route.avoids seleccionarRestricciones(){
        Route.avoids avoid= Route.avoids.nothing;
        switch(JCombo_Ruta_Restricc.getSelectedItem().toString()){
            case "Ninguna":
                avoid= Route.avoids.nothing;
                break;
            case "Peajes":
                avoid=Route.avoids.tolls;
                break;
            case "Autopistas/autovías":
                avoid=Route.avoids.highways;
                break;
        }
        return avoid;
    }
    
    private Route.mode seleccionarModoRuta(){
        Route.mode modo= Route.mode.driving;
        switch(JCombo_Ruta_Trasnpo.getSelectedItem().toString()){
            case "Coche":
                modo= Route.mode.driving;
                break;
            case "Andando":
                modo=Route.mode.walking;
                break;
            case "Bicicleta":
                modo=Route.mode.bicycling;
                break;
        }
        return modo;
    }
    private StaticMaps.Format seleccionarFormato(){
        StaticMaps.Format formato= StaticMaps.Format.png;
        switch(JCombo_ME_Formato.getSelectedItem().toString()){
            case "png":
                formato= StaticMaps.Format.png;
                break;
            case "png32":
                formato= StaticMaps.Format.png32;
                break;
            case "gif":
                formato= StaticMaps.Format.gif;
                break;
            case "jpg":
                formato= StaticMaps.Format.jpg;
                break;
            case "jpg_baseline":
                formato= StaticMaps.Format.jpg_baseline;
                break;
        }
        return formato;
    }
    
    private StaticMaps.Maptype seleccionarTipoMapa(){
        StaticMaps.Maptype tipoMapa= StaticMaps.Maptype.roadmap;
        switch(JCombo_ME_TipoMapa.getSelectedItem().toString()){
            case "roadmap":
                tipoMapa= StaticMaps.Maptype.roadmap;
                break;
            case "satellite":
                tipoMapa= StaticMaps.Maptype.satellite;
                break;
            case "hybrid":
                tipoMapa= StaticMaps.Maptype.hybrid;
                break;
            case "terrain":
                tipoMapa= StaticMaps.Maptype.terrain;
                break;
        }
        return tipoMapa;
    }
        
    private void rellenarTablaRuta(String[][] ruta){
        String[] columnas=new String[5];
        columnas[0]="Duración tramo";columnas[1]="Distancia tramo";columnas[2]="Indicaciones";columnas[3]="Latitud";columnas[4]="Longitud";
        for(int i=0;i<ruta.length;i++){
            try {
                 ruta[i][2]=Jsoup.parse(ruta[i][2]).text();
            } catch (Exception e) {
            }
        }
        TableModel tableModel=new DefaultTableModel(ruta, columnas);
        this.jTable_Ruta_Tramos.setModel(tableModel);
    }
    private void rellenarDatosrRuta(){
         this.JLabel_Ruta_Copyright.setText("");
         this.JLabel_Ruta_Resumen.setText("");
         this.JText_Ruta_Tiempo.setText("");
         this.JText_Ruta_Distancia.setText("");
         this.JLabel_Ruta_Status.setText(MapsJava.getLastRequestStatus());
         ArrayList<Integer> tiempoTotal=ObjRoute.getTotalTime();
         int tiempoAux=0;
         for(Integer item:tiempoTotal){
             tiempoAux+=item;
         }
         ArrayList<Integer> distanciaTotal=ObjRoute.getTotalDistance();
         int distanciaAux=0;
         for(Integer item:distanciaTotal){
             distanciaAux+=item;
         }
         double tiempo=(double)(tiempoAux);
         tiempo=(tiempo/60)/60;
         tiempo=redondeoDosDecimales(tiempo);
         double distancia=(double)(distanciaAux);
         distancia=distancia/1000;
         this.JLabel_Ruta_Copyright.setText(ObjRoute.getCopyright());
         this.JLabel_Ruta_Resumen.setText(ObjRoute.getSummary());
         this.JText_Ruta_Tiempo.setText(String.valueOf(tiempo));
         this.JText_Ruta_Distancia.setText(String.valueOf(distancia));

    }
     private void crearRuta() throws MalformedURLException, UnsupportedEncodingException{
         if(!JText_Ruta_DirecOrigen.getText().isEmpty() && !JText_Ruta_DirecDestin.getText().isEmpty()){
             ArrayList<String> hitos=new ArrayList<>();
             if(jCheckBox_Ruta_Hito.isSelected() && !JText_Ruta_Hito.getText().isEmpty()){
                 hitos.add(JText_Ruta_Hito.getText());
             }
             String[][] arrayRoute=ObjRoute.getRoute(JText_Ruta_DirecOrigen.getText(), JText_Ruta_DirecDestin.getText(),
                     hitos, Boolean.TRUE,this.seleccionarModoRuta(),this.seleccionarRestricciones());  
             rellenarTablaRuta(arrayRoute);
             rellenarDatosrRuta();
            
         }
    }
    private void guardarCambios(){
         MapsJava.setConnectTimeout(Integer.valueOf(JText_Conexion.getText()));
         MapsJava.setLanguage(JText_idioma.getText());
         MapsJava.setRegion(JText_Region.getText());
         if("true".equals(JCombo_Sensor.getSelectedItem().toString())){
             MapsJava.setSensor(true);
         }else{
             MapsJava.setSensor(false);
         }
         MapsJava.setKey(JText_Clave.getText());
    }
     private void cargarStreetView() throws MalformedURLException, UnsupportedEncodingException{
        if(!JText_SV_Direccion.getText().isEmpty()){
            JLabel_SV_Imagen.setText("");
            Image imagenStreet=ObjStreetView.getStreetView(JText_SV_Direccion.getText(), new Dimension(500,500), 
                    Double.valueOf(JText_SV_horizontal.getText()), Double.valueOf(JText_SV_zoom.getText()),
                    -100);
            if(imagenStreet!=null){
                ImageIcon imgIcon=new ImageIcon(imagenStreet);
                Icon iconImage=(Icon)imgIcon;
                JLabel_SV_Imagen.setIcon(iconImage);
            }
        }
    }
     
     private void crearMapa() throws MalformedURLException, UnsupportedEncodingException{
         if(!JText_ME_Direccion.getText().isEmpty()){
             this.JLabel_ME_Imagen.setText("");
             Image imagenMapa=ObjStaticMaps.getStaticMap(JText_ME_Direccion.getText(),
                     Integer.valueOf(JText_ME_Zoom.getText()),new Dimension(500,500),
                     Integer.valueOf(JText_ME_Escala.getText()),this.seleccionarFormato(),
                     this.seleccionarTipoMapa());
            if(imagenMapa!=null){
                ImageIcon imgIcon=new ImageIcon(imagenMapa);
                Icon iconImage=(Icon)imgIcon;
                JLabel_ME_Imagen.setIcon(iconImage);
            }
         }
     }
     private class MyTableModel extends DefaultTableModel {

         public MyTableModel(Object[][] data, Object[] columnNames) {
             super(data, columnNames);
         }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
                    Class<?> clazz = Object.class;
      Object aux = getValueAt(0, columnIndex);
       if (aux != null) {
        clazz = aux.getClass();
       }

       return clazz;
      }

     }
    private void rellenarPlaces(String[][] resultadoPlaces) throws MalformedURLException, IOException{
        this.JLabel_Pl_Status.setText(MapsJava.getLastRequestStatus());
        if(resultadoPlaces.length>0){
            String[] columnas=new String[6];
            columnas[0]="Place";columnas[1]="Dirección";columnas[2]="Latitud";columnas[3]="Longitud";columnas[4]="Tipo";columnas[5]="Referencia";
            Object[][] obj=new Object[resultadoPlaces.length][resultadoPlaces[0].length];
            for(int i=0; i<obj.length;i++){
                obj[i][0]=resultadoPlaces[i][0].toString();
                obj[i][1]=resultadoPlaces[i][1].toString();
                obj[i][2]=resultadoPlaces[i][2].toString();
                obj[i][3]=resultadoPlaces[i][3].toString();
                Image imageCargada;
                imageCargada=ImageIO.read(new URL(resultadoPlaces[i][4]));
                imageCargada=imageCargada.getScaledInstance(20,20,Image.SCALE_FAST);
                obj[i][4]=new ImageIcon(imageCargada);
                obj[i][5]=resultadoPlaces[i][5].toString();
            }
            TableModel tableModel=new MyTableModel(obj, columnas);
            this.jTable_Pl_places.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.jTable_Pl_places.setModel(tableModel);
            this.jTable_Pl_places.setRowSelectionInterval(0, 0);
            seleccionarReferencia();
        }
    }
    private void borrarTable(JTable jtable){
        jtable.setModel(new DefaultTableModel());
    }
    private void places() throws UnsupportedEncodingException, MalformedURLException, IOException{
        if(!JText_Pl_Direccion.getText().isEmpty()){
            borrarTable(jTable_Pl_places);
            Point2D.Double latLong=ObjGeocoding.getCoordinates(JText_Pl_Direccion.getText());
            if(latLong.x!=0.0 && latLong.y!=0.0){
                String keyword=null;
                if(!JText_Pl_Keyword.getText().isEmpty()){
                    keyword=JText_Pl_Keyword.getText();
                }
                String place=null;
                if(!JText_Pl_Place.getText().isEmpty()){
                    place=JText_Pl_Place.getText();
                }
                ArrayList<String> types=new ArrayList<>();
                if(!"Sin tipo".equals(JCombo_Pl_TipoPlace.getSelectedItem().toString())){
                    types.add(JCombo_Pl_TipoPlace.getSelectedItem().toString());
                }
                Places.Rankby rankby= Places.Rankby.prominence;
                if(!"Importancia".equals(JCombo_Pl_Orden.getSelectedItem().toString())){
                    rankby=Places.Rankby.distance;
                }
                int radio=Integer.valueOf(JText_Pl_Radio.getText());
                String[][] resultado=ObjPlaces.getPlaces(latLong.x, latLong.y,radio,
                        keyword, place,rankby,types);
                rellenarPlaces(resultado);
            }
        }
        
    }
    
    private void abrirFramePlaces(String referenciaPlace) throws UnsupportedEncodingException{
        if(!referenciaPlace.isEmpty()){
            for(UIManager.LookAndFeelInfo laf:UIManager.getInstalledLookAndFeels()){
                if("Nimbus".equals(laf.getName()))
                    try {
                    UIManager.setLookAndFeel(laf.getClassName());
                } catch (Exception ex) {
                }
            }
            PlacesFrame mainF=new PlacesFrame(referenciaPlace);
            mainF.setVisible(true);
      
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        JText_Region = new javax.swing.JTextField();
        JText_idioma = new javax.swing.JTextField();
        JText_Conexion = new javax.swing.JTextField();
        JText_Clave = new javax.swing.JTextField();
        JCombo_Sensor = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        JLabel_Clave = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        JText_CD_Direc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        JText_CD_Lati = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        JText_CD_Long = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        JText_CD_Mostrar = new javax.swing.JButton();
        JText_CD_Buscar = new javax.swing.JButton();
        JLabel_CD_Status = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        JText_CD_DireEnc = new javax.swing.JTextField();
        JText_CD_Resolucion = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        JText_CD_CodigPost = new javax.swing.JTextField();
        JText_CD_Elevacion = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        JText_CI_Lati = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        JText_CI_Long = new javax.swing.JTextField();
        JText_CI_Buscar1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        JText_CI_DireEnc = new javax.swing.JTextField();
        JText_CI_Mostrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_CI_DirEncon = new javax.swing.JList();
        JLabel_CI_Status = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        JText_CI_CodigPost = new javax.swing.JTextField();
        JText_CI_Elevacion = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        JText_CI_Resolucion = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        JText_CD_Buscar1 = new javax.swing.JButton();
        JText_SV_Direccion = new javax.swing.JTextField();
        jSlider1 = new javax.swing.JSlider();
        JText_SV_horizontal = new javax.swing.JTextField();
        jSlider2 = new javax.swing.JSlider();
        JText_SV_zoom = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        JLabel_SV_Imagen = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        JButton_ME_Buscar = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        JText_ME_Direccion = new javax.swing.JTextField();
        JSlider_ME_Escala = new javax.swing.JSlider();
        jLabel29 = new javax.swing.JLabel();
        JText_ME_Escala = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        JCombo_ME_Formato = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        JCombo_ME_TipoMapa = new javax.swing.JComboBox();
        JSlider_ME_Zoom = new javax.swing.JSlider();
        jLabel32 = new javax.swing.JLabel();
        JText_ME_Zoom = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        JLabel_ME_Imagen = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButton_Peticiones1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable_Ruta_Tramos = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        JText_Ruta_DirecOrigen = new javax.swing.JTextField();
        JText_Ruta_DirecDestin = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        JCombo_Ruta_Restricc = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        JCombo_Ruta_Trasnpo = new javax.swing.JComboBox();
        JText_Ruta_Hito = new javax.swing.JTextField();
        jCheckBox_Ruta_Hito = new javax.swing.JCheckBox();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        JText_Ruta_Tiempo = new javax.swing.JTextField();
        JText_Ruta_Distancia = new javax.swing.JTextField();
        JLabel_Ruta_Copyright = new javax.swing.JLabel();
        JLabel_Ruta_Resumen = new javax.swing.JLabel();
        JLabel_Ruta_Status = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable_Pl_places = new javax.swing.JTable();
        JButton_ME_Buscar1 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        JText_Pl_Direccion = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        JSlider_Pl_Radio = new javax.swing.JSlider();
        JText_Pl_Radio = new javax.swing.JTextField();
        JText_Pl_Place = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        JText_Pl_Keyword = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        JCombo_Pl_Orden = new javax.swing.JComboBox();
        JCombo_Pl_TipoPlace = new javax.swing.JComboBox();
        jLabel38 = new javax.swing.JLabel();
        JLabel_Pl_Status = new javax.swing.JLabel();
        JText_Pl_Referencia = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton_Peticiones = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Peticiones = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Mapas");
        setAlwaysOnTop(true);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(146, 178, 206));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(173, 173, 173), new java.awt.Color(224, 224, 224), null, null));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(353, 401));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel4.setToolTipText("");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Región búsquedas");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Idioma resultados");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Tiempo conex (ms)");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Clave API");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Sensor");

        JText_Region.setText("es");

        JText_idioma.setText("es");

        JText_Conexion.setText("300");
        JText_Conexion.setEnabled(false);

        JCombo_Sensor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "false", "true", " " }));

        jButton1.setText("Pegar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Comprobar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(153, 153, 255));
        jButton3.setText("Guardar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Info");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Info");
        jButton5.setToolTipText("");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JText_Conexion, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JCombo_Sensor, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(JText_Region, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(JText_idioma)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(JLabel_Clave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(JText_Clave, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(JText_Region, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(JText_idioma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(JText_Conexion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(JCombo_Sensor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(JText_Clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(JLabel_Clave))
                .addGap(30, 30, 30)
                .addComponent(jButton3)
                .addContainerGap(208, Short.MAX_VALUE))
        );

        jLabel8.getAccessibleContext().setAccessibleName("region");
        jLabel9.getAccessibleContext().setAccessibleName("idioma");
        jLabel10.getAccessibleContext().setAccessibleName("tiempoConex");
        jLabel11.getAccessibleContext().setAccessibleName("claveAPI");
        jLabel12.getAccessibleContext().setAccessibleName("sensor");
        JText_Region.getAccessibleContext().setAccessibleName("region");
        JText_idioma.getAccessibleContext().setAccessibleName("idioma");
        JText_Conexion.getAccessibleContext().setAccessibleName("tiempoConex");
        JText_Clave.getAccessibleContext().setAccessibleName("claveAPI");
        JCombo_Sensor.getAccessibleContext().setAccessibleName("sensorCombo");
        jButton2.getAccessibleContext().setAccessibleName("comprobarClave");
        jButton3.getAccessibleContext().setAccessibleName("guardarProp");
        jButton4.getAccessibleContext().setAccessibleName("infoRegion");
        jButton5.getAccessibleContext().setAccessibleName("infoIdioma");

        jTabbedPane1.addTab("Propiedades", jPanel4);
        jPanel4.getAccessibleContext().setAccessibleName("Principal");

        jLabel1.setText("Dirección");

        jLabel2.setText("Latitud");

        JText_CD_Lati.setEditable(false);

        jLabel3.setText("Longitud");

        JText_CD_Long.setEditable(false);

        jLabel4.setText("Dirección encontrada");

        JText_CD_Mostrar.setBackground(new java.awt.Color(153, 153, 255));
        JText_CD_Mostrar.setText("Mostrar en mapa");
        JText_CD_Mostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JText_CD_MostrarActionPerformed(evt);
            }
        });

        JText_CD_Buscar.setBackground(new java.awt.Color(153, 153, 255));
        JText_CD_Buscar.setText("Buscar");
        JText_CD_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JText_CD_BuscarActionPerformed(evt);
            }
        });

        JLabel_CD_Status.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        JLabel_CD_Status.setName(""); // NOI18N

        jLabel13.setText("Código postal");

        JText_CD_DireEnc.setEditable(false);

        JText_CD_Resolucion.setEditable(false);

        jLabel15.setText("Elevación (m)");

        JText_CD_CodigPost.setEditable(false);

        JText_CD_Elevacion.setEditable(false);

        jLabel16.setText("Resolución (m)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JText_CD_Mostrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JText_CD_Lati, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JText_CD_Long)
                    .addComponent(JText_CD_DireEnc)
                    .addComponent(JText_CD_CodigPost, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JText_CD_Elevacion, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(JText_CD_Resolucion)))
                    .addComponent(JText_CD_Buscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JText_CD_Direc)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)))
                            .addComponent(jLabel1)
                            .addComponent(JLabel_CD_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CD_Direc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CD_Buscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLabel_CD_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CD_Lati, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CD_Long, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CD_DireEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JText_CD_Mostrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CD_CodigPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JText_CD_Elevacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JText_CD_Resolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jLabel1.getAccessibleContext().setAccessibleName("CDdireccion");
        JText_CD_Direc.getAccessibleContext().setAccessibleName("CDdireccion");
        jLabel2.getAccessibleContext().setAccessibleName("CD_latitud");
        JText_CD_Lati.getAccessibleContext().setAccessibleName("CD_latitud");
        jLabel3.getAccessibleContext().setAccessibleName("CD_longitud");
        JText_CD_Long.getAccessibleContext().setAccessibleName("CD_longitud");
        JText_CD_Long.getAccessibleContext().setAccessibleDescription("");
        jLabel4.getAccessibleContext().setAccessibleName("CD_dirEncontr");
        JText_CD_Mostrar.getAccessibleContext().setAccessibleName("mostrarMapa");
        JText_CD_Buscar.getAccessibleContext().setAccessibleName("CDdireccionBuscar");
        JLabel_CD_Status.getAccessibleContext().setAccessibleName("status");
        jLabel13.getAccessibleContext().setAccessibleName("codigoPostal");
        JText_CD_DireEnc.getAccessibleContext().setAccessibleName("CD_dirEncontr");
        JText_CD_Resolucion.getAccessibleContext().setAccessibleName("resolucion");
        jLabel15.getAccessibleContext().setAccessibleName("elevacion");
        JText_CD_CodigPost.getAccessibleContext().setAccessibleName("codigoPostal");
        JText_CD_Elevacion.getAccessibleContext().setAccessibleName("elevacion");
        jLabel16.getAccessibleContext().setAccessibleName("resolucion");

        jTabbedPane1.addTab("Cod. geo.", jPanel1);
        jPanel1.getAccessibleContext().setAccessibleName("Principal");

        jLabel5.setText("Latitud");

        jLabel6.setText("Longitud");

        JText_CI_Buscar1.setBackground(new java.awt.Color(153, 153, 255));
        JText_CI_Buscar1.setText("Buscar");
        JText_CI_Buscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JText_CI_Buscar1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Dirección encontrada");

        JText_CI_DireEnc.setEditable(false);

        JText_CI_Mostrar.setBackground(new java.awt.Color(153, 153, 255));
        JText_CI_Mostrar.setText("Mostrar en mapa");
        JText_CI_Mostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JText_CI_MostrarActionPerformed(evt);
            }
        });

        jList_CI_DirEncon.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_CI_DirEnconValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList_CI_DirEncon);
        jList_CI_DirEncon.getAccessibleContext().setAccessibleName("CI_masResul");

        JLabel_CI_Status.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        JLabel_CI_Status.setName(""); // NOI18N

        jLabel14.setText("Código postal");

        JText_CI_CodigPost.setEditable(false);

        JText_CI_Elevacion.setEditable(false);

        jLabel17.setText("Elevación (m)");

        jLabel18.setText("Resolución (m)");

        JText_CI_Resolucion.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JText_CI_DireEnc)
                    .addComponent(JText_CI_Mostrar, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addComponent(JText_CI_CodigPost, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JText_CI_Elevacion, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(JText_CI_Resolucion)))
                    .addComponent(JText_CI_Lati, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JText_CI_Long)
                    .addComponent(JText_CI_Buscar1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLabel_CI_Status)
                            .addComponent(jLabel7)
                            .addComponent(jLabel14)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CI_Lati, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CI_Long, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CI_Buscar1)
                .addGap(18, 18, 18)
                .addComponent(JLabel_CI_Status)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CI_DireEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JText_CI_Mostrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_CI_CodigPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JText_CI_Elevacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JText_CI_Resolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(96, 96, 96))
        );

        jLabel5.getAccessibleContext().setAccessibleName("CI_latitud");
        JText_CI_Lati.getAccessibleContext().setAccessibleName("CI_latitud");
        jLabel6.getAccessibleContext().setAccessibleName("CI_longitud");
        JText_CI_Long.getAccessibleContext().setAccessibleName("CI_longitud");
        JText_CI_Buscar1.getAccessibleContext().setAccessibleName("CI_buscar");
        jLabel7.getAccessibleContext().setAccessibleName("CI_dirEnco");
        JText_CI_DireEnc.getAccessibleContext().setAccessibleName("CI_dirEnco");
        JText_CI_Mostrar.getAccessibleContext().setAccessibleName("mostrarMapa");
        jScrollPane1.getAccessibleContext().setAccessibleName("CI_masResul");
        JLabel_CI_Status.getAccessibleContext().setAccessibleName("status");
        JLabel_CI_Status.getAccessibleContext().setAccessibleDescription("");
        jLabel14.getAccessibleContext().setAccessibleName("codigoPostal");
        JText_CI_CodigPost.getAccessibleContext().setAccessibleName("codigoPostal");
        JText_CI_Elevacion.getAccessibleContext().setAccessibleName("elevacion");
        jLabel17.getAccessibleContext().setAccessibleName("elevacion");
        jLabel18.getAccessibleContext().setAccessibleName("resolucion");
        JText_CI_Resolucion.getAccessibleContext().setAccessibleName("resolucion");

        jTabbedPane1.addTab("Cod. geo. inver.", jPanel2);
        jPanel2.getAccessibleContext().setAccessibleName("Principal");

        jLabel23.setText("Dirección");

        JText_CD_Buscar1.setBackground(new java.awt.Color(153, 153, 255));
        JText_CD_Buscar1.setText("Buscar");
        JText_CD_Buscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JText_CD_Buscar1ActionPerformed(evt);
            }
        });

        jSlider1.setMaximum(360);
        jSlider1.setValue(180);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        JText_SV_horizontal.setText("180");

        jSlider2.setMaximum(120);
        jSlider2.setMinimum(10);
        jSlider2.setValue(90);
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        JText_SV_zoom.setText("90");
        JText_SV_zoom.setToolTipText("");

        jLabel24.setText("Horizontal");

        jLabel27.setText("Zoom");

        JLabel_SV_Imagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabel_SV_Imagen.setText("Imagen StreetView");
        jScrollPane4.setViewportView(JLabel_SV_Imagen);
        JLabel_SV_Imagen.getAccessibleContext().setAccessibleName("imagenSV");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JText_SV_Direccion)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JText_SV_horizontal, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JText_SV_zoom, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(JText_CD_Buscar1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_SV_Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(JText_SV_horizontal)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(JText_SV_zoom)
                        .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JText_CD_Buscar1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel23.getAccessibleContext().setAccessibleName("direccionSV");
        JText_CD_Buscar1.getAccessibleContext().setAccessibleName("direccionSV");
        JText_SV_Direccion.getAccessibleContext().setAccessibleName("direccionSV");
        jSlider1.getAccessibleContext().setAccessibleName("horizontalSV");
        JText_SV_horizontal.getAccessibleContext().setAccessibleName("horizontalSV");
        jSlider2.getAccessibleContext().setAccessibleName("zoomSV");
        JText_SV_zoom.getAccessibleContext().setAccessibleName("zoomSV");
        jLabel24.getAccessibleContext().setAccessibleName("horizontalSV");
        jLabel27.getAccessibleContext().setAccessibleName("zoomSV");
        jScrollPane4.getAccessibleContext().setAccessibleName("imagenSV");

        jTabbedPane1.addTab("StreetView", jPanel7);
        jPanel7.getAccessibleContext().setAccessibleName("Principal");

        JButton_ME_Buscar.setBackground(new java.awt.Color(153, 153, 255));
        JButton_ME_Buscar.setText("Crear mapa");
        JButton_ME_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton_ME_BuscarActionPerformed(evt);
            }
        });

        jLabel28.setText("Centro del mapa");

        JSlider_ME_Escala.setMaximum(2);
        JSlider_ME_Escala.setMinimum(1);
        JSlider_ME_Escala.setValue(1);
        JSlider_ME_Escala.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                JSlider_ME_EscalaStateChanged(evt);
            }
        });

        jLabel29.setText("Escala");

        JText_ME_Escala.setText("1");

        jLabel30.setText("Formato");

        JCombo_ME_Formato.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "png", "png32", "gif", "jpg", "jpg_baseline" }));

        jLabel31.setText("Tipo de mapa");

        JCombo_ME_TipoMapa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "roadmap", "satellite", "hybrid", "terrain" }));

        JSlider_ME_Zoom.setMaximum(20);
        JSlider_ME_Zoom.setMinimum(1);
        JSlider_ME_Zoom.setValue(14);
        JSlider_ME_Zoom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                JSlider_ME_ZoomStateChanged(evt);
            }
        });

        jLabel32.setText("Zoom");

        JText_ME_Zoom.setText("14");

        JLabel_ME_Imagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabel_ME_Imagen.setText("Mapa estático");
        jScrollPane5.setViewportView(JLabel_ME_Imagen);
        JLabel_ME_Imagen.getAccessibleContext().setAccessibleName("mapaEstaticoME");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JCombo_ME_Formato, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JCombo_ME_TipoMapa, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(JSlider_ME_Escala, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(JText_ME_Escala, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel29))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(JSlider_ME_Zoom, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(JText_ME_Zoom, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel32))))
                        .addGap(0, 47, Short.MAX_VALUE))
                    .addComponent(JText_ME_Direccion)
                    .addComponent(jScrollPane5)
                    .addComponent(JButton_ME_Buscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_ME_Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JText_ME_Escala)
                            .addComponent(JSlider_ME_Escala, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JSlider_ME_Zoom, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JText_ME_Zoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCombo_ME_TipoMapa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCombo_ME_Formato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JButton_ME_Buscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
        );

        JButton_ME_Buscar.getAccessibleContext().setAccessibleName("crearMapaME");
        jLabel28.getAccessibleContext().setAccessibleName("direccionME");
        JText_ME_Direccion.getAccessibleContext().setAccessibleName("direccionME");
        JSlider_ME_Escala.getAccessibleContext().setAccessibleName("escalaME");
        jLabel29.getAccessibleContext().setAccessibleName("escalaME");
        JText_ME_Escala.getAccessibleContext().setAccessibleName("escalaME");
        jLabel30.getAccessibleContext().setAccessibleName("formatoME");
        JCombo_ME_Formato.getAccessibleContext().setAccessibleName("formatoME");
        jLabel31.getAccessibleContext().setAccessibleName("tipoMapaME");
        JCombo_ME_TipoMapa.getAccessibleContext().setAccessibleName("tipoMapaME");
        JSlider_ME_Zoom.getAccessibleContext().setAccessibleName("zoomME");
        jLabel32.getAccessibleContext().setAccessibleName("zoomME");
        JText_ME_Zoom.getAccessibleContext().setAccessibleName("zoomME");
        jScrollPane5.getAccessibleContext().setAccessibleName("mapaEstaticoME");

        jTabbedPane1.addTab("Mapa estático", jPanel8);
        jPanel8.getAccessibleContext().setAccessibleName("Principal");

        jButton_Peticiones1.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Peticiones1.setText("Calcular");
        jButton_Peticiones1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Peticiones1ActionPerformed(evt);
            }
        });

        jTable_Ruta_Tramos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Duración tramo", "Distancia tramo", "Indicaciones", "Latitud", "Longitud"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable_Ruta_Tramos);
        jTable_Ruta_Tramos.getAccessibleContext().setAccessibleName("indicacionesRuta");

        jLabel19.setText("Dirección origen");

        jLabel20.setText("Dirección destino");

        jLabel21.setText("Restricciones carretera");

        JCombo_Ruta_Restricc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ninguna", "Peajes", "Autopistas/autovías" }));

        jLabel22.setText("Tipo transporte");

        JCombo_Ruta_Trasnpo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Coche", "Andando", "Bicicleta" }));

        jCheckBox_Ruta_Hito.setText("Hito intermedio");

        jLabel25.setText("Tiempo total (horas)");

        jLabel26.setText("Distancia total (km)");

        JText_Ruta_Tiempo.setEditable(false);

        JText_Ruta_Distancia.setEditable(false);

        JLabel_Ruta_Copyright.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        JLabel_Ruta_Copyright.setText("Copyright");

        JLabel_Ruta_Resumen.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        JLabel_Ruta_Resumen.setText("Resumen");

        JLabel_Ruta_Status.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_Peticiones1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addComponent(JText_Ruta_DirecOrigen)
                    .addComponent(JText_Ruta_DirecDestin)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jCheckBox_Ruta_Hito)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JText_Ruta_Hito, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JCombo_Ruta_Restricc, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JCombo_Ruta_Trasnpo, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JText_Ruta_Tiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JText_Ruta_Distancia, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(JLabel_Ruta_Copyright)
                            .addComponent(JLabel_Ruta_Resumen)
                            .addComponent(JLabel_Ruta_Status))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_Ruta_DirecOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_Ruta_DirecDestin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCombo_Ruta_Trasnpo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCombo_Ruta_Restricc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox_Ruta_Hito)
                    .addComponent(JText_Ruta_Hito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(JLabel_Ruta_Status)
                .addGap(1, 1, 1)
                .addComponent(jButton_Peticiones1)
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JText_Ruta_Tiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JText_Ruta_Distancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JLabel_Ruta_Copyright)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLabel_Ruta_Resumen)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton_Peticiones1.getAccessibleContext().setAccessibleName("calculaRuta");
        jLabel19.getAccessibleContext().setAccessibleName("direccionOrigen");
        JText_Ruta_DirecOrigen.getAccessibleContext().setAccessibleName("direccionOrigen");
        JText_Ruta_DirecDestin.getAccessibleContext().setAccessibleName("direccionDestino");
        jLabel20.getAccessibleContext().setAccessibleName("direccionDestino");
        jLabel21.getAccessibleContext().setAccessibleName("restriccionCarreteras");
        JCombo_Ruta_Restricc.getAccessibleContext().setAccessibleName("restriccionCarreteras");
        jLabel22.getAccessibleContext().setAccessibleName("tipoTransporte");
        JCombo_Ruta_Trasnpo.getAccessibleContext().setAccessibleName("tipoTransporte");
        JText_Ruta_Hito.getAccessibleContext().setAccessibleName("hito");
        jCheckBox_Ruta_Hito.getAccessibleContext().setAccessibleName("hito");
        jLabel25.getAccessibleContext().setAccessibleName("tiempoTotal");
        jLabel26.getAccessibleContext().setAccessibleName("distanciaTotal");
        JText_Ruta_Tiempo.getAccessibleContext().setAccessibleName("tiempoTotal");
        JText_Ruta_Distancia.getAccessibleContext().setAccessibleName("distanciaTotal");
        JLabel_Ruta_Copyright.getAccessibleContext().setAccessibleName("copyright");
        JLabel_Ruta_Resumen.getAccessibleContext().setAccessibleName("resumen");
        JLabel_Ruta_Status.getAccessibleContext().setAccessibleName("status");

        jTabbedPane1.addTab("Ruta", jPanel6);
        jPanel6.getAccessibleContext().setAccessibleName("Principal");

        jTable_Pl_places.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Place", "Dirección", "Latitud", "Longitud", "Tipo", "Referencia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_Pl_places.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable_Pl_placesMousePressed(evt);
            }
        });
        jScrollPane6.setViewportView(jTable_Pl_places);
        jTable_Pl_places.getAccessibleContext().setAccessibleName("placesEncPL");

        JButton_ME_Buscar1.setBackground(new java.awt.Color(153, 153, 255));
        JButton_ME_Buscar1.setText("Buscar");
        JButton_ME_Buscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton_ME_Buscar1ActionPerformed(evt);
            }
        });

        jLabel33.setText("Centro de búsqueda (*)");

        jLabel34.setText("Radio (*)");

        JSlider_Pl_Radio.setMaximum(5000);
        JSlider_Pl_Radio.setMinimum(1);
        JSlider_Pl_Radio.setValue(3000);
        JSlider_Pl_Radio.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                JSlider_Pl_RadioStateChanged(evt);
            }
        });

        JText_Pl_Radio.setText("3000");

        jLabel35.setText("Place");

        jLabel36.setText("Keyword");

        jLabel37.setText("Orden resultados");

        JCombo_Pl_Orden.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Importancia", "Distancia" }));

        JCombo_Pl_TipoPlace.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sin tipo", "accounting", "airport", "amusement_park", "aquarium", "art_gallery", "atm", "bakery", "bank", "bar", "beauty_salon", "bicycle_store", "book_store", "bowling_alley", "bus_station", "cafe", "campground", "car_dealer", "car_rental", "car_repair", "car_wash", "casino", "cemetery", "church", "city_hall", "clothing_store", "convenience_store", "courthouse", "dentist", "department_store", "doctor", "electrician", "electronics_store", "embassy", "establishment", "finance", "fire_station", "florist", "food", "funeral_home", "furniture_store", "gas_station", "general_contractor", "grocery_or_supermarket", "gym", "hair_care", "hardware_store", "health", "hindu_temple", "home_goods_store", "hospital", "insurance_agency", "jewelry_store", "laundry", "lawyer", "library", "liquor_store", "local_government_office", "locksmith", "lodging", "meal_delivery", "meal_takeaway", "mosque", "movie_rental", "movie_theater", "moving_company", "museum", "night_club", "painter", "park", "parking", "pet_store", "pharmacy", "physiotherapist", "place_of_worship", "plumber", "police", "post_office", "real_estate_agency", "restaurant", "roofing_contractor", "rv_park", "school", "shoe_store", "shopping_mall", "spa", "stadium", "storage", "store", "subway_station", "synagogue", "taxi_stand", "train_station", "travel_agency", "university", "veterinary_care", "zoo" }));

        jLabel38.setText("Tipo place");

        JLabel_Pl_Status.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N

        JText_Pl_Referencia.setEditable(false);

        jLabel39.setText("Referencia");

        jButton6.setBackground(new java.awt.Color(153, 153, 255));
        jButton6.setText("Ver local");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addComponent(JButton_ME_Buscar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JText_Pl_Direccion)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(JCombo_Pl_TipoPlace, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addComponent(JSlider_Pl_Radio, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JText_Pl_Radio, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JCombo_Pl_Orden, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JText_Pl_Keyword, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(JText_Pl_Place)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel35))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(JLabel_Pl_Status))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JText_Pl_Referencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JText_Pl_Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JText_Pl_Radio)
                            .addComponent(JSlider_Pl_Radio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JText_Pl_Place, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JText_Pl_Keyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCombo_Pl_Orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JCombo_Pl_TipoPlace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JLabel_Pl_Status)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButton_ME_Buscar1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JText_Pl_Referencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39)
                    .addComponent(jButton6))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        JButton_ME_Buscar1.getAccessibleContext().setAccessibleName("BuscarPL");
        jLabel33.getAccessibleContext().setAccessibleName("centroPL");
        JText_Pl_Direccion.getAccessibleContext().setAccessibleName("centroPL");
        jLabel34.getAccessibleContext().setAccessibleName("radioPL");
        JSlider_Pl_Radio.getAccessibleContext().setAccessibleName("radioPL");
        JText_Pl_Radio.getAccessibleContext().setAccessibleName("radioPL");
        JText_Pl_Place.getAccessibleContext().setAccessibleName("placePL");
        jLabel35.getAccessibleContext().setAccessibleName("placePL");
        JText_Pl_Keyword.getAccessibleContext().setAccessibleName("keyPL");
        jLabel36.getAccessibleContext().setAccessibleName("keyPL");
        jLabel37.getAccessibleContext().setAccessibleName("ordenPL");
        JCombo_Pl_Orden.getAccessibleContext().setAccessibleName("ordenPL");
        JCombo_Pl_TipoPlace.getAccessibleContext().setAccessibleName("tipoPL");
        jLabel38.getAccessibleContext().setAccessibleName("tipoPL");
        JLabel_Pl_Status.getAccessibleContext().setAccessibleName("status");
        JText_Pl_Referencia.getAccessibleContext().setAccessibleName("placesReferencia");
        jLabel39.getAccessibleContext().setAccessibleName("placesReferencia");
        jButton6.getAccessibleContext().setAccessibleName("placesReferenciaBoton");

        jTabbedPane1.addTab("Places", jPanel9);
        jPanel9.getAccessibleContext().setAccessibleName("Principal");

        jButton_Peticiones.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Peticiones.setText("Mostrar peticiones");
        jButton_Peticiones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_PeticionesActionPerformed(evt);
            }
        });

        jTable_Peticiones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Número", "Hora", "Status", "URL", "Información", "Excepción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable_Peticiones);
        jTable_Peticiones.getAccessibleContext().setAccessibleName("peticiones");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton_Peticiones, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jButton_Peticiones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton_Peticiones.getAccessibleContext().setAccessibleName("mostrPeticiones");

        jTabbedPane1.addTab("Peticiones", jPanel3);
        jPanel3.getAccessibleContext().setAccessibleName("Principal");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Principal");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.JText_CD_Direc.requestFocus();
    }//GEN-LAST:event_formWindowOpened

    private void JText_CD_MostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JText_CD_MostrarActionPerformed
        if(!JText_CD_Lati.getText().isEmpty() && !JText_CD_Long.getText().isEmpty()){
            try {
                this.mostrarMapa(Double.valueOf(JText_CD_Lati.getText()),Double.valueOf(JText_CD_Long.getText()));
            } catch (Exception ex) {
        }  
        }
        
    }//GEN-LAST:event_JText_CD_MostrarActionPerformed

    private void JText_CI_MostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JText_CI_MostrarActionPerformed
        if(!JText_CI_DireEnc.getText().isEmpty()){
            try {
                this.mostrarMapa(JText_CI_DireEnc.getText());
            } catch (Exception ex) {
        }  
        }
    }//GEN-LAST:event_JText_CI_MostrarActionPerformed

    private void JText_CD_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JText_CD_BuscarActionPerformed
        try {
            this.CodiGeografica();
            this.comprobarStatus(JLabel_CD_Status);
            this.elevacionCD(this.JText_CD_Lati,this.JText_CD_Long);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_JText_CD_BuscarActionPerformed

    private void JText_CI_Buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JText_CI_Buscar1ActionPerformed
        try {
            CodiGeograficaInver();
            this.comprobarStatus(JLabel_CI_Status);
            this.elevacionCI(this.JText_CI_Lati,this.JText_CI_Long);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_JText_CI_Buscar1ActionPerformed

    private void jList_CI_DirEnconValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_CI_DirEnconValueChanged
        seleccionarItemList();
    }//GEN-LAST:event_jList_CI_DirEnconValueChanged

    private void jButton_PeticionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PeticionesActionPerformed
        rellenarPeticiones();
    }//GEN-LAST:event_jButton_PeticionesActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      guardarCambios();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            pegarTexto();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if(jTabbedPane1.getSelectedIndex()==0){
            actualizarPropiedades();
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {        
            Desktop.getDesktop().browse(new URI("http://es.wikipedia.org/wiki/CcTLD"));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {        
            Desktop.getDesktop().browse(new URI("https://spreadsheets.google.com/pub?key=p9pdwsai2hDMsLkXsoM05KQ&gid=1"));
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton_Peticiones1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Peticiones1ActionPerformed
        try {
            crearRuta();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton_Peticiones1ActionPerformed
   
    private void JText_CD_Buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JText_CD_Buscar1ActionPerformed
        try {
            cargarStreetView();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_JText_CD_Buscar1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        this.JText_SV_horizontal.setText(String.valueOf(jSlider1.getValue()));
    }//GEN-LAST:event_jSlider1StateChanged

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        this.JText_SV_zoom.setText(String.valueOf(jSlider2.getValue()));
    }//GEN-LAST:event_jSlider2StateChanged
    
    private void JButton_ME_Buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton_ME_Buscar1ActionPerformed
        if(!MapsJava.getKey().isEmpty()){
        try {
            places();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        }else{
            JOptionPane.showMessageDialog(null,"Esta función requiere clave de desarrollador", 
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_JButton_ME_Buscar1ActionPerformed

    private void JSlider_Pl_RadioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_JSlider_Pl_RadioStateChanged
        this.JText_Pl_Radio.setText(String.valueOf(JSlider_Pl_Radio.getValue()));
    }//GEN-LAST:event_JSlider_Pl_RadioStateChanged

    private void seleccionarReferencia(){
        if(jTable_Pl_places.getRowCount()>0){
          this.JText_Pl_Referencia.setText((String)jTable_Pl_places.getValueAt(jTable_Pl_places.getSelectedRow(),5));
        }
    }
    private void jTable_Pl_placesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_Pl_placesMousePressed
         seleccionarReferencia();
    }//GEN-LAST:event_jTable_Pl_placesMousePressed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
             abrirFramePlaces(JText_Pl_Referencia.getText());
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton6ActionPerformed
  
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        comprobarClaveApi();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void JSlider_ME_ZoomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_JSlider_ME_ZoomStateChanged
        this.JText_ME_Zoom.setText(String.valueOf(JSlider_ME_Zoom.getValue()));
    }//GEN-LAST:event_JSlider_ME_ZoomStateChanged

    private void JSlider_ME_EscalaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_JSlider_ME_EscalaStateChanged
        this.JText_ME_Escala.setText(String.valueOf(JSlider_ME_Escala.getValue()));
    }//GEN-LAST:event_JSlider_ME_EscalaStateChanged

    private void JButton_ME_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton_ME_BuscarActionPerformed
        try {
            this.crearMapa();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_JButton_ME_BuscarActionPerformed

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButton_ME_Buscar;
    private javax.swing.JButton JButton_ME_Buscar1;
    private javax.swing.JComboBox JCombo_ME_Formato;
    private javax.swing.JComboBox JCombo_ME_TipoMapa;
    private javax.swing.JComboBox JCombo_Pl_Orden;
    private javax.swing.JComboBox JCombo_Pl_TipoPlace;
    private javax.swing.JComboBox JCombo_Ruta_Restricc;
    private javax.swing.JComboBox JCombo_Ruta_Trasnpo;
    private javax.swing.JComboBox JCombo_Sensor;
    private javax.swing.JLabel JLabel_CD_Status;
    private javax.swing.JLabel JLabel_CI_Status;
    private javax.swing.JLabel JLabel_Clave;
    private javax.swing.JLabel JLabel_ME_Imagen;
    private javax.swing.JLabel JLabel_Pl_Status;
    private javax.swing.JLabel JLabel_Ruta_Copyright;
    private javax.swing.JLabel JLabel_Ruta_Resumen;
    private javax.swing.JLabel JLabel_Ruta_Status;
    private javax.swing.JLabel JLabel_SV_Imagen;
    private javax.swing.JSlider JSlider_ME_Escala;
    private javax.swing.JSlider JSlider_ME_Zoom;
    private javax.swing.JSlider JSlider_Pl_Radio;
    private javax.swing.JButton JText_CD_Buscar;
    private javax.swing.JButton JText_CD_Buscar1;
    private javax.swing.JTextField JText_CD_CodigPost;
    private javax.swing.JTextField JText_CD_DireEnc;
    private javax.swing.JTextField JText_CD_Direc;
    private javax.swing.JTextField JText_CD_Elevacion;
    private javax.swing.JTextField JText_CD_Lati;
    private javax.swing.JTextField JText_CD_Long;
    private javax.swing.JButton JText_CD_Mostrar;
    private javax.swing.JTextField JText_CD_Resolucion;
    private javax.swing.JButton JText_CI_Buscar1;
    private javax.swing.JTextField JText_CI_CodigPost;
    private javax.swing.JTextField JText_CI_DireEnc;
    private javax.swing.JTextField JText_CI_Elevacion;
    private javax.swing.JTextField JText_CI_Lati;
    private javax.swing.JTextField JText_CI_Long;
    private javax.swing.JButton JText_CI_Mostrar;
    private javax.swing.JTextField JText_CI_Resolucion;
    private javax.swing.JTextField JText_Clave;
    private javax.swing.JTextField JText_Conexion;
    private javax.swing.JTextField JText_ME_Direccion;
    private javax.swing.JTextField JText_ME_Escala;
    private javax.swing.JTextField JText_ME_Zoom;
    private javax.swing.JTextField JText_Pl_Direccion;
    private javax.swing.JTextField JText_Pl_Keyword;
    private javax.swing.JTextField JText_Pl_Place;
    private javax.swing.JTextField JText_Pl_Radio;
    private javax.swing.JTextField JText_Pl_Referencia;
    private javax.swing.JTextField JText_Region;
    private javax.swing.JTextField JText_Ruta_DirecDestin;
    private javax.swing.JTextField JText_Ruta_DirecOrigen;
    private javax.swing.JTextField JText_Ruta_Distancia;
    private javax.swing.JTextField JText_Ruta_Hito;
    private javax.swing.JTextField JText_Ruta_Tiempo;
    private javax.swing.JTextField JText_SV_Direccion;
    private javax.swing.JTextField JText_SV_horizontal;
    private javax.swing.JTextField JText_SV_zoom;
    private javax.swing.JTextField JText_idioma;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton_Peticiones;
    private javax.swing.JButton jButton_Peticiones1;
    private javax.swing.JCheckBox jCheckBox_Ruta_Hito;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList_CI_DirEncon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable_Peticiones;
    private javax.swing.JTable jTable_Pl_places;
    private javax.swing.JTable jTable_Ruta_Tramos;
    // End of variables declaration//GEN-END:variables
}
