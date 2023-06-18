package travelagency;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label IncorrectData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JDBC.logInUser(event,loginField.getText(),passwordField.getText());
                IncorrectData.setText("Błędny login lub hasło");
            }
        });
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JDBC.changeScene(event,"sign-up.fxml","Rejestracja",null);
            }
        });
    }
}