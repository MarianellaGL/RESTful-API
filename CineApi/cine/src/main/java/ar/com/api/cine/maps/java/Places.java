package ar.com.api.cine.maps.java;

import java.awt.Image;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * REQUIERE CLAVE DESARROLLADOR API GOOGLE MAPS
 *Los usuarios con una clave de API se permite 1 000 solicitudes por cada período de 24 horas.<br/>
 * Los usuarios que hayan verificado su identidad a través de la consola de las API se permiten 100 000 solicitudes por
 * cada período de 24 horas. Se requiere una tarjeta de crédito para verificar, al permitir la facturación en la consola.
 * Se pedimos su tarjeta de crédito exclusivamente para validar su identidad. Su tarjeta no se cobrará por el uso de la API de Places.
 * @author Luis Marcos
 */
public class Places extends MapsJava {

    private final String URLRoot="https://maps.googleapis.com/maps/api/place/search/xml";
    private final String URLDetails="https://maps.googleapis.com/maps/api/place/details/xml";
    private final String URLPhoto="https://maps.googleapis.com/maps/api/place/photo";
    
    private final String pathStatus="PlaceSearchResponse/status";
    private final String pathDetailsStatus="PlaceDetailsResponse/status";
    
    private ArrayList<String> photosReference;

    /**
     * Devuelve el conjunto de referencias de fotografías. Para obtenerlas, se REQUIERE QUE 
     * <b>PREVIAMENTE SE LLAME A LA FUNCIÓN getPlaceReview, y en caso de haber fotografías, se almacenarán</b>
     * las referencias. Si no hubiese fotografías, se devolverá un ArrayList vacío.<br/>
     * Para obtener las fotografías asociadas a las referencias, hay que llamar a la función
     * getPhoto y enviarle como parámetro la referencia.
     * @return ArrayList con las diferentes referencias de las fotografías
     * @see Places#getPlaces(double, double, int, java.lang.String, java.lang.String, maps.java.Places.Rankby, java.util.ArrayList) 
     * @see Places#getPlaceReview(java.lang.String) 
     * @see Places#getPhoto(java.lang.String, int) 
     * 
     */
    public ArrayList<String> getPhotosReference() {
        return photosReference;
    }
    
    /**
     * Enumeración con los diferentes tipos de ordenación de resultados.<br/>
     * <b>Rankby.prominence</b> --> Esta opción permite ordenar los resultados por importancia.<br/>
     * <b>Rankby.distance</b> -->  Esta opción permite disponer los resultados en orden ascendente en función de la distancia a la que se encuentren de la 
     * ubicación especificada.
     */
    public enum Rankby{prominence,distance}
    
    @Override
    protected void onError(URL urlRequest, String status, Exception ex) {
        super.storageRequest(urlRequest.toString(), "Places request", status, ex);
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
        super.storageRequest(urlRequest.toString(), info, status, exception);
    }
    
    private String[][] getNodesPlaces(ArrayList<NodeList> nodes){
        String[][] result=new String[1000][6];
        for(int i = 0; i < nodes.size();i++){
             for (int j = 0, n = nodes.get(i).getLength(); j < n; j++) {
                String nodeString = nodes.get(i).item(j).getTextContent();
                result[j][i]=nodeString;
             }
        }
        result=(String[][])super.resizeArray(result, nodes.get(0).getLength());
        return result;
    }
    
