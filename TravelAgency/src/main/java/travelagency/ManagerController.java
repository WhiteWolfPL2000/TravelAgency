package travelagency;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ManagerController  implements Initializable,Validation {
    @FXML
    private TableView tableView;
    @FXML
    private Button fetchUserData;
    @FXML
    private TableColumn rank;
    @FXML
    private TableColumn IdUser;
    @FXML
    private TableColumn UserName;
    @FXML
    private ComboBox usersComboBox;
    @FXML
    private Button deleteUser;
    @FXML
    private Button logoutButton;
    @FXML
    private Button fetchPlacesData;
    @FXML
    private Button visibilityButton;
    @FXML
    private Button priceButton;
    @FXML
    private ComboBox visibilityCombobox;
    @FXML
    private ComboBox priceCombobox;
    @FXML
    private TextField priceField;
    @FXML
    private TableView tripTable;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn placeCol;
    @FXML
    private TableColumn priceCol;
    @FXML
    private TableColumn spotsCol;
    @FXML
    private TableColumn vehicleCol;
    @FXML
    private TableColumn visibilityCol;
    @FXML
    private TextField newPlace_ID;
    @FXML
    private TextField newPlace_keywords;
    @FXML
    private TextField newPlace_price;
    @FXML
    private TextField newPlace_description;
    @FXML
    private TextField newPlace_path;
    @FXML
    private TextField newPlace_country;
    @FXML
    private TextField newPlace_visibility;
    @FXML
    private TextField newPlace_transport;
    @FXML
    private TextField newPlace_spots;
    @FXML
    private Button approve;
    @FXML
    private Label warning;
    @FXML
    private Button addEmployee;
    @FXML
    private Label confirmEmployee;
    @FXML
    private Button derank;
    @FXML
    private ComboBox comboboxSpots;
    @FXML
    private TextField spotsField;
    @FXML
    private Button spotsButton;

    private Connection connection = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * setCellValueFactory ustawia "wskaźnik" na pole odpowiedniej klasy z której dane będą wrzucane do tabeli
         * W tym przypadku IdUser pobiera dane z klasy QueryOutput i do tej kolumny wrzuca tylko wartości z pola ID
         */
        IdUser.setCellValueFactory(new PropertyValueFactory<QueryOutput, String>("ID"));
        UserName.setCellValueFactory(new PropertyValueFactory<QueryOutput, String>("username"));
        rank.setCellValueFactory(new PropertyValueFactory<QueryOutput, String>("ranga"));
        fetchUserData.setOnAction(event -> {
            tableView.setItems(fetchDataToTable());
            usersComboBox.setItems(fetchDataToTable());
            usersComboBox.setConverter(new StringConverter<QueryOutput>() {
                @Override
                public String toString(QueryOutput query) {
                    if(query != null) return query.getUsername();
                    else return "";
                }

                @Override
                public QueryOutput fromString(String s) {
                    return null;
                }
            });
        });
        deleteUser.setOnAction(actionEvent -> {
            deleteUserQuery();
        });
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Kierownik.logout(event);
            }
        });
        idCol.setCellValueFactory(new PropertyValueFactory<QueryPlacesOutput,String>("placeID"));
        placeCol.setCellValueFactory(new PropertyValueFactory<QueryPlacesOutput,String>("place"));
        priceCol.setCellValueFactory(new PropertyValueFactory<QueryPlacesOutput,Integer>("price"));
        spotsCol.setCellValueFactory(new PropertyValueFactory<QueryPlacesOutput,Integer>("spots"));
        vehicleCol.setCellValueFactory(new PropertyValueFactory<QueryPlacesOutput,String>("vehicle"));
        visibilityCol.setCellValueFactory(new PropertyValueFactory<QueryPlacesOutput,Integer>("visible"));
        fetchPlacesData.setOnAction(event -> {
            tripTable.setItems(fetchPlacesToTable());
            visibilityCombobox.setItems(fetchPlacesToTable());
            visibilityCombobox.setConverter(new StringConverter<QueryPlacesOutput>() {
                @Override
                public String toString(QueryPlacesOutput query) {
                    if(query != null) return query.getPlaceID();
                    else return "";
                }

                @Override
                public QueryPlacesOutput fromString(String s) {
                    return null;
                }
            });
            priceCombobox.setItems(fetchPlacesToTable());
            priceCombobox.setConverter(new StringConverter<QueryPlacesOutput>() {
                @Override
                public String toString(QueryPlacesOutput query) {
                    if(query != null) return query.getPlaceID();
                    else return "";
                }

                @Override
                public QueryPlacesOutput fromString(String s) {
                    return null;
                }
            });
            comboboxSpots.setItems(fetchPlacesToTable());
            priceCombobox.setConverter(new StringConverter<QueryPlacesOutput>() {
                @Override
                public String toString(QueryPlacesOutput query) {
                    if(query != null) return query.getPlaceID();
                    else return "";
                }

                @Override
                public QueryPlacesOutput fromString(String s) {
                    return null;
                }
            });
        });
        visibilityButton.setOnAction(event -> changeVisibility());
        priceButton.setOnAction(event -> changePrice());
        approve.setOnAction(event->addNewPlace());
        addEmployee.setOnAction(event->newEmployee());
        derank.setOnAction(event -> derankEmployee());
        spotsButton.setOnAction(event -> changeSpots());
    }

    /**
     * Pobieranie danych do tabeli
     *
     * @return Lista z zestawem danych dodawanych do tabeli
     */
    private ObservableList<QueryOutput> fetchDataToTable() {
        ObservableList<QueryOutput> fetchedData = FXCollections.observableArrayList();
        PreparedStatement userInfoFetch = null;
        ResultSet userInfo = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            userInfoFetch = connection.prepareStatement("SELECT user_id, username, IF(p_rank = 1,'klient','pracownik') as 'ranga' FROM users WHERE p_rank <> 3");
            userInfo = userInfoFetch.executeQuery();
            while (userInfo.next()) {
                fetchedData.add(new QueryOutput(userInfo.getInt("user_id"), userInfo.getString("username"),userInfo.getString("ranga")));
            }
            return fetchedData;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void deleteUserQuery() {
        PreparedStatement deleteQuery = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            QueryOutput selectedUser = (QueryOutput) this.usersComboBox.getValue();
            deleteQuery = connection.prepareStatement("DELETE FROM data WHERE user_id = ?");
            deleteQuery.setInt(1,selectedUser.getID());
            deleteQuery.executeUpdate();
            deleteQuery = connection.prepareStatement("DELETE FROM users WHERE user_id = ?");
            deleteQuery.setInt(1,selectedUser.getID());
            deleteQuery.executeUpdate();
            tableView.getItems().clear();
            tableView.setItems(fetchDataToTable());
            usersComboBox.getItems().clear();
            usersComboBox.setItems(fetchDataToTable());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(deleteQuery != null) {
                try {
                    deleteQuery.close();
                    deleteQuery = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private ObservableList<QueryPlacesOutput> fetchPlacesToTable() {
        ObservableList<QueryPlacesOutput> fetchedData = FXCollections.observableArrayList();
        PreparedStatement userInfoFetch = null;
        ResultSet userInfo = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            userInfoFetch = connection.prepareStatement("SELECT place_id, country,price,spots,vehicle,visible FROM places");
            userInfo = userInfoFetch.executeQuery();

            while (userInfo.next()) {
                fetchedData.add(new QueryPlacesOutput(userInfo.getString("place_id"), userInfo.getString("country"),userInfo.getInt("price"),userInfo.getInt("spots"),userInfo.getString("vehicle"),userInfo.getInt("visible")));
            }
            return fetchedData;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if(userInfoFetch != null) {
                try {
                    userInfoFetch.close();
                    userInfoFetch = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(userInfo != null) {
                try {
                    userInfo.close();
                    userInfo = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void changeVisibility() {
        PreparedStatement updateVisibility = null;
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            QueryPlacesOutput comboboxValue = (QueryPlacesOutput) visibilityCombobox.getValue();
            int currentVisibility = comboboxValue.getVisible();
            int newVisibility;
            if(currentVisibility == 1) newVisibility = 0;
            else newVisibility = 1;
            updateVisibility = connection.prepareStatement("UPDATE places SET visible = ? WHERE place_id = ?");
            updateVisibility.setInt(1,newVisibility);
            updateVisibility.setString(2,comboboxValue.getPlaceID());
            updateVisibility.executeUpdate();
            tripTable.setItems(fetchPlacesToTable());
            visibilityCombobox.setItems(fetchPlacesToTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(updateVisibility != null) {
                try {
                    updateVisibility.close();
                    updateVisibility = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void changePrice() {
        PreparedStatement updatePrice = null;
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            QueryPlacesOutput comboboxValue = (QueryPlacesOutput) priceCombobox.getValue();
            String placeId = comboboxValue.getPlaceID();
            int currentPrice = comboboxValue.getPrice();
            String newPrice = priceField.getText();
            //Funkcja z walidatora
            int changedPrice = newPrice(newPrice,currentPrice);
            updatePrice = connection.prepareStatement("UPDATE places SET price = ? WHERE place_id = ?");
            updatePrice.setInt(1,changedPrice);
            updatePrice.setString(2,comboboxValue.getPlaceID());
            updatePrice.executeUpdate();
            tripTable.setItems(fetchPlacesToTable());
            priceCombobox.setItems(fetchPlacesToTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(updatePrice != null) {
                try {
                    updatePrice.close();
                    updatePrice = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void addNewPlace() {
        if(newPlace_ID.getText().isEmpty() || newPlace_keywords.getText().isEmpty() || newPlace_price.getText().isEmpty() || newPlace_description.getText().isEmpty() || newPlace_path.getText().isEmpty() || newPlace_country.getText().isEmpty() || newPlace_visibility.getText().isEmpty() || newPlace_spots.getText().isEmpty()) {
            warning.setText("Uzupełnij wszystkie pola!");
            warning.setTextFill(Color.RED);
        }
        else {
            PreparedStatement insertPlace = null;
            try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            insertPlace = connection.prepareStatement("INSERT INTO places VALUES(?,?,?,?,?,?,?,?,?)");
            insertPlace.setString(1,newPlace_ID.getText());
            insertPlace.setString(2,newPlace_keywords.getText());
            insertPlace.setInt(3,newPlacePrice(newPlace_price.getText()));
            insertPlace.setString(4,newPlace_description.getText());
            insertPlace.setString(5,newPlace_path.getText());
            insertPlace.setString(6,newPlace_country.getText());
            insertPlace.setInt(7,newPlaceVisibility(newPlace_visibility.getText()));
            insertPlace.setString(8,newPlace_transport.getText());
            insertPlace.setInt(9,newPlacePrice(newPlace_spots.getText()));
            insertPlace.executeUpdate();
            warning.setText("Dodano wycieczkę");
            warning.setTextFill(Color.GREEN);
            newPlace_ID.setText("");
            newPlace_keywords.setText("");
            newPlace_keywords.setText("");
            newPlace_price.setText("");
            newPlace_description.setText("");
            newPlace_path.setText("");
            newPlace_country.setText("");
            newPlace_visibility.setText("");
            newPlace_transport.setText("");
            newPlace_spots.setText("");

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(insertPlace != null) {
                    try {
                        insertPlace.close();
                        insertPlace = null;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(connection != null) {
                    try {
                        connection.close();
                        connection = null;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void newEmployee() {
        QueryOutput user = (QueryOutput) usersComboBox.getValue();
        PreparedStatement updateUser = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            updateUser = connection.prepareStatement("UPDATE users SET p_rank = ? WHERE user_id = ?");
            updateUser.setInt(1,2);
            updateUser.setInt(2,user.getID());
            updateUser.executeUpdate();
            confirmEmployee.setText("Dodano nowego pracownika");
            confirmEmployee.setTextFill(Color.GREEN);
            tableView.getItems().clear();
            tableView.setItems(fetchDataToTable());
            usersComboBox.getItems().clear();
            usersComboBox.setItems(fetchDataToTable());
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(updateUser != null) {
                try {
                    updateUser.close();
                    updateUser = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void derankEmployee() {
        QueryOutput user = (QueryOutput) usersComboBox.getValue();
        PreparedStatement updateUser = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            updateUser = connection.prepareStatement("UPDATE users SET p_rank = ? WHERE user_id = ?");
            updateUser.setInt(1,1);
            updateUser.setInt(2,user.getID());
            updateUser.executeUpdate();
            confirmEmployee.setText("Zdegradowano pracownika");
            confirmEmployee.setTextFill(Color.GREEN);
            tableView.getItems().clear();
            tableView.setItems(fetchDataToTable());
            usersComboBox.getItems().clear();
            usersComboBox.setItems(fetchDataToTable());
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(updateUser != null) {
                try {
                    updateUser.close();
                    updateUser = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void changeSpots() {
        PreparedStatement updatePrice = null;
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            QueryPlacesOutput comboboxValue = (QueryPlacesOutput) comboboxSpots.getValue();
            String placeId = comboboxValue.getPlaceID();
            int currentSpots = comboboxValue.getPrice();
            String newSpots = spotsField.getText();
            //Funkcja z walidatora
            int changedSpots = newPrice(newSpots,currentSpots);
            updatePrice = connection.prepareStatement("UPDATE places SET spots = ? WHERE place_id = ?");
            updatePrice.setInt(1,changedSpots);
            updatePrice.setString(2,comboboxValue.getPlaceID());
            updatePrice.executeUpdate();
            tripTable.getItems().clear();
            tripTable.setItems(fetchPlacesToTable());
            comboboxSpots.getItems().clear();
            comboboxSpots.setItems(fetchPlacesToTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(updatePrice != null) {
                try {
                    updatePrice.close();
                    updatePrice = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
