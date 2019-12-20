package ar.com.api.cine.maps.java;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Clase padre desde donde se extienden el resto. Sirve principalmente para asignar/obtener propiedades básicas
 * a la hora de realizar una petición (clave, sensor, idioma, etc.) y para obtener un registro de todas las peticiones realizadas
 * @author Luis Marcos
 */
public abstract class MapsJava {

    //request properties 
    private static int connectTimeout=300;
    private static String region="es";
    private static String language="es";
    private static Boolean sensor= Boolean.FALSE;
    private static String APIKey="";
    
    //Stock request
    private static String[][] stockRequest=new String[0][6];

    

    
    //Abstract methods
    protected abstract void onError(URL urlRequest,String status,Exception ex);
    protected abstract String getStatus(XPath xpath, Document document);
    protected abstract void storeInfoRequest(URL urlRequest,String info,String status,Exception exception);
    

    //Protected methods
    private static int numRequest=0;
    protected void storageRequest(String urlRequest,String info,String status,
            Exception exception){
        Date date = new Date();
        numRequest+=1;
        MapsJava.stockRequest=(String[][])(this.resizeArray(MapsJava.stockRequest,numRequest));
        if(MapsJava.stockRequest[numRequest-1]==null){
                MapsJava.stockRequest[numRequest-1]=new String[6];
            }
        MapsJava.stockRequest[numRequest-1][0]=String.valueOf(numRequest);
        MapsJava.stockRequest[numRequest-1][1]=date.toString();
        MapsJava.stockRequest[numRequest-1][2]=status;
        MapsJava.stockRequest[numRequest-1][3]=urlRequest;
        MapsJava.stockRequest[numRequest-1][4]=info;
        if(exception==null){
            MapsJava.stockRequest[numRequest-1][5]="No exception";
        }else{
            MapsJava.stockRequest[numRequest-1][5]=exception.toString();
        }
        
    }
    
    protected String getSelectPropertiesRequest(){
        return "&region=" + MapsJava.region + "&language=" + MapsJava.language + 
                "&sensor=" + MapsJava.sensor;
    }
     protected ArrayList<String> getNodesString(NodeList node){
         ArrayList<String> result=new ArrayList<>();
             for (int j = 0, n = node.getLength(); j < n; j++) {
                String nodeString = node.item(j).getTextContent();
                result.add(nodeString);
             }
        return result;
    }
     
    protected ArrayList<Double> getNodesDouble(NodeList node){
         ArrayList<Double> result=new ArrayList<>();
             for (int j = 0, n = node.getLength(); j < n; j++) {
                String nodeString = node.item(j).getTextContent();
                result.add(Double.valueOf(nodeString));
             }
        return result;
    }
    
    protected ArrayList<Integer> getNodesInteger(NodeList node){
         ArrayList<Integer> result=new ArrayList<>();
             for (int j = 0, n = node.getLength(); j < n; j++) {
                String nodeString = node.item(j).getTextContent();
                result.add(Integer.valueOf(nodeString));
             }
        return result;
    }
    
    /**
    * Author: Christian d'Heureuse (www.source-code.biz, www.inventec.ch/chdh)
    * Reallocates an array with a new size, and copies the contents
    * of the old array to the new array.
    * @param oldArray  the old array, to be reallocated.
    * @param newSize   the new array size.
    * @return          A new array with the same contents.
    */
    protected Object resizeArray (Object oldArray, int newSize) {
       int oldSize = java.lang.reflect.Array.getLength(oldArray);
       Class elementType = oldArray.getClass().getComponentType();
       Object newArray = java.lang.reflect.Array.newInstance(
             elementType, newSize);
       int preserveLength = Math.min(oldSize, newSize);
       if (preserveLength > 0)
          System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
       return newArray; 
    }
    
    //Public methods
    /**
     * Comprueba si la clave de desarrollador API Google Maps es válida.
     * @param key clave de desarrollador API Google Maps
     * @return devuelve el estado de una petición con clave API. En caso de ser válida, devuelve
     * "OK", en cualquier otro caso la clave no es correcta.
     * @see MapsJava#setKey(java.lang.String) 
     * @see MapsJava#getKey() 
     */
    public static String APIkeyCheck(String key){
        try{
            URL url=new URL("https://maps.googleapis.com/maps/api/place/search/xml?location=0,0&radius=1000" + 
                    "&sensor=false&key=" + key);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse(url.openStream()); 
            XPathFactory xpathFactory = XPathFactory.newInstance(); 
            XPath xpath = xpathFactory.newXPath(); 

            NodeList nodeLatLng = (NodeList) xpath.evaluate("PlaceSearchResponse/status", 
                            document, XPathConstants.NODESET);
            String status = nodeLatLng.item(0).getTextContent();
            return status;
        }catch (Exception e){
            return "NO STATUS";
        }
    }
    
