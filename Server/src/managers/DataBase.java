package managers;

import org.postgresql.util.MD5Digest;

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

    private static final String DELETE_USER_REQUEST = "DROP FROM users WHERE username = ?";

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
        preparedStatement.setString(2, MD5Hash.getMD5Hash(user.getPassword()));
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            logger.log(Level.INFO, "Пользователь с логином " +user.getUsername()+" существует, все ок");
            return true;
        }
        return false;
    }

    public boolean toRegistreUser(User user){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_REQUEST);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, MD5Hash.getMD5Hash(user.getPassword()));
            //Statement statement = connection.createStatement();
            int rows = preparedStatement.executeUpdate();
            logger.log(Level.INFO, "Пользователь " + user.getUsername() + " теперь зарегистрирован");
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.INFO, "Данный пользователь уже зарегистрирован");
            return false;
        }
    }

    public boolean toDropUser(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_REQUEST);
            preparedStatement.setString(1, user.getUsername());
            //preparedStatement.setString(2, MD5Hash.getMD5Hash(user.getPassword()));
            int rows = preparedStatement.executeUpdate();
            logger.log(Level.INFO, "Пользователь " + user.getUsername() + " теперь удален");
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.INFO, "Пользователя с таким логином не существует");
            return false;
        }

    }
}
