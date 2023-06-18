package travelagency;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable, Validation {

    @FXML
    private Button logoutButton;
    @FXML
    private Label nameOfUserLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn image;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn desc;
    @FXML
    private TableColumn price;
    @FXML
    private TableColumn reservation;
    @FXML
    private TableColumn countPerson;


    private Connection connection = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image.setCellValueFactory(new PropertyValueFactory<QueryGuest, ImageView>("image"));
        image.setStyle("-fx-alignment: CENTER; -fx-background-color: #FFFFCC;");
        name.setCellValueFactory(new PropertyValueFactory<QueryGuest, String>("country"));
        name.setStyle("-fx-alignment: CENTER; -fx-background-color: #FFFFCC;");
        name.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<QueryGuest,String>()
                {
                    @Override
                    protected void updateItem(String s, boolean b) {
                        super.updateItem(s, b);
                        if(isEmpty())
                        {
                            setText("");
                        }
                        else
                        {
                            setTextFill(Color.BLACK);
                            setFont(Font.font("Verdana", 16));
                            setText(s);
                            setTextAlignment(TextAlignment.CENTER);
                        }
                    }
                };
            }
        });
        desc.setCellValueFactory(new PropertyValueFactory<QueryGuest, Button>("info"));
        desc.setStyle("-fx-alignment: CENTER; -fx-background-color: #FFFFCC;");
        price.setCellValueFactory(new PropertyValueFactory<QueryGuest, String>("price"));
        price.setStyle("-fx-alignment: CENTER; -fx-background-color: #FFFFCC;");
        reservation.setCellValueFactory(new PropertyValueFactory<QueryGuest, Button>("reservation"));
        reservation.setStyle("-fx-alignment: CENTER; -fx-background-color: #FFFFCC;");

        price.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<QueryGuest,String >()
                {
                    @Override
                    protected void updateItem(String s, boolean b) {
                        super.updateItem(s, b);
                        if(isEmpty())
                        {
                            setText("");
                        }
                        else
                        {
                            setTextFill(Color.BLACK);
                            setFont(Font.font("Verdana", 16));
                            setText(s);
                            setTextAlignment(TextAlignment.CENTER);
                        }
                    }
                };
            }
        });
        countPerson.setCellValueFactory(new PropertyValueFactory<QueryGuest, TextField>("numeric"));
        tableView.setItems(fetchDataToTable());
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Users.logout(event);
            }
        });
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String whatSearch = searchTextField.getText();
                Users.search(whatSearch);
                tableView.setItems(fetchDataToTable());
            }
        });
    }

    private ObservableList<QueryGuest> fetchDataToTable() {
        ObservableList<QueryGuest> fetchedData = FXCollections.observableArrayList();
        PreparedStatement userInfoFetch = null;
        ResultSet userInfo = null;
        Path path = Paths.get("TravelAgencyV2\\src\\main\\resources\\img");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            userInfoFetch = connection.prepareStatement("SELECT place_id, image, country, description, price, spots FROM places WHERE key_words like CONCAT('%', ? , '%') AND visible = 1");
            String whatSearch = searchTextField.getText();
            userInfoFetch.setString(1,whatSearch);
            userInfo = userInfoFetch.executeQuery();
            int i = 0;
            while (userInfo.next()) {

                //ImageView
                String nazwa_obrazka = userInfo.getString("image");
                String sciezka = path + "\\" + nazwa_obrazka;
                File file = new File(sciezka);
                Image image = new Image(file.toURI().toString());
                ImageView imageView_solo = new ImageView();
                imageView_solo.setImage(image);

                //Button opis
                String opis_guzik = userInfo.getString("description");
                Button informacje = new Button(opis_guzik);
                informacje.setId("button_" + i);
                informacje.setTextAlignment(TextAlignment.CENTER);
                informacje.setFont(Font.font("Verdana", 16));
                BackgroundFill background_fill = new BackgroundFill(Color.rgb(255,255,204), CornerRadii.EMPTY, Insets.EMPTY);
                Background background = new Background(background_fill);
                informacje.setBackground(background);

                //NumericField
                TextField numeric = new TextField("0");
                numeric.setId("numeric" + i);

                //Button rezerwacja
                Button rezerwacja = new Button("Rezerwacja");
                rezerwacja.setId("rezerwacja_" + i);
                rezerwacja.setTextAlignment(TextAlignment.CENTER);
                rezerwacja.setFont(Font.font("Verdana", 16));
                background_fill = new BackgroundFill(Color.rgb(255,255,221), CornerRadii.EMPTY, Insets.EMPTY);
                background = new Background(background_fill);
                rezerwacja.setBackground(background);
                rezerwacja.setCursor(Cursor.HAND);
                rezerwacja.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)) );

                //Fetch
                QueryGuest data = new QueryGuest(imageView_solo, userInfo.getString("country"), informacje,userInfo.getString("price"), rezerwacja, numeric);
                fetchedData.add(data);
                i++;
                String place_id = userInfo.getString("place_id");
                rezerwacja.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int ileDoRezerwacji = newPlacePrice(numeric.getText());
                        Users.isAvailable(ileDoRezerwacji, place_id);
                    }
                });
            }
            return fetchedData;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(userInfoFetch != null)
            {
                try
                {
                    userInfoFetch.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(userInfo != null)
            {
                try
                {
                    userInfo.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setUserInformation(String username) {
        nameOfUserLabel.setText(username);
    }

    @Override
    public int newPlacePrice(String price) {
        return Validation.super.newPlacePrice(price);
    }
}
