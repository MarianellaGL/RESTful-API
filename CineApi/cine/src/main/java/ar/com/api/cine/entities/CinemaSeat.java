package ar.com.api.cine.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CinemaSeats")
public class CinemaSeat {
    @Id
    private ObjectId _id;
    
    private String seat;
    private boolean hasDBOX;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public boolean isHasDBOX() {
        return hasDBOX;
    }

    public void setHasDBOX(boolean hasDBOX) {
        this.hasDBOX = false;
    }





    
}