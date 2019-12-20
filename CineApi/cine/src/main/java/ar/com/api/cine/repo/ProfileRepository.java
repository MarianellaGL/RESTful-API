package ar.com.api.cine.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ar.com.api.cine.entities.UserProfile;

@Repository
public interface ProfileRepository extends MongoRepository<UserProfile, ObjectId>{

    UserProfile findBy_id(ObjectId _id);    
    UserProfile findByUsername(String username);
    UserProfile findByEmail(String email);
    UserProfile findByAlias(String alias);

}