    //Public access to properties of the request (Getters/Setters)
    /**
     * Devuelve el tiempo de conexión máximo (milisegundos) de espera al servidor (NO FUNCIONA)
     * @return int con tiempo máximo de conexión
     * @see MapsJava#setConnectTimeout(int) 
     */
    public static int getConnectTimeout() {
        return connectTimeout;
    }
    /**
     * Establece el tiempo máximo de espera (milisegundos) por el servidor (NO FUNCIONA)
     * @param aConnectTimeout asigna tiempo máximo de conexión
     * @see MapsJava#getConnectTimeout() 
     */
    public static void setConnectTimeout(int aConnectTimeout) {
        connectTimeout = aConnectTimeout;
    }
    
    /**
     * Obtiene la región de búsqueda de resultados (de forma predeterminada "es").
     * Más info. sobre regiones en http://es.wikipedia.org/wiki/CcTLD
     * @return devuelve la región de búsqueda actual
     * @see MapsJava#setRegion(java.lang.String) 
     */
    public static String getRegion() {
        return region;
    }
    
    /**
     * Establece la región de búsqueda de resultados (de forma predeterminada "es").
     * Más info. sobre regiones en http://es.wikipedia.org/wiki/CcTLD
     * @param aRegion asigna la región de búsqueda
     * @see MapsJava#getRegion() 
     */
    public static void setRegion(String aRegion) {
        region = aRegion;
    }

    /**
     * Obtiene el idioma en el que se muestran los resultados (de forma predeterminada "es")
     * Más info. sobre idiomas en https://spreadsheets.google.com/pub?key=p9pdwsai2hDMsLkXsoM05KQ&gid=1
     * @return devuelve el idioma actual de los resultados
     * @see MapsJava#setLanguage(java.lang.String) () 
     */
    public static String getLanguage() {
        return language;
    }
    /**
     * Establece el idioma en el que se muestran los resultados (de forma predeterminada "es")
     * Más info. sobre idiomas en https://spreadsheets.google.com/pub?key=p9pdwsai2hDMsLkXsoM05KQ&gid=1
     * @param aLanguage establece el idioma de resultados
     * @see MapsJava#getLanguage() 
     */
    public static void setLanguage(String aLanguage) {
        language = aLanguage;
    }

    /**
     * Obtiene si se está utilizando sensor GPS (GNSS) en las peticiones para obtener ubicación (de forma predeterminada es false)
     * @return devuelve true en caso de utilización del sensor y false en caso contrario
     * @see MapsJava#setSensor(java.lang.Boolean) 
     */
    public static Boolean getSensor() {
        return sensor;
    }
    /**
     * Establece el uso o no uso de un sensor GPS (GNSS) en las peticiones para obtener ubicación (de forma predeterminada false)
     * @param aSensor en caso de ser true, se fuerza a utilizar sensor. Si es false, no se utiliza
     * @see MapsJava#getSensor() 
     */
    public static void setSensor(Boolean aSensor) {
        sensor = aSensor;
    }
    
    /**
     * Obtiene la clave actual de desarrollador de API Google Maps (sólo necesario para Places)
     * @return obtiene string con clave actual
     * @see MapsJava#setKey(java.lang.String) 
     * @see MapsJava#APIkeyCheck(java.lang.String) 
     */
    public static String getKey() {
        return APIKey;
    }

    /**
     * Establece clave de desarrollador API Google Maps (sólo necesario para Places)
     * @param aKey string con clave API de desarrollador
     * @see MapsJava#getKey() 
     * @see MapsJava#APIkeyCheck(java.lang.String) 
     */
    public static void setKey(String aKey) {
        APIKey = aKey;
    }
    
    
    //Public acces to stockRequest 
    /**
     * Obtiene registro de todas las peticiones HTTP realizadas. Conforma un String[n][6] con la siguiente
     * estructura: <br/>
     * [0][0]="Número de petición";<br/>[0][1]="Fecha/hora petición";<br/>[0][2]="status de la petición";<br/>
     * [0][3]="URL de la petición";<br/>[0][4]="Información sobre petición realizada";<br/>[0][5]="Excepciones generadas";<br/>
     * Ejemplo:<br/>
     * [0][0]="1";<br/>[0][1]="Mon Oct 07 11:30:31 CEST 2013";<br/>[0][2]="OK";<br/>
     * [0][3]=""http://maps.google.com/maps/api/geocode/xml?address=Madrid&region=es&language=es&sensor=false"";<br/>[0][4]="Geocoding request";<br/>[0][5]="No exception";<br/>
     * Tipos de status:<br/>
     * OK: indica que la solicitud del API se realizó correctamente.<br/>
     * INVALID_REQUEST: indica que la solicitud del API se formó de forma incorrecta.<br/>
     * OVER_QUERY_LIMIT: indica que el solicitante ha superado los límites.<br/>
     * REQUEST_DENIED: indica que el API no completó la solicitud, posiblemente porque el solicitante no puedo incluir correctamente un parámetro sensor válido.<br/>
     * UNKNOWN_ERROR: indica un error desconocido.<br/>
     * NO STATUS: indica un error al realizar la petición<br/>
     * @return devuelve un array de dos dimensiones con las diferentes peticiones realizadas
     */
    public static String[][] getStockRequest() {
        return stockRequest;
    }

