package managers;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    private String URL;

    private static Logger logger;
    private Connection connection;
    private String username;
    private String password;

    private static final String ADD_USER_REQUEST = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String USERNAME_REQUEST = "SELECT id FROM users WHERE username = '?'";

    private static final String CHECK_USER_REQUEST = "SELECT id FROM users WHERE username = ? AND password = ?";

    public DataBase(String url, String username, String password){
        this.URL = url;
        this.username = username;
        this.password = password;
        logger = Logger.getLogger(DataBase.class.getName());
    }

    public void connectionToDataBase(){
        try {
            connection = DriverManager.getConnection(URL, username, password);
            logger.log(Level.INFO, "Sucsessfull connection to dataBase");
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
        }
    }

    public String read(){
        String answer="";
        return answer;
    }


    public boolean isUserRegistred(User user) throws SQLException{
        /*if (user.isSignIn()){
            return true;
        }*/
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_REQUEST);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            logger.log(Level.INFO, "Пользователь с логином " +user.getUsername()+" существует, все ок");
            return true;
        }
        return false;
    }

    public void toRegistreUser(User user)throws SQLException{

        PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_REQUEST);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        //Statement statement = connection.createStatement();
        int rows = preparedStatement.executeUpdate();
        if(rows==0){
            logger.log(Level.INFO, "Данный пользователь уже зарегистрирован");
        }
        logger.log(Level.INFO, "Пользователь " + user.getUsername() + " теперь зарегистрирован");
    }
}
