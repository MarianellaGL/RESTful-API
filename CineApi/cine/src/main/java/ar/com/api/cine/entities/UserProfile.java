package ar.com.api.cine.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserProfiles")
public class UserProfile {

    @Id
    private ObjectId _id;

    public String alias;
    public String previousPurchases;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPreviousPurchases() {
        return previousPurchases;
    }

    public void setPreviousPurchases(String previousPurchases) {
        this.previousPurchases = previousPurchases;
    }

    public UserProfile() {
    }

	public void grabar() {
	}
    






    
}