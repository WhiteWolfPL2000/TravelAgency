package travelagency;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class JDBC {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username)
    {
        Parent root = null;
        if(username != null)
        {
            try{
                FXMLLoader loader = new FXMLLoader(JDBC.class.getResource(fxmlFile));
                root = loader.load();
                if(fxmlFile.equals("EmployeePanel.fxml")) {
                    EmployeeController employeeController = loader.getController();
                }
                else if(fxmlFile.equals("startScreen.fxml")) {
                    LogInController logInController = loader.getController();
                }
                else if(fxmlFile.equals("ManagerPanel.fxml")) {
                    ManagerController managerController = loader.getController();
                }
                else if(fxmlFile.equals("sign-up.fxml")) {
                    SignUpController signUpController = loader.getController();
                }
                else {
                    LoggedInController loggedInController = loader.getController();
                    loggedInController.setUserInformation(username);
                }
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        } else {
            try{
                root = FXMLLoader.load(JDBC.class.getResource(fxmlFile));
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        if(fxmlFile == "GuestPanel.fxml")
        {
            stage.setScene(new Scene(root,1100,900));
        }
        else if(fxmlFile == "EmployeePanel.fxml" || fxmlFile == "ManagerPanel.fxml"){
            stage.setScene(new Scene(root,700,500));
        }
        else
        {
            stage.setScene(new Scene(root,550,400));
        }
        stage.show();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = (bounds.getMinX() + (bounds.getWidth() - stage.getWidth()))/2;
        double y = (bounds.getMinY() + (bounds.getHeight() - stage.getHeight()))/2;
        stage.setX(x);
        stage.setY(y);
    }

    public static void signUpUser(ActionEvent event, String username, String password, String name, String lastname, String city, String address, String zipcode)
    {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement dataInsert = null;
        PreparedStatement clientIdQuery = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;
        ResultSet clientID = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();
            if(resultSet.isBeforeFirst()){
                Alert alert = new Alert(Alert.AlertType.NONE, "Użytkownik o podanej nazwie już istnieje!", ButtonType.APPLY);
                alert.show();
            }
            else
            {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, p_rank) VALUES(?, ?, 1)");
                psInsert.setString(1,username);
                psInsert.setString(2,password);
                psInsert.executeUpdate();
                clientIdQuery = connection.prepareStatement("SELECT user_id FROM users WHERE username = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                clientIdQuery.setString(1,username);
                clientID = clientIdQuery.executeQuery();
                clientID.beforeFirst();
                clientID.next();

                dataInsert = connection.prepareStatement("INSERT INTO data (name, lastname, city, address, zipcode, user_id) VALUES(?, ?, ?, ?, ?, ?)");
                dataInsert.setString(1,name);
                dataInsert.setString(2,lastname);
                dataInsert.setString(3,city);
                dataInsert.setString(4,address);
                dataInsert.setString(5,zipcode);
                dataInsert.setString(6, Integer.toString(clientID.getInt("user_id")));
                dataInsert.executeUpdate();

                changeScene(event,"startScreen.fxml", "Logowanie", username);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(clientID != null)
            {
                try
                {
                    clientID.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(resultSet != null)
            {
                try
                {
                    resultSet.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null)
            {
                try
                {
                    psCheckUserExists.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(clientIdQuery != null)
            {
                try
                {
                    clientIdQuery.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(dataInsert != null)
            {
                try
                {
                    dataInsert.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsert != null)
            {
                try
                {
                    psInsert.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
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

        }
    }
    public static void logInUser(ActionEvent event, String username, String password)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement rankQuery = null;
        ResultSet resultSet = null;
        ResultSet resultSetQuery = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_lab_po", "root", "toor");
            preparedStatement = connection.prepareStatement("SELECT password FROM users where username = ?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                String retrivedPassword = resultSet.getString("password");
                if(retrivedPassword.equals(password))
                {
                    rankQuery = connection.prepareStatement("SELECT p_rank FROM users WHERE username = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rankQuery.setString(1,username);
                    resultSetQuery = rankQuery.executeQuery();
                    resultSetQuery.beforeFirst();
                    resultSetQuery.next();
                    if(resultSetQuery.getInt("p_rank") == 1)
                    {
                        changeScene(event,"GuestPanel.fxml", "Biuro Podrozy", username);
                    }
                    else if(resultSetQuery.getInt("p_rank") == 2)
                    {
                        new Pracownik(username, event);
                    }
                    else if(resultSetQuery.getInt("p_rank") == 3)
                    {
                        new Kierownik(username, event);
                    }
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            if(resultSet != null)
            {
                try {
                    resultSet.close();
                }catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(resultSetQuery != null)
            {
                try {
                    resultSetQuery.close();
                }catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null)
            {
                try {
                    preparedStatement.close();
                }catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(rankQuery != null)
            {
                try {
                    rankQuery.close();
                }catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try {
                    connection.close();
                }catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }
}
