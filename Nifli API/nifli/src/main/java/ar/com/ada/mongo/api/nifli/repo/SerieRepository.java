package ar.com.ada.mongo.api.nifli.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ar.com.ada.mongo.api.nifli.entities.Serie;


/**
 * UsuarioRepository
 */
@Repository
public interface SerieRepository extends MongoRepository<Serie, ObjectId> {
    Serie findBy_id(ObjectId _id); 
    Serie findByNombre(String nombre);
}