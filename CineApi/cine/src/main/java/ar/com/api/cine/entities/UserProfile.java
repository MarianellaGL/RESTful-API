package ar.com.api.cine.entities;

public class UserProfile {

    public String alias;
    private int creditCard;
    public String previousPurchases;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(int creditCard) {
        this.creditCard = creditCard;
    }

    public String getPreviousPurchases() {
        return previousPurchases;
    }

    public void setPreviousPurchases(String previousPurchases) {
        this.previousPurchases = previousPurchases;
    }

    public UserProfile() {
    }
    






    
}