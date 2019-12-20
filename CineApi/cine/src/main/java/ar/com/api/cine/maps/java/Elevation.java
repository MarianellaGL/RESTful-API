package ar.com.api.cine.maps.java;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * El uso del API de elevación de Google está sujeto a un límite de 2.500 solicitudes al día.<br/>
 * En cada solicitud, puedes consultar la elevación de un máximo de 512 ubicaciones, pero no puedes superar el límite de 25.000 ubicaciones totales al día.<br/>
 * El API de elevación solo se puede utilizar en combinación con la presentación de resultados en un mapa de Google. 
 * @author Luis
 * @see MapsJava
 */
public class Elevation extends MapsJava {

    private final String URLRoot="http://maps.googleapis.com/maps/api/elevation/xml";
    private final String pathStatus="ElevationResponse/status";
    
    private double resolution;
    private ArrayList<Double> resolutionList=new ArrayList<>();

    /**
     * Indica la distancia máxima (en metros) entre los puntos de datos desde los que se interpoló la elevación.
     * <b>REQUIERE PRIMERAMENTE HACER PETICIÓN DE ELEVACIÓN.</b>
     * @return devuelve la/s resolucion/es en metros de la última petición de elevación.
     * En caso de error, devuelve un ArrayList vacío.
     * @see Elevation#elevation(java.util.ArrayList) 
     */
    public ArrayList<Double> getResolutionList() {
        return resolutionList;
    }
   
    /**
     * Indica la distancia máxima (en metros) entre los puntos de datos desde los que se interpoló la elevación.
     * <b>REQUIERE PRIMERAMENTE HACER PETICIÓN DE ELEVACIÓN.</b>
     * @return devuelve la resolución en metros de la última petición de elevación.
     * En caso de error devuelve 0.0
     * @see Elevation#elevation(double, double) 
     */
    public double getResolution() {
        return resolution;
    }
    
    private URL createURL(double latitude, double longitude) throws MalformedURLException{
        URL urlReturn=new URL(URLRoot + "?locations=" + latitude + "," + longitude + super.getSelectPropertiesRequest());
        return urlReturn;
    }
    private URL createURL(ArrayList<Double> LatiLong) throws MalformedURLException{
        String locations="";
        for (int i = 0; i < LatiLong.size(); i+=2) {
            locations+=String.valueOf(LatiLong.get(i)) + "," + String.valueOf(LatiLong.get(i+1)) + "|";
        }
        locations=locations.substring(0, locations.length()-1);
        URL urlReturn=new URL(URLRoot + "?locations=" + locations + super.getSelectPropertiesRequest());
        return urlReturn;
    }
    
    @Override
    protected void onError(URL urlRequest, String status, Exception ex) {
        super.storageRequest(urlRequest.toString(), "Elevation request", status, ex);
    }

    @Override
    protected String getStatus(XPath xpath, Document document) {
        NodeList nodes;
        try {
            nodes = (NodeList) xpath.evaluate(this.pathStatus, 
                document, XPathConstants.NODESET);
            return nodes.item(0).getTextContent();
        } catch (XPathExpressionException ex) {
            return null;
        }
    }

    @Override
    protected void storeInfoRequest(URL urlRequest, String info, String status, Exception exception) {
        super.storageRequest(urlRequest.toString(), "Elevation request", status, exception);
    }
    
   
    /**
     * Devuelve la elevación (datum WGS84) del lugar especificado
     * @param latitude latitud del punto a obtener elevación
     * @param longitude longitud del punto a obtener elevación
     * @return devuelve la elevación en metros del lugar especificado.
     * Devuelve 0.0 en caso de error.
     * @see Elevation#getResolution() 
     */
    public double getElevation(double latitude, double longitude) throws MalformedURLException{
        URL url=createURL(latitude,longitude);
        this.resolution=0.0;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
                DocumentBuilder builder = factory.newDocumentBuilder(); 
                Document document = builder.parse(url.openStream()); 

                XPathFactory xpathFactory = XPathFactory.newInstance(); 
                XPath xpath = xpathFactory.newXPath(); 

                NodeList nodeElevation = (NodeList) xpath.evaluate("ElevationResponse/result/elevation", 
                         document, XPathConstants.NODESET);
                NodeList nodeResolution = (NodeList) xpath.evaluate("ElevationResponse/result/resolution", 
                         document, XPathConstants.NODESET);
                
               double elevationResult=0.0;
               
               try {
                   elevationResult=Double.valueOf(nodeElevation.item(0).getTextContent());
                   this.resolution=Double.valueOf(nodeResolution.item(0).getTextContent());
                } catch (Exception e) {
                     onError(url,"NO STATUS",e);
                }
                
                this.storeInfoRequest(url, null, this.getStatus(xpath, document), null);
                return elevationResult;
            } catch (Exception e) {
                onError(url,"NO STATUS",e);
                return 0.0;
            }
    }
    
    
    /**
     * Devuelve la elevación (datum WGS84) del lugar especificado
     * @param LatiLong ArrayList<Double> donde se incluye la lista de latitud/longitud de los puntos a obtener elevación.
     * El orden debe de ser latitud1/longitud1/latitud2/longitud2, etc.
     * @return devuelve un ArrayList<Double> con las elevaciones correspondientes a cada par de coordenadas.
     * Devuelve null en caso de error.
     * @see Elevation#getResolutionList() 
     */
    public ArrayList<Double> getElevation(ArrayList<Double> LatiLong) throws MalformedURLException{
        URL url=createURL(LatiLong);
         this.resolutionList.clear();
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
                DocumentBuilder builder = factory.newDocumentBuilder(); 
                Document document = builder.parse(url.openStream()); 

                XPathFactory xpathFactory = XPathFactory.newInstance(); 
                XPath xpath = xpathFactory.newXPath(); 

                NodeList nodeElevation = (NodeList) xpath.evaluate("ElevationResponse/result/elevation", 
                         document, XPathConstants.NODESET);
                NodeList nodeResolution = (NodeList) xpath.evaluate("ElevationResponse/result/resolution", 
                         document, XPathConstants.NODESET);
                
               ArrayList<Double> elevationResult;
               elevationResult=super.getNodesDouble(nodeElevation);
              
               this.resolutionList=super.getNodesDouble(nodeResolution);
               
                this.storeInfoRequest(url, null, this.getStatus(xpath, document), null);
                return elevationResult;
            } catch (Exception e) {
                onError(url,"NO STATUS",e);
                return null;
            }
    }
   
}
