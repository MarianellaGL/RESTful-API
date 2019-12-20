/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.api.cine.GUI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Luis
 */
public class EventsStatusBar implements MouseListener{
    private JPanel statusBar;
    private JLabel info=new JLabel("Listo");
    
    public EventsStatusBar(JPanel statusbar){
        statusbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.statusBar=statusbar;
        this.info.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.statusBar.add(this.info);
        this.statusBar.revalidate();
        this.statusBar.repaint();
    }
    
   
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        selectText(e.getComponent().getAccessibleContext().getAccessibleName());
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void selectText(String nameControl){
      if(nameControl!=null){
       String message; 
       switch(nameControl){
            case "Principal":
                message="Listo";
                break;
            case "region":
                message="Región de búsqueda de los resultados";
                break;
            case "infoRegion":
                message="Más información sobre regiones disponibles";
                break;
            case "idioma":
                message="Idioma de los resultados";
                break;
            case "infoIdioma":
                message="Más información sobre idiomas disponibles";
                break;
            case "tiempoConex":
                message="Tiempo máximo de espera al servidor";
                break;
            case "sensor":
                message="Sensor GNSS";
                break;
            case "sensorCombo":
                message="Establece si el sensor GNSS está activo";
                break;
            case "claveAPI":
                message="Clave API de Google Maps para desarrolladores";
                break;
            case "comprobarClave":
                message="Verificar clave API de Google Maps";
                break;
            case "guardarProp":
                message="Guardar propiedades";
                break;
            case "CDdireccion":
                message="Dirección de búsqueda";
                break;
            case "CDdireccionBuscar":
                message="Buscar coordenadas";
                break;
            case "status":
                message="Estado de la petición";
                break;
            case "CD_latitud":
                message="Latitud encontrada";
                break;
            case "CD_longitud":
                message="Longitud encontrada";
                break;
            case "CD_dirEncontr":
                message="Dirección asociada a las coordenadas";
                break;
            case "mostrarMapa":
                message="Cargar mapa en la web";
                break;
            case "CI_latitud":
                message="Latitud de búsqueda (máx. precisión 6 decimales)";
                break;
             case "CI_longitud":
                message="Longitud de búsqueda (máx. precisión 6 decimales)";
                break;
            case "CI_buscar":
                message="Buscar dirección";
                break;
            case "CI_dirEnco":
                message="Dirección asociada a las coordenadas";
                break;
            case "CI_masResul":
                message="Más resultados de búsqueda";
                break;
            case "mostrPeticiones":
                message="Ver todas las peticiones";
                break;
            case "peticiones":
                message="Peticiones realizadas";
                break;
             case "codigoPostal":
                message="Código postal";
                break;
             case "elevacion":
                message="Elevación de la ubicación (en metros)";
                break;
             case "resolucion":
                message="Distancia (en metros) entre los puntos de interpolación";
                break;
                
             case "direccionOrigen":
                message="Dirección de salida de la ruta";
                break;
             case "direccionDestino":
                message="Dirección de destino de la ruta";
                break;
            case "restriccionCarreteras":
                message="Restricciones en carreteras (peajes/vías rápidas)";
                break;
            case "tipoTransporte":
                message="Método de transporte de la ruta (a pie/coche/bicicleta)";
                break;
            case "hito":
                message="Punto intermedio de paso obligado en la ruta";
                break;
            case "calculaRuta":
                message="Calcular ruta";
                break;
            case "tiempoTotal":
                message="Tiempo estimado de la ruta expresado en horas";
                break;
             case "distanciaTotal":
                message="Distancia total de la ruta expresado en kilómetros";
                break;
             case "copyright":
                message="Copyright de los datos";
                break;
            case "resumen":
                message="Resumen de la ruta";
                break;
             case "indicacionesRuta":
                message="Indicaciones de tramos";
                break;
             case "direccionSV":
                message="Dirección de búsqueda de imagen (\"Puerta del Sol, Madrid\" o \"40.41,-3.70\")";
                break;
             case "horizontalSV":
                message="Indica el encabezado de la brújula de la cámara (90 apunta al Norte)";
                break;
            case "zoomSV":
                message="Indica el zoom (valores mayores, menor zoom)";
                break;
             case "imagenSV":
                message="Imagen StreetView encontrada";
                break;
                 
                 
             case "direccionME":
                message="Centro del mapa (\"Puerta del Sol, Madrid\" o \"40.41,-3.70\")";
                break;
            case "escalaME":
                message="Escala del mapa";
                break;
             case "formatoME":
                message="Formato de compresión de la imagen";
                break;
             case "zoomME":
                message="Zoom realizado al centro del mapa";
                break;
             case "tipoMapaME":
                message="Capa mostrada en el mapa";
                break;
            case "crearMapaME":
                message="Crear mapa";
                break;
             case "mapaEstaticoME":
                message="Mapa cargado";
                break;
                 
            case "centroPL":
                message="Centro de búsqueda de places (Obligatorio)";
                break;
            case "radioPL":
                message="Radio máximo de búsqueda (Obligatorio)";
                break;
             case "placePL":
                message="Término que se debe comparar con los nombres de sitios (NO Obligatorio)";
                break;
             case "keyPL":
                message="Término que se debe comparar con todo el contenido indexado por Google (NO Obligatorio)";
                break;
             case "ordenPL":
                message="Orden de búsqueda. \"Distancia\" requiere que se seleccione place, keyword o tipo";
                break;
            case "tipoPL":
                message="Tipo de place a buscar";
                break;
             case "BuscarPL":
                message="Buscar places";
                break;
             case "placesEncPL":
                message="Places encontrados";
                break;
             case "placesReferencia":
                message="Referencia del \"place\" encontrado";
                break;
             case "placesReferenciaBoton":
                message="Ver más datos del local, fotos y reviews";
                break;
                 
            default:
                message="Listo";
        }
        this.info.setText(message);
      }
     
    }




}
