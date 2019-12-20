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
 * El límite de mapas estáticos es de 25.000 solicitudes gratuitas diarias
 * @author Luis Marcos
 */
public class StaticMaps extends MapsJava {
    private final String URLRoot="http://maps.googleapis.com/maps/api/staticmap";
    
    /**
     * Establece el tipo de formato del mapa estático. Sus posisbles valores son:<br/>
     * <b>png:</b> especifica el formato PNG de 8 bits.<br/>
     * <b>png32:</b> especifica el formato PNG de 32 bits.<br/>
     * <b>gif:</b> especifica el formato GIF.<br/>
     * <b>jpg:</b> especifica el formato de compresión JPEG.<br/>
     * <b>jpg_baseline:</b> especifica un formato de compresión JPEG no progresivo.
     */
    public enum Format{png,png32,gif,jpg,jpg_baseline}
    
    /**
     * Define el tipo de mapa que se va a generar. Sus posibles valores son:<br/>
     * <b>roadmap:</b> especifica una imagen de mapa de carreteras estándar, como se muestra habitualmente en la página de Google Maps.<br/>
     * <b>satellite:</b> especifica una imagen obtenida por satélite.<br/>
     * <b>hybrid:</b> especifica una imagen de mapa de relieve físico, en la que aparece relieve y vegetación.<br/>
     * <b>terrain:</b> especifica un híbrido de imagen obtenida por satélite e imagen de mapa de carreteras, en la que aparece una capa 
     * transparente de calles principales y nombres de lugares en la imagen obtenida por satélite.
     */
    public enum Maptype{roadmap, satellite, hybrid,terrain}
    
    @Override
    protected void onError(URL urlRequest, String status, Exception ex) {
        super.storageRequest(urlRequest.toString(), "Static maps request", "NO STATUS", ex);
    }

    @Override
    protected String getStatus(XPath xpath, Document document) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void storeInfoRequest(URL urlRequest, String info, String status, Exception exception) {
        super.storageRequest(urlRequest.toString(), "Static maps request", "OK", exception);
    }
    
    /**
     * Crea un mapa estático con las características indicadas en los diferentes parámetros de la función
     * @param centerAddress centro del mapa que se creará (como "dirección postal" o "latitud,longitud")
     * @param zoom define el nivel de zoom del mapa, que determina su nivel de ampliación. Valores 0 muestran
     * todo el planeta y mayores que 21 muestran edificios individuales
     * @param size tamaño del mapa que se generará
     * @param scale afecta al número de píxeles que se muestran. Los dos valores posibles son 1 o 2
     * @param format define el formato de la imagen resultante
     * @param maptype define el tipo de mapa que se va a generar
     * @return devuelve una Image con el mapa estático asociado.<br/>
     * En caso de error devuelve null
     * @see Format
     * @see Maptype
     */
    public Image getStaticMap(String centerAddress,int zoom,Dimension size,int scale,Format format, Maptype maptype) throws MalformedURLException, UnsupportedEncodingException{
        URL url=new URL(URLRoot + "?center=" + URLEncoder.encode(centerAddress, "utf-8") + "&zoom=" + zoom +
                "&size=" + size.width + "x" + size.height + "&scale=" + scale +
                "&format=" + format.toString() + "&maptype=" + maptype.toString() + 
                "&markers=" + URLEncoder.encode(centerAddress, "utf-8") + super.getSelectPropertiesRequest());
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
