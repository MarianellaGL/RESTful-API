package ar.com.ada.mongo.api.nifli.entities;

import java.util.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Series")
public class Serie extends Contenido {

    @Id
    private ObjectId _id;

    public Serie() {

        super();
    }

    
    public List<Temporada> temporadas = new ArrayList<Temporada>();

    public Temporada getTemporada(int nro)

    {

        for (Temporada tempo : this.temporadas) {

           

            if (tempo.numerotemporada == nro) {

                return tempo;

            }

        }
        return null;

    }

   public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }



    
}