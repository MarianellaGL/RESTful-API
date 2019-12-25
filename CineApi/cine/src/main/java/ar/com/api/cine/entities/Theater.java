package ar.com.api.cine.entities;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Theaters")
public class Theater {
    @Id
    private ObjectId _id;
    public boolean hasIMAX = false;
    public boolean has3D = true;
    public boolean hasDBox = false;
    public boolean available = true; //seria un request que llame a una api que pregunte si está disponible la pelicula para ver y en qué horario ¿Es del back o del front?
 
    

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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

    
    public Theater() {
    }



    
}