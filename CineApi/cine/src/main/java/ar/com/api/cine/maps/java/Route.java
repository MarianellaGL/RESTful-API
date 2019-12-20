package ar.com.api.cine.maps.java;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * El uso del API de rutas de Google está sujeto a un límite de 2.500 solicitudes de rutas al día.<br/>
 * Las solicitudes individuales de indicaciones en coche, a pie o en bicicleta pueden contar como un máximo de ocho hitos intermedios en la solicitud.
 * el API de rutas solo se puede utilizar en combinación con la presentación de resultados en un mapa de Google. No se permite utilizar los datos del 
 * servicio de rutas sin mostrar un mapa correspondiente a los datos de rutas solicitados. Además, al calcular rutas se generan avisos sobre derechos 
 * de autor y advertencias que se deben mostrar al usuario de algún modo.
 * @author Luis Marcos
 */
public class Route extends MapsJava{

    private final String URLRoot="http://maps.googleapis.com/maps/api/directions/xml";
    private final String pathStatus="DirectionsResponse/status";
    
    private String summary="";
    private String copyright="";
    private ArrayList<Integer> waypointIndex=new ArrayList<>();
    private ArrayList<Integer> totalTime=new ArrayList<>();
    private ArrayList<Integer> totalDistance=new ArrayList<>();
    private ArrayList<String> polilines=new ArrayList<>();
    
    /**
     * Indica el tipo de medio de transporte de la ruta. Los valores son:<br/>
     * <b>driving:</b> proporciona rutas estándar para llegar en coche a través de la red de carreteras.<br/>
     * <b>walking:</b> proporciona rutas a pie a través de aceras y rutas peatonales (según disponibilidad).<br/>
     * <b>driving:</b> proporciona rutas para llegar en bicicleta a través de carriles bici y vías preferenciales para bicicletas (según disponibilidad).<br/>
     * <b>driving:</b> solicita indicaciones a través de rutas de transporte público (según disponibilidad) (NO FUNCIONA ACTUALMENTE).
     */
    public enum mode{driving,walking,bicycling,transit}
    
    /**
     * Indica que la ruta deben evitar determinados elementos. A continuación, se indican los tres argumentos que admite actualmente este parámetro:<br/>
     * <b>nothing:</b> no se evita ningún tipo de elemento.<br/>
     * <b>tolls:</b> indica que la ruta calculada debe evitar los peajes de carretera y de puentes.<br/>
     * <b>highways:</b>  indica que la ruta calculada debe evitar las autopistas y las autovías.
     */
    public enum avoids{nothing,tolls,highways}

    /**
     * Indica el resumen de la ruta calculada.<br/>
     * <b>REQUIERE CALCULAR PRIMERAMENTE LA RUTA (con getRoute).</b>
     * @return devuelve resumen de la ruta
     * @see Route#getRoute(java.lang.String, java.lang.String, java.util.ArrayList, java.lang.Boolean, maps.java.Route.mode, maps.java.Route.avoids) 
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Indica el copyright de la ruta calculada.<br/>
     * <b>REQUIERE CALCULAR PRIMERAMENTE LA RUTA (con getRoute).</b>
     * @return devuelve copyright de la ruta
     * @see Route#getRoute(java.lang.String, java.lang.String, java.util.ArrayList, java.lang.Boolean, maps.java.Route.mode, maps.java.Route.avoids) 
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Indica el orden de los hitos incluidos en la ruta.<br/>
     * <b>REQUIERE CALCULAR PRIMERAMENTE LA RUTA (con getRoute).</b>
     * @return devuelve el orden de la ruta
     * @see Route#getRoute(java.lang.String, java.lang.String, java.util.ArrayList, java.lang.Boolean, maps.java.Route.mode, maps.java.Route.avoids) 
     */
    public ArrayList<Integer> getWaypointIndex() {
        return waypointIndex;
    }

    /**
     * Indica el tiempo total (en segundos) de la ruta calculada (por tramos).<br/>
     * <b>REQUIERE CALCULAR PRIMERAMENTE LA RUTA (con getRoute).</b>
     * @return devuelve el tiempo total de la ruta (en segundos)
     * @see Route#getRoute(java.lang.String, java.lang.String, java.util.ArrayList, java.lang.Boolean, maps.java.Route.mode, maps.java.Route.avoids) 
     */
    public ArrayList<Integer> getTotalTime() {
        return totalTime;
    }

