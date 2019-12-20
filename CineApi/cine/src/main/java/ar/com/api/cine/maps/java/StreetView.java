package ar.com.api.cine.maps.java;

import java.awt.Dimension;
import java.awt.Image;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import javax.xml.xpath.XPath;
import org.w3c.dom.Document;

/**
 * El límite de imágenes estáticas Street View es de 25.000 solicitudes cada 24 horas.
 * @author Luis Marcos
 */
public class StreetView extends MapsJava {
    private final String URLRoot="http://maps.googleapis.com/maps/api/streetview";
    
    @Override
    protected void onError(URL urlRequest, String status, Exception ex) {
        super.storageRequest(urlRequest.toString(), "Elevation request", "NO STATUS", ex);
    }

    @Override
    protected String getStatus(XPath xpath, Document document) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void storeInfoRequest(URL urlRequest, String info, String status, Exception exception) {
        super.storageRequest(urlRequest.toString(), "StreetView request", "OK", exception);
    }
    
    private URL createURL(String address,Dimension sizeImage,double heading,double fov,double pitch) throws MalformedURLException, UnsupportedEncodingException{
        String _heading="&heading=" + heading;
        String _fov="&fov=" + fov;
        String _pitch="&pitch=" + pitch;
        
        if(heading==-1){
            _heading="";
        }
        if(fov==-1){
            _fov="";
        }
        if(pitch==-100){
            _pitch="";
        }
        
        URL urlReturn=new URL(URLRoot + "?size=" + sizeImage.width + "x" + sizeImage.height + 
                "&location=" + URLEncoder.encode(address, "utf-8") + _heading + _pitch + _fov + super.getSelectPropertiesRequest());
        return urlReturn;
    }
    
    /**
     * Esta función permite insertar crear una imagen de Street View estática (no interactiva), indicando a través
     * de sus diferentes parámetros las diferentes características de la misma. 
     * @param address dirección de la imagen que se utilizará para crear la imagen Street View más
     * cercana a la posición indicada (como "dirección postal" o "latitud,longitud")
     * @param sizeImage tamaño de la imagen creada. El máximo es de 640x640
     * @param heading indica el encabezado de la brújula de la cámara. Se admiten valores comprendidos entre 0 y 360.
     * En caso de que se quiera utilizar el valor predeterminado, se debe pasar como parámetro -1
     * @param fov determina el campo horizontal de visión de la imagen. Este campo de visión se expresa en grados, con un valor permitido máximo de 120.
     * En caso de que se quiera utilizar el valor predeterminado, se debe pasar como parámetro -1
     * @param pitch especifica la inclinación del ángulo de la cámara hacia arriba o hacia abajo en relación con el vehículo de Street View.
     * En caso de que se quiera utilizar el valor predeterminado, se debe pasar como parámetro -100
     * @return devuelve la imagen de Street View asociada.
     * En caso de error, devuelve null
     */
    public Image getStreetView(String address,Dimension sizeImage,double heading,double fov,double pitch) throws MalformedURLException, UnsupportedEncodingException{
        URL url=createURL(address,sizeImage,heading,fov,pitch);
        try {
            Image imageReturn;
            imageReturn=ImageIO.read(url);
            storeInfoRequest(url,null,null,null);
            return imageReturn;
        } catch (Exception e) {
            onError(url, "NO STATUS", e);
            return null;
        }
      
        
    }
    
     
    
}