    private URL createURL(double latitude, double longitude,int radius,String keyword,String namePlace,
            Rankby rankby,ArrayList<String> types) throws UnsupportedEncodingException, MalformedURLException{
        
        String _location= URLRoot + "?location=" + latitude + "," + longitude;
        String _radius= "";
        if(!rankby.equals(Rankby.distance)){
            _radius= "&radius=" + radius;
        }
      
        String _keyword="";
        if(keyword!=null && !keyword.isEmpty()){
            _keyword="&keyword=" +  URLEncoder.encode(keyword, "utf-8");
        }
        String _namePlace="";
        if(namePlace!=null && !namePlace.isEmpty()){
            _namePlace="&name=" + URLEncoder.encode(namePlace, "utf-8");
        }
        String _rankby="&rankby=" + rankby.toString();
        String _types="";
        if(types!=null && types.size()>0){
            _types="&types=";
            for(String item:types){
                _types+=item;
            }
        }
        URL urlReturn=new URL(_location + _radius + _keyword + _namePlace + _rankby +
                _types + super.getSelectPropertiesRequest() + "&key=" + MapsJava.getKey());
        return urlReturn;
    }
    
    /**
     * Busca información sobre locales/lugares (places) y devuelve información asociada a esos lugares. Máximo 20 lugares por petición.<br/>
     * Devuelve un String[n][6] con la siguiente información:<br/>
     * [n][0]="Nombre del lugar";<br/>[n][1]="Dirección (vecindad)";<br/>[n][2]="Latitud";<br/>
     * [n][3]="Longitud";<br/>[n][4]="URL del icono asociado al tipo de place";<br/>[n][5]="Referencia del lugar";<br/>
     * Ejemplo:<br/>
     * [n][0]="Museo Nacional del Prado";<br/>[n][1]="Paseo del Prado, s/n, Madrid";<br/>[n][2]="40.4137530";<br/>
     * [n][3]="-3.6922420";<br/>[n][4]="http://maps.gstatic.com/mapfiles/place_api/icons/museum-71.png";<br/>[n][5]="CoQBdQAAAG
     * YWr6ievgvJK00OcgmBZ6JmiWsq8wine3KsT-9m4fGySNCqZIulwJkE7rdl254BjcmBNrSRMtVFxZdznkkf1YWmy9wQmGSANm61SJwbgJbnF
     * lP-0CsVKgUm73nAiUi3f3JT1gps_G7okwI7kHYmJsCpr2pGksSz9kdaI4wWAtVdEhBBps21G3BIkTX7a6XfcfOmGhQO-Ugg0AEY2gE4K
     * 6_kKj-c61HVpw";<br/>
     * @param latitude (OBLIGATORIO) latitud (junto con longitud), indica el punto alrededor del cual se quiere obtener información de sitios
     * @param longitude (OBLIGATORIO) longitud (junto con latitud), indica el punto alrededor del cual se quiere obtener información de sitios
     * @param radius (OBLIGATORIO) define la distancia (en metros) dentro de la que se deben mostrar resultados de sitios
     * @param keyword  indica un término que se debe comparar con todo el contenido indexado por Google para este sitio (por ejemplo, el nombre, 
     * el tipo y la dirección, así como las opiniones de los clientes y otro contenido de terceros).
     * @param namePlace indica un término que se debe comparar con los nombres de sitios
     * @param rankby especifica el orden en que se deben mostrar los resultados. <br/>
     * Rankby.prominence --> Esta opción permite ordenar los resultados por importancia.<br/>
     * Rankby.distance -->  Esta opción permite disponer los resultados en orden ascendente en función de la distancia a la que se encuentren de la 
     * ubicación especificada. <br/>REQUIERE ESTABLECER keyword, namePlace o types.
     * @param types restringe los resultados a los sitios que coinciden como mínimo con uno de los tipos especificados. <br/>Más info. sobre tipos de locales en
     * https://developers.google.com/places/documentation/supported_types?hl=es
     * @return devuelve string bidimensional con información sobre places. <br/>
     * En caso de error retorna null.
     * @see Rankby
     * @see Places#getPhotosReference() 
     * @see Places#getPlacesDetails(java.lang.String) 
     * @see Places#getPlaceReview
     */
    public String[][] getPlaces(double latitude, double longitude,int radius,String keyword,String namePlace,
            Rankby rankby,ArrayList<String> types) throws UnsupportedEncodingException, MalformedURLException{
        URL url=createURL(latitude,longitude,radius,keyword,namePlace,rankby,types);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse(url.openStream()); 

            XPathFactory xpathFactory = XPathFactory.newInstance(); 
            XPath xpath = xpathFactory.newXPath();
            
            NodeList nodeName = (NodeList) xpath.evaluate("PlaceSearchResponse/result/name", 
                         document, XPathConstants.NODESET);
            NodeList nodeVicinity = (NodeList) xpath.evaluate("PlaceSearchResponse/result/vicinity", 
                         document, XPathConstants.NODESET);
            NodeList nodeLatitude = (NodeList) xpath.evaluate("PlaceSearchResponse/result/geometry/location/lat", 
                         document, XPathConstants.NODESET);
            NodeList nodeLongitude = (NodeList) xpath.evaluate("PlaceSearchResponse/result/geometry/location/lng", 
                         document, XPathConstants.NODESET);
            NodeList nodeIcon = (NodeList) xpath.evaluate("PlaceSearchResponse/result/icon", 
                         document, XPathConstants.NODESET);
            NodeList nodeReference = (NodeList) xpath.evaluate("PlaceSearchResponse/result/reference", 
                         document, XPathConstants.NODESET);
                
            ArrayList<NodeList> allNodes=new ArrayList<>();
            allNodes.add(nodeName);allNodes.add(nodeVicinity);allNodes.add(nodeLatitude);
            allNodes.add(nodeLongitude);allNodes.add(nodeIcon);allNodes.add(nodeReference);
            String[][] result=this.getNodesPlaces(allNodes);
            
            this.storeInfoRequest(url, "Places request", this.getStatus(xpath, document), null);
            return result;
        } catch (Exception e) {
            onError(url,"NO STATUS",e);
            return null;
        }
    }
    
    private String[] getNodesDetails(ArrayList<NodeList> nodes){
        String[] result=new String[8];
        for(int i = 0; i < nodes.size();i++){
                try {
                    result[i]= nodes.get(i).item(0).getTextContent();
                } catch (Exception ex) {
                    result[i]= "NO DATA";
                }
        }
        return result;
    }
    
    
    protected String getStatusDetails(XPath xpath, Document document) {
        NodeList nodes;
        try {
            nodes = (NodeList) xpath.evaluate(this.pathDetailsStatus, 
                document, XPathConstants.NODESET);
            return nodes.item(0).getTextContent();
        } catch (Exception ex) {
            return null;
        }
    }
    
    private URL createURL(String reference) throws UnsupportedEncodingException, MalformedURLException{
        URL urlReturn=new URL(URLDetails + "?reference=" + URLEncoder.encode(reference, "utf-8") + 
                super.getSelectPropertiesRequest() + "&key=" + MapsJava.getKey());
        return urlReturn;
    }
    
    /**
     * Obtiene detalles de un local a partir de su referencia. La referencia se obtiene a través de una búsqueda de 
     * places (getPlaces), ya que en los resultados devueltos el último lugar corresponde a la referencia de un lugar
     * [i][5]<br/>
     * Devuelve un String[5] con la siguiente información:<br/>
     * [0]="Nombre local";<br/>[1]="Vecindad";<br/>[2]="Número de teléfono";<br/>
     * [3]="Dirección del local";<br/>[4]="URL página Google+";<br/>[5]="Puntuación loca (sobre 5)";<br/>
     * [6]="URL del icono asociado al tipo de place";<br/>[7]="Página web del local";<br/>
     * Ejemplo:<br/>
     * [0]="Museo Nacional del Prado";<br/>[1]="Paseo del Prado, s/n, Madrid";<br/>[2]="913 30 28 00";<br/>
     * [3]="Paseo del Prado, s/n, Madrid, España";<br/>[4]="https://plus.google.com/105513221084832515311/about?hl=es";<br/>[5]="4.6";<br/>
     * [6]="http://maps.gstatic.com/mapfiles/place_api/icons/museum-71.png";<br/>[7]="http://www.museodelprado.es/";<br/>
     * @param referencePlace referencia del place (se obtiene a través de la función getPlace)
     * @return devuelve String[8] con la información del local.<br/>
     * En caso de error devuelve null
     * @see Places#getPlaces(double, double, int, java.lang.String, java.lang.String, maps.java.Places.Rankby, java.util.ArrayList) 
     */
    public String[] getPlacesDetails(String referencePlace) throws UnsupportedEncodingException, MalformedURLException{
        URL url=createURL(referencePlace);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse(url.openStream()); 

            XPathFactory xpathFactory = XPathFactory.newInstance(); 
            XPath xpath = xpathFactory.newXPath();

            NodeList nodeName = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/name", 
                         document, XPathConstants.NODESET);
            NodeList nodeVicinity = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/vicinity", 
                         document, XPathConstants.NODESET);
            NodeList nodePhone = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/formatted_phone_number", 
                         document, XPathConstants.NODESET);
            NodeList nodeAddress = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/formatted_address", 
                         document, XPathConstants.NODESET);
            NodeList nodeUrlGoogle = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/url", 
                         document, XPathConstants.NODESET);
            NodeList nodeRating = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/rating", 
                         document, XPathConstants.NODESET);
            NodeList nodeIcon = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/icon", 
                         document, XPathConstants.NODESET);
            NodeList nodeWebsite = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/website", 
                         document, XPathConstants.NODESET);
            
            ArrayList<NodeList> allNodes=new ArrayList<>();
            allNodes.add(nodeName);allNodes.add(nodeVicinity);allNodes.add(nodePhone);
            allNodes.add(nodeAddress);allNodes.add(nodeUrlGoogle);allNodes.add(nodeRating);
            allNodes.add(nodeIcon);allNodes.add(nodeWebsite);
            
            String[] result=getNodesDetails(allNodes);
            
            this.storeInfoRequest(url, "Places details request", this.getStatusDetails(xpath, document), null);
            return result;
            
        } catch (Exception e) {
            onError(url,"NO STATUS",e);
            return null;
        }
    }
    
    private String[][] getNodesReview(ArrayList<NodeList> nodes){
        String[][] result=new String[1000][4];
        for(int i = 0; i < nodes.size();i++){
             for (int j = 0, n = nodes.get(i).getLength(); j < n; j++) {
                 String nodeString = nodes.get(i).item(j).getTextContent();
                 result[j][i]=nodeString;
             }
        }
        result=(String[][])super.resizeArray(result, nodes.get(0).getLength());
        return result;
    }
    
    private ArrayList<String> getNodesPhoto(NodeList node){
       ArrayList<String> result=new ArrayList<>();
             for (int j = 0, n = node.getLength(); j < n; j++) {
                String nodeString = node.item(j).getTextContent();
                result.add(nodeString);
             }
        return result;
    }
    
    /**
     * Obtiene las reviews de un determinado local. La referencia se obtiene a través de una búsqueda de 
     * places (getPlaces), ya que en los resultados devueltos el último lugar corresponde a la referencia de un lugar
     * [i][5]<br/>
     * Devuelve un String[n][4] con la siguiente información:<br/>
     * [n][0]="Fecha de la review (en tiempo UNIX en segundos)";<br/>[n][1]="Nombre del autor";<br/>
     * [n][2]="Texto de la review";<br/>[n][3]="URL Google+ del autor";<br/>
     * Ejemplo:<br/>
     * [n][0]="1380809161";<br/>[n][1]="sara robles";<br/>
     * [n][2]="Es una de las mejores pinacotecas del mundo, con gran variedad de obras de diversos artistas, 
     * cuenta con exposiciones temporales de artistas recopilando obras de diversos museos del mundo, esta 
     * todo muy cuidado y esta bien de precio y la ubicacion es genial, y lo recomiendo al maximo si eres 
     * amante del arte es un lugar que no debes de dejar de visitar en madrid y si no eres amante del arte 
     * tambien es un lugar que no te puedes perder. Goya, Velazquez y todos los grandes maestros 
     * te estan esperando.";<br/>[n][3]="https://plus.google.com/116072533644897622969";<br/>
     * Adicionalmente, y en caso de haber fotografías del local, la referencia a estas fotografías se puede obtener
     * a través de la función getPhotosReference.
     * @param referencePlace referencia del place (se obtiene a través de la función getPlace)
     * @return devuelve un array bidimensional [i][4] con la información de las reviews.<br/>
     * En caso de error devuelve null
     * @see Places#getPlaces(double, double, int, java.lang.String, java.lang.String, maps.java.Places.Rankby, java.util.ArrayList) 
     * @see Places#getPhotosReference() 
     * @see Places#getPhoto(java.lang.String, int) 
     */
    public String[][] getPlaceReview(String referencePlace) throws UnsupportedEncodingException, MalformedURLException{
        URL url=createURL(referencePlace);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse(url.openStream()); 

            XPathFactory xpathFactory = XPathFactory.newInstance(); 
            XPath xpath = xpathFactory.newXPath();

            NodeList nodeTime = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/review/time", 
                         document, XPathConstants.NODESET);
            NodeList nodeAuthor= (NodeList) xpath.evaluate("PlaceDetailsResponse/result/review/author_name", 
                         document, XPathConstants.NODESET);
            NodeList nodeText = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/review/text", 
                         document, XPathConstants.NODESET);
            NodeList nodeURL = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/review/author_url", 
                         document, XPathConstants.NODESET);
            
            ArrayList<NodeList> allNodes=new ArrayList<>();
            allNodes.add(nodeTime);allNodes.add(nodeAuthor);allNodes.add(nodeText);
            allNodes.add(nodeURL);
            
            String[][] result=getNodesReview(allNodes);
            
            this.storeInfoRequest(url, "Places review request", this.getStatusDetails(xpath, document), null);
            
            NodeList nodePhoto = (NodeList) xpath.evaluate("PlaceDetailsResponse/result/photo/photo_reference", 
                         document, XPathConstants.NODESET);
            this.photosReference=this.getNodesPhoto(nodePhoto);
            
            return result;
            
        } catch (Exception e) {
            onError(url,"NO STATUS",e);
            return null;
        }
    }
    
    private URL createURL(String photoreference,int maxWidth) throws MalformedURLException{
        URL urlReturn=new URL(URLPhoto + "?maxwidth=" + maxWidth + "&photoreference=" + 
                photoreference + super.getSelectPropertiesRequest() + "&key=" + MapsJava.getKey());
        return urlReturn;
    }
    
    /**
     * Devuelve la foto del local a través de su referencia. Dicha referencia, se obtiene a partir de 
     * una petición previa de detalles del local (getPlaceReview), que, en caso de haber fotografías, 
     * almacena todas sus referencias (dichas referencias se obtiene con la función getPhotosReference.
     * @param photoreference string con referencia de fotografía
     * @param maxWidth ancho máximo de la imagen devuelta
     * @return devuelve la imagen asociada a dicha referencia del local<br/>
     * En caso de error, devuelve null
     * @see Places#getPlaces(double, double, int, java.lang.String, java.lang.String, maps.java.Places.Rankby, java.util.ArrayList) 
     * @see Places#getPlacesDetails(java.lang.String) 
     * @see Places#getPhotosReference() 
     */
    public Image getPhoto(String photoreference,int maxWidth) throws MalformedURLException{
        URL url=createURL(photoreference,maxWidth);
        try {
            Image imageReturn;
            imageReturn=ImageIO.read(url);
            storeInfoRequest(url,"Photo request","OK",null);
            return imageReturn;
        } catch (Exception e) {
            onError(url, "NO STATUS", e);
            return null;
        }
    }
}
