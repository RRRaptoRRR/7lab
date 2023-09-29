package managers;

import data.LabWork;
import org.postgresql.util.MD5Digest;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    private String URL;

    private static Logger logger;
    private Connection connection;
    private String adminUsername;
    private String adminPassword;



    private static final String ADD_USER_REQUEST = "INSERT INTO users  VALUES (nextval('user_seq'), ?, ?)";

    private static final String ADD_LABWORK_REQUEST = "INSERT INTO labworks VALUES(nextval('labwork_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?))";//"INSERT INTO labworks(name, x, y, time, minimalPoint, difficulty, person_name, height, weight, user_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?))";
    private static final String USER_ID_REQUEST = "SELECT id FROM users WHERE username = ?";

    private static final String CHECK_USER_REQUEST = "SELECT id FROM users WHERE username = ? AND password = ?";

    private static final String DELETE_USER_REQUEST = "DROP FROM users WHERE username = ?";

    public DataBase(String url, String username, String password){
        this.URL = url;
        this.adminUsername = username;
        this.adminPassword = password;
        logger = Logger.getLogger(DataBase.class.getName());
    }

    public void connectionToDataBase(){
        try {
            connection = DriverManager.getConnection(URL, adminUsername, adminPassword);
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

        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_REQUEST);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, MD5Hash.getMD5Hash(user.getPassword()));
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            logger.log(Level.INFO, "Пользователь с логином " +user.getUsername()+" существует, все ок");
            user.setId(resultSet.getInt(1));
            logger.log(Level.INFO, "Теперь у пользователя id = "+user.getId());
            return true;
        }
        return false;
    }

    public boolean toRegistreUser(User user){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_REQUEST);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, MD5Hash.getMD5Hash(user.getPassword()));
            int rows = preparedStatement.executeUpdate();
            logger.log(Level.INFO, "Пользователь " + user.getUsername() + " теперь зарегистрирован");
            PreparedStatement preparedStatement1 = connection.prepareStatement(USER_ID_REQUEST);
            preparedStatement1.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement1.executeQuery();
            resultSet.next();
            user.setId(resultSet.getInt(1));
            logger.log(Level.INFO, "Теперь у пользователя id = "+user.getId());
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.INFO, "Данный пользователь уже зарегистрирован");
            logger.log(Level.INFO, ex.getMessage());
            return false;
        }
    }

    public void addLabworkToDB(LabWork labWork, User user){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_LABWORK_REQUEST);
            preparedStatement.setString(1, labWork.getName());
            preparedStatement.setInt(2, labWork.getCoordinates().getX());
            preparedStatement.setInt(3, labWork.getCoordinates().getY());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(labWork.getCreationDate().toLocalDateTime()));
            preparedStatement.setFloat(5, labWork.getMinimalPoint());
            preparedStatement.setString(6, labWork.getDifficulty().toString());
            preparedStatement.setString(7, labWork.getAuthor().getName());
            preparedStatement.setLong(8, labWork.getAuthor().getHeight());
            preparedStatement.setLong(9, labWork.getAuthor().getHeight());
            preparedStatement.setInt(10, user.getId());
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
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
