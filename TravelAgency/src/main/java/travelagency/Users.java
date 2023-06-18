package travelagency;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.ArrayList;

public class Users implements Validation {
    protected String username;

    public Users() {
        username = "";
    }

    Users(String username)
    {
        this.username = username;
    }

    Users(String username, ActionEvent event){
        this.username = username;
        JDBC.changeScene(event,"GuestPanel.fxml", "Biuro Podrozy", username);
    }

    public static void logout(ActionEvent event)
    {
        JDBC.changeScene(event, "startScreen.fxml", "Logowanie", null);
    }

    public static ArrayList<String> search(String whatSearch)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> listOfPlacesID = new ArrayList<>();
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            preparedStatement = connection.prepareStatement("SELECT place_id from places where key_words LIKE CONCAT('%', ? , '%')");
            preparedStatement.setString(1,whatSearch);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                listOfPlacesID.add(resultSet.getString("place_id"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return listOfPlacesID;
    }


    public static void isAvailable(int ileDoRezerwacji, String place_id)
    {
        Connection connection = null;
        PreparedStatement insertSpots = null;
        PreparedStatement ileMiejscQuery = null;
        ResultSet ileMiejsc = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            ileMiejscQuery = connection.prepareStatement("SELECT spots from places where place_id = ?");
            ileMiejscQuery.setString(1,place_id);
            ileMiejsc = ileMiejscQuery.executeQuery();
            while(ileMiejsc.next())
            {
                int ile = ileMiejsc.getInt("spots");
                if(ile < ileDoRezerwacji || ileDoRezerwacji == 0)
                {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Brak miejsc lub niepoprawna liczba miejsc", ButtonType.APPLY);
                    alert.show();
                }
                else
                {
                    try{
                        insertSpots = connection.prepareStatement("UPDATE places SET spots = spots - ? WHERE place_id = ?");
                        insertSpots.setInt(1,ileDoRezerwacji);
                        insertSpots.setString(2,place_id);
                        insertSpots.executeUpdate();
                        Alert alert = new Alert(Alert.AlertType.NONE, "Dokonano rezerwacji",ButtonType.APPLY);
                        alert.show();

                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
