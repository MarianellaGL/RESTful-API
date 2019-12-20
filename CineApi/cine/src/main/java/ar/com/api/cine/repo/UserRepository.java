package ar.com.api.cine.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ar.com.api.cine.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>{

    
    User findBy_id(ObjectId _id);    
    User findByUsername(String username);
    User findByEmail(String email);


}