    /**
     * Obtiene registro de la última petición HTTP realizada. Conforma un String[6] con la siguiente estructura:</br>
     * [0]="Número de petición";<br/>[1]="Fecha/hora petición";<br/>[2]="status de la petición";<br/>
     * [3]="URL de la petición";<br/>[4]="Información sobre petición realizada";<br/>[5]="Excepciones generadas";<br/>
     * Ejemplo:<br/>
     * [0]="1";<br/>[1]="Mon Oct 07 11:30:31 CEST 2013";<br/>[2]="OK";<br/>
     * [3]="http://maps.google.com/maps/api/geocode/xml?address=Madrid&region=es&language=es&sensor=false";<br/>[4]="Geocoding request";<br/>[5]="No exception";<br/>
     * Tipos de status:<br/>
     * OK: indica que la solicitud del API se realizó correctamente.<br/>
     * INVALID_REQUEST: indica que la solicitud del API se formó de forma incorrecta.<br/>
     * OVER_QUERY_LIMIT: indica que el solicitante ha superado los límites.<br/>
     * REQUEST_DENIED: indica que el API no completó la solicitud, posiblemente porque el solicitante no puedo incluir correctamente un parámetro sensor válido.<br/>
     * UNKNOWN_ERROR: indica un error desconocido.<br/>
     * NO STATUS: indica un error al realizar la petición<br/>
     * @return array de una dimensión con la última petición realizada
     * @see MapsJava#getStockRequest() 
     */
    public static  String[] getLastRequestRequest() {
        String[] stockRequestTemp=new String[6];
        System.arraycopy(stockRequest[stockRequest.length-1], 0, stockRequestTemp, 0, 6);
        return stockRequestTemp;
    }
    
    /**
     * Obtiene el status de la última petición realizada.<br/>
     * Tipos de status:<br/>
     * OK: indica que la solicitud del API se realizó correctamente.<br/>
     * INVALID_REQUEST: indica que la solicitud del API se formó de forma incorrecta.<br/>
     * OVER_QUERY_LIMIT: indica que el solicitante ha superado los límites.<br/>
     * REQUEST_DENIED: indica que el API no completó la solicitud, posiblemente porque el solicitante no puedo incluir correctamente un parámetro sensor válido.<br/>
     * UNKNOWN_ERROR: indica un error desconocido.<br/>
     * NO STATUS: indica un error al realizar la petición<br/>
     * @return devuelve string con estado de última petición
     * @see MapsJava#getStockRequest() 
     */
    public static String getLastRequestStatus() {
         return stockRequest[stockRequest.length-1][2];
    }
    /**
     * Devuelve URL asociada a la última petición realizada.
     * @return retorna string con URL de la última petición (por ejemplo, "http://maps.google.com/maps/api/geocode/xml?address=Madrid&region=es&language=es&sensor=false"
     * @see MapsJava#getStockRequest() 
     */
    public static String getLastRequestURL() {
        return stockRequest[stockRequest.length-1][3];
    }
    /**
     * Devuelve información sobre el tipo de la última petición realizada
     * @return retorna string con información de la última petición realizada (por ejemplo, "Geocoding request")
     * @see MapsJava#getStockRequest() 
     */
    public static String getLastRequestInfo() {
         return stockRequest[stockRequest.length-1][4];
    }
    /**
     * Devuelve información sobre la posible excepción generada en la última petición.
     * @return retorna string con información sobre error de la última petición (por ejemplo, "No exception")
     * @see MapsJava#getStockRequest() 
     */
    public static String getLastRequestException() {
         return stockRequest[stockRequest.length-1][5];
    }

  
}
