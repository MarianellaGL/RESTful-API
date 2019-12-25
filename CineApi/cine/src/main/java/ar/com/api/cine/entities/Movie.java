package ar.com.api.cine.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Movies")
public class Movie {

    @Id
    private Object _id;
    public String movieOn;
    public String directedBy;
    public String actedBy;
    

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getMovieOn() {
        return movieOn;
    }

    public void setMovieOn(String movieOn) {
        this.movieOn = movieOn;
    }

    public String getDirectedBy() {
        return directedBy;
    }

    public void setDirectedBy(String directedBy) {
        this.directedBy = directedBy;
    }

    public String getActedBy() {
        return actedBy;
    }

    public void setActedBy(String actedBy) {
        this.actedBy = actedBy;
    }

    public Movie() {
    }

  
    
    

    


    
}