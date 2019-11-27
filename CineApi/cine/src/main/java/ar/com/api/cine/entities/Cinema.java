package ar.com.api.cine.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Cines")
public class Cinema {
    @Id
    private ObjectId _id;
    private String locationCinema;
    private String cinemaCuit;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getLocationCinema() {
        return locationCinema;
    }

    public void setLocationCinema(String locationCinema) {
        this.locationCinema = locationCinema;
    }

    public String getCinemaCuit() {
        return cinemaCuit;
    }

    public void setCinemaCuit(String cinemaCuit) {
        this.cinemaCuit = cinemaCuit;
    }

    public Cinema() {
    }
    
   


    
    
}