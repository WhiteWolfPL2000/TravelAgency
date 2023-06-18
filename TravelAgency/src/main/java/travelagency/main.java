package travelagency;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("startScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 400);
        stage.setTitle("Ekran Logowania!");
        stage.setScene(scene);
        Image icon = new Image("file:travel_bus.png");
        stage.getIcons().add(icon);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}