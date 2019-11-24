package ar.com.api.cine.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Movies")
public class Movie {

    @Id
    private Object _id;
    public String movieOnBoard;
    
    

    


    
}