    /**
     * Indica las distancia total (en metros) de la ruta calculada (por tramos).<br/>
     * <b>REQUIERE CALCULAR PRIMERAMENTE LA RUTA (con getRoute).</b>
     * @return devuelve la distancia total de la ruta (en metros)
     * @see Route#getRoute(java.lang.String, java.lang.String, java.util.ArrayList, java.lang.Boolean, maps.java.Route.mode, maps.java.Route.avoids) 
     */
    public ArrayList<Integer> getTotalDistance() {
        return totalDistance;
    }

    /**
     * Indica la referencia de las polilíneas asociadas a cada tramo.<br/>
     * <b>REQUIERE CALCULAR PRIMERAMENTE LA RUTA (con getRoute).</b>
     * @return devuelve referencia de las polilíneas asociadas a cada tramo
     * @see Route#getRoute(java.lang.String, java.lang.String, java.util.ArrayList, java.lang.Boolean, maps.java.Route.mode, maps.java.Route.avoids) 
     */
    public ArrayList<String> getPolilines() {
        return polilines;
    }

  
    
    @Override
    protected void onError(URL urlRequest, String status, Exception ex) {
        super.storageRequest(urlRequest.toString(), "Route request", status, ex);
    }

    @Override
    protected String getStatus(XPath xpath, Document document) {
        NodeList nodes;
        try {
            nodes = (NodeList) xpath.evaluate(this.pathStatus, 
                document, XPathConstants.NODESET);
            return nodes.item(0).getTextContent();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void storeInfoRequest(URL urlRequest, String info, String status, Exception exception) {
        super.storageRequest(urlRequest.toString(), "Route request", status, exception);
    }
    
    private URL createURL(String originAddress, String destinationAddress, ArrayList<String> waypoints,
            Boolean optimize, mode travelMode,avoids avoidsRoad) throws MalformedURLException, UnsupportedEncodingException{
        
        String origin="?origin=" + URLEncoder.encode(originAddress, "utf-8");
        String destination="&destination=" + URLEncoder.encode(destinationAddress, "utf-8");
        String waypoi="";
        if(waypoints!=null && waypoints.size()>0){
            if(optimize==true){
                waypoi="&waypoints=optimize:true|";
            }else{
                waypoi="&waypoints=";
            }
            for(String item:waypoints){
                waypoi+=URLEncoder.encode(item, "utf-8") + "|";
            }
        }
        String travel="&mode=" + travelMode.toString();
        
        String avoid="";
        switch(avoidsRoad.toString()){
            case "nothing":
                avoid="";
                break;
            case "tolls":
                avoid="&avoid=tolls";
                break;
            case "highways":
                avoid="&avoid=highways";
                break;
        }
        
        URL urlReturn=new URL(URLRoot+origin+destination+waypoi+travel+avoid+super.getSelectPropertiesRequest());
        return urlReturn;
    }
    
    private String[][] getNodesRoute(ArrayList<NodeList> nodes){
        
        String[][] result=new String[1000][5];
        
        for(int i = 0; i < nodes.size();i++){
             for (int j = 0, n = nodes.get(i).getLength(); j < n; j++) {
                String nodeString = nodes.get(i).item(j).getTextContent();
                result[j][i]=nodeString;
             }
        }
        result=(String[][])super.resizeArray(result, nodes.get(0).getLength());
        return result;
    }
    
    private void deleteProperties(){
        summary="";
        copyright="";
        waypointIndex.clear();
        totalTime.clear();
        totalDistance.clear();
        polilines.clear();
    }
    
    /**
     * Devuelve la ruta especificada a partir de de una dirección de origen y una de destino. Además, se 
     * pueden seleccionar puntos intermedios de paso obligado, o hitos (hasta un máximo de 8), y varios tipos de transporte
     * y restricciones de carreteras.<br/></br>
     * [n][0]="Tiempo del tramo (en horas/minutos)";<br/>[n][1]="Distancia recorridas en el tramo (en m/km)";<br/>
     * [n][2]="Indicaciones del tramo";<br/>[n][3]="Latitud de llegada del tramo";<br/>[n][4]="Longitud de llegada del tramo";<br/>
     * Ejemplo:<br/>
     * [n][0]="1 min";<br/>[n][1]="56 m";<br/>
     * [n][2]="Continúa por Plaza Puerta del Sol/Puerta del Sol.";<br/>[n][3]="40.4166260";<br/>[n][4]="-3.7044618";</br></br>
     * <br/>Adicionalmente a esta información, una vez calculada la ruta, se puede comprobar el tiempo copyright de la ruta (getCopyright),
     * el resumen de la ruta (getSummary), el orden de paso de los hitos intermedios (getWaypointIndex), el tiempo total de todos
     * los tramos (getTotalTime), la distancia total de los tramos (getTotalDistance), las polilíneas asociadas a los tramos (getPolilines).
     * @param originAddress dirección postal de origen de la ruta
     * @param destinationAddress dirección postal de destino de la ruta
     * @param waypoints lista de hitos intermedios de paso obligado (opcional)
     * @param optimize reordena (en caso de ser true) los hitos de forma más eficaz para optimizar la ruta proporcionada. Esta optimización es el resultado de aplicar el Problema del viajante.
     * @param travelMode indica el tipo de medio de transporte
     * @param avoidsRoad indica restricción de vías
     * @return devuelve un string bidimensional con la información de la .<br/>
     * En caso de error devuelve null
     * @see Route#getCopyright() 
     * @see Route#getSummary() 
     * @see Route#getTotalDistance() 
     * @see Route#getTotalTime() 
     * @see Route#getWaypointIndex() 
     * @see mode
     * @see avoids
     */
    public String[][] getRoute(String originAddress, String destinationAddress, ArrayList<String> waypoints,
            Boolean optimize, mode travelMode,avoids avoidsRoad) throws MalformedURLException, UnsupportedEncodingException{
        deleteProperties();
        URL url=createURL(originAddress,destinationAddress,waypoints,optimize,travelMode,avoidsRoad);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse(url.openStream()); 

            XPathFactory xpathFactory = XPathFactory.newInstance(); 
            XPath xpath = xpathFactory.newXPath(); 

            NodeList nodeLatitude = (NodeList) xpath.evaluate("DirectionsResponse/route/leg/step/start_location/lat", 
                     document, XPathConstants.NODESET);
            NodeList nodeLongitude = (NodeList) xpath.evaluate("DirectionsResponse/route/leg/step/start_location/lng", 
                     document, XPathConstants.NODESET);
            NodeList nodeTime = (NodeList) xpath.evaluate("DirectionsResponse/route/leg/step/duration/text", 
                     document, XPathConstants.NODESET);
            NodeList nodeDistance = (NodeList) xpath.evaluate("DirectionsResponse/route/leg/step/distance/text", 
                     document, XPathConstants.NODESET);
            NodeList nodeIndications = (NodeList) xpath.evaluate("DirectionsResponse/route/leg/step/html_instructions", 
                     document, XPathConstants.NODESET);
            NodeList nodeSummary = (NodeList) xpath.evaluate("DirectionsResponse/route/summary", 
                     document, XPathConstants.NODESET);
            NodeList nodeCopyright = (NodeList) xpath.evaluate("DirectionsResponse/route/copyrights", 
                     document, XPathConstants.NODESET);
            NodeList nodeWaypointsIndex = (NodeList) xpath.evaluate("DirectionsResponse/route/waypoint_index", 
                     document, XPathConstants.NODESET);
            NodeList nodeTotaltime = (NodeList) xpath.evaluate("DirectionsResponse/route/leg/duration/value", 
                     document, XPathConstants.NODESET);
            NodeList nodeTotalDistance = (NodeList) xpath.evaluate("DirectionsResponse/route/leg/distance/value", 
                     document, XPathConstants.NODESET);
            NodeList nodePolilines = (NodeList) xpath.evaluate("DirectionsResponse/route/leg/step/polyline/points", 
                     document, XPathConstants.NODESET);
            
            
            ArrayList<NodeList> allNodes=new ArrayList<>();
            allNodes.add(nodeTime);allNodes.add(nodeDistance);allNodes.add(nodeIndications);
            allNodes.add(nodeLatitude);allNodes.add(nodeLongitude);
            String[][] result=this.getNodesRoute(allNodes);
            this.storeInfoRequest(url, null, this.getStatus(xpath, document), null);
            try {
                this.copyright=nodeCopyright.item(0).getTextContent();
                this.summary=nodeSummary.item(0).getTextContent();
                this.waypointIndex=super.getNodesInteger(nodeWaypointsIndex);
                this.totalDistance=super.getNodesInteger(nodeTotalDistance);
                this.totalTime=super.getNodesInteger(nodeTotaltime);
                this.polilines=super.getNodesString(nodePolilines);
            } catch (Exception e) {
            }
            
           
            return result;
        } catch (Exception e) {
            onError(url,"NO STATUS",e);
            return null;
        }
    }
}
