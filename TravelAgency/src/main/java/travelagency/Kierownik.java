package travelagency;

import javafx.event.ActionEvent;

public class Kierownik extends Pracownik{

    public Kierownik(String username, ActionEvent event) {
        this.username = username;
        JDBC.changeScene(event,"ManagerPanel.fxml","Panel Kierownika",this.username);
    }
}
