package ar.com.ada.mongo.api.nifli.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ar.com.ada.mongo.api.nifli.entities.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, ObjectId> {
    Usuario findBy_id(ObjectId _id);    
    Usuario findByUsername(String username);
    Usuario findByEmail(String email);
}