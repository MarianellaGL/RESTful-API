package ar.com.api.cine.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import ar.com.api.cine.maps.java.Geocoding;

import java.util.*;

@Document(collection = "Cines")
public class Cinema {
    @Id
    private ObjectId _id;
    public Map<Integer, Geocoding> locationCinema = new HashMap<Integer, Geocoding>();
    public String nameCinema;
    private String cinemaCuit;
    public Map<String, Movie> moviesOn = new HashMap<String, Movie>();

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getCinemaCuit() {
        return cinemaCuit;
    }

    public void setCinemaCuit(String cinemaCuit) {
        this.cinemaCuit = cinemaCuit;
    }

    public Cinema() {
    }

    public Map<Integer, Geocoding> getLocationCinema() {
        return locationCinema;
    }

    public void setLocationCinema(Map<Integer, Geocoding> locationCinema) {
        this.locationCinema = locationCinema;
    }

    public Map<String, Movie> getMoviesOn() {
        return moviesOn;
    }

    public void setMoviesOn(Map<String, Movie> moviesOn) {
        this.moviesOn = moviesOn;
    }

    public String getNameCinema() {
        return nameCinema;
    }

    public void setNameCinema(String nameCinema) {
        this.nameCinema = nameCinema;
    }

}