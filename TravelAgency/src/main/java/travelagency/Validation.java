package travelagency;

public interface Validation {
    default Boolean ValidatePassword(String pwd1, String pwd2) {
        if(pwd1.equals(pwd2)) return true;
        else return false;
    }
    default String ValidateValues(String login, String name, String surname) {
        if(login.length() > 50) {
            return "Login jest zbyt długi.";
        }
        else if(name.length() > 50) {
            return "Imię jest za długie";
        }
        else if(surname.length() > 100) {
            return "Nazwisko jest za długie";
        }
        else return "";
    }
    default int newPrice(String price,int oldPrice) {
        int newPrice;
        try {
            newPrice= Integer.parseInt(price);
        }
        catch (NumberFormatException e) {
            return oldPrice;
        }
        if(newPrice<0) return oldPrice;
        else return newPrice;
    }
    default int newPlacePrice(String price) {
        int newPrice;
        try {
            newPrice= Integer.parseInt(price);
        }
        catch (NumberFormatException e) {
            return 0;
        }
        if(newPrice<0) return 0;
        else return newPrice;
    }
    default int newPlaceVisibility(String visibility) {
        int Vis;
        try {
            Vis= Integer.parseInt(visibility);
        }
        catch (NumberFormatException e) {
            return 1;
        }
        if(Vis!=0 || Vis != 1) return 1;
        else return Vis;
    }

}

