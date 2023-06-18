package travelagency;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class QueryGuest {
    private ImageView image;
    private String country;
    private Button info;
    private String  price;
    private Button reservation;
    private TextField numeric;

    public QueryGuest(ImageView image, String country, Button info, String price, Button reservation, TextField numeric) {
        this.image = image;
        this.country = country;
        this.info = info;
        this.price = price;
        this.reservation = reservation;
        this.numeric = numeric;
    }

    public TextField getNumeric() {
        return numeric;
    }

    public void setNumeric(TextField numeric) {
        this.numeric = numeric;
    }

    public Button getReservation() {
        return reservation;
    }

    public void setReservation(Button reservation) {
        this.reservation = reservation;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String name) {
        this.country = name;
    }

    public Button getInfo() {
        return info;
    }

    public void setInfo(Button info) {
        this.info = info;
    }

    public String  getPrice() {
        return price;
    }

    public void setPrice(String  price) {
        this.price = price;
    }
}
