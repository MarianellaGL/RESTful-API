package ar.com.ada.mongo.api.nifli.repo;



import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ar.com.ada.mongo.api.nifli.entities.Pelicula;


@Repository
public interface PeliculaRepository extends MongoRepository<Pelicula, ObjectId> {

    Pelicula findBy_id(ObjectId _id); 
    Pelicula findByNombre(String nombre);
    
}
