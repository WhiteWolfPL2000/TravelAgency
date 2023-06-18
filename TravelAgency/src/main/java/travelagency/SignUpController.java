package travelagency;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable, Validation {
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField zipcodeField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button goBackButton;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!loginField.getText().trim().isEmpty() && !passwordField.getText().trim().isEmpty() && !repeatPasswordField.getText().trim().isEmpty())
                {
                    if(PasswordValidator.isValid(passwordField.getText().trim()) && ValidatePassword(passwordField.getText().trim(), repeatPasswordField.getText().trim())) {
                        String paramsCheck = ValidateValues(loginField.getText().trim(),nameField.getText().trim(),lastnameField.getText().trim());
                        if(paramsCheck.equals("")) {
                            JDBC.signUpUser(event, loginField.getText(), passwordField.getText(), nameField.getText(), lastnameField.getText(), cityField.getText(), addressField.getText(), zipcodeField.getText());
                        }
                        else errorLabel.setText(paramsCheck);
                    }
                    else {
                        errorLabel.setText("Hasła nie pokrywają się \n lub nie spełniają\n wymagań długości i/lub\n bezpieczeństwa");
                    }
                }
                else
                {
                    errorLabel.setText("Proszę podać wszystkie \n potrzebne dane");
                }

            }
        });
        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JDBC.changeScene(event,"startScreen.fxml", "Logowanie", null);
            }
        });
    }
    class PasswordValidator {

        // digit + lowercase char + uppercase char + punctuation + symbol
        private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

        private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        public static boolean isValid(final String password) {
            Matcher matcher = pattern.matcher(password);
            return matcher.matches();
        }
    }
}
