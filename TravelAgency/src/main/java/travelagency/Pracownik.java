package travelagency;

import javafx.event.ActionEvent;

public class Pracownik extends Users {
    protected String username;

    public Pracownik() {
        this.username = "";
    }

    public Pracownik(String username, ActionEvent event) {
        super(username);
        JDBC.changeScene(event,"EmployeePanel.fxml","Panel Pracownika",this.username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
