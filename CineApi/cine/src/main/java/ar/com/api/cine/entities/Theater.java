package ar.com.api.cine.entities;

import java.util.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Theaters")
public class Theater {
    @Id
    private ObjectId _id;
    private Map<String, CinemaSeat> cinemaSeats = new HashMap<String, CinemaSeat>();
    private boolean hasIMAX = false;
    private boolean has3D = true;
    private boolean hasDBox = false;
    private Integer floor;
    private Integer cinemaRoomNum;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Map<String, CinemaSeat> getCinemaSeats() {
        return cinemaSeats;
    }

    public void setCinemaSeats(Map<String, CinemaSeat> cinemaSeats) {
        this.cinemaSeats = cinemaSeats;
    }

    public boolean isHasIMAX() {
        return hasIMAX;
    }

    public void setHasIMAX(boolean hasIMAX) {
        this.hasIMAX = hasIMAX;
    }

    public boolean isHas3D() {
        return has3D;
    }

    public void setHas3D(boolean has3d) {
        has3D = has3d;
    }

    public boolean isHasDBox() {
        return hasDBox;
    }

    public void setHasDBox(boolean hasDBox) {
        this.hasDBox = hasDBox;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getCinemaRoomNum() {
        return cinemaRoomNum;
    }

    public void setCinemaRoomNum(Integer cinemaRoomNum) {
        this.cinemaRoomNum = cinemaRoomNum;
    }
    
    public Theater() {
    }



    
}