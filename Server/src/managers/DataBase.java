package managers;

import data.Coordinates;
import data.Difficulty;
import data.LabWork;
import data.Person;
import exceptions.InvalidInputException;
import org.postgresql.util.MD5Digest;

import java.math.BigInteger;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    private String URL;

    private static Logger logger;
    private Connection connection;
    private String adminUsername;
    private String adminPassword;

    private CollectionManager collectionManager;



    private static final String ADD_USER_REQUEST = "INSERT INTO users  VALUES (nextval('user_seq'), ?, ?)";

    private static final String ADD_LABWORK_REQUEST = "INSERT INTO labworks VALUES(nextval('labwork_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String READ_DB = "SELECT * FROM labworks;";
    private static final String USER_ID_REQUEST = "SELECT id FROM users WHERE username = ?";

    private static final String CHECK_USER_REQUEST = "SELECT id FROM users WHERE username = ? AND password = ?";

    private static final String MAX_ID_IN_LABWORKS ="select max(id) from labworks";
    private static final String CURRENT_LABWORK_ID = "SELECT setval('labwork_seq', ?)";//"alter sequence labwork_seq restart with ?";

    private static final String DELETE_USER_REQUEST = "DROP FROM users WHERE username = ?";
    private static final String DELETE_ALL_lABWORKS_BY_USER_ID_REQUEST = "DELETE FROM labworks WHERE user_id = ?";
    private static final String DELETE_ALL_BY_DIFFICULT = "delete from labworks where user_id = ? and difficulty = ?";
    private static final String DELETE_ANY_BY_MINPOINT = "delete from labworks where minimalPoint in(select minimalPoint from labworks where minimalPoint = ? limit 1) and user_id in (select user_id from labworks where user_id = ? limit 1)";

    public DataBase(String url, String username, String password, CollectionManager collectionManager){
        this.URL = url;
        this.adminUsername = username;
        this.adminPassword = password;
        logger = Logger.getLogger(DataBase.class.getName());
        this.collectionManager = collectionManager;
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

    public Boolean readToCollection(){
        try {
            collectionManager.clear();
            logger.log(Level.INFO, "Начинаю читать данные из БД");
            PreparedStatement preparedStatement = connection.prepareStatement(READ_DB);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                LabWork labWork = readOneLabwork(resultSet);
                collectionManager.add(labWork);
            }
            logger.log(Level.INFO, "Прочитал данные из БД");
            setCurrentLabworkId();
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
            logger.log(Level.INFO, "Не удалось прочитать данные из БД");
            return false;
        }
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

    public boolean removeAllLabworkByUserIdFromDB(LabWork labWork, User user){
        try {
            logger.log(Level.INFO, "Получена команда на очистку labworks в БД");
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_lABWORKS_BY_USER_ID_REQUEST);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
            readToCollection();
            logger.log(Level.INFO, "Команда на удаление labworks в БД выполнена успешно");
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
            logger.log(Level.INFO, "Команда на удаление labwork в БД не выполнена");
            return false;
        }
    }

    public boolean removeBydifficult(Difficulty difficulty, User user){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_BY_DIFFICULT);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, difficulty.toString());
            preparedStatement.executeUpdate();
            readToCollection();
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
            logger.log(Level.INFO,"Удалить не удалось");
            return false;
        }

    }
    public boolean removeAnyByMinPoint(float minpoint, User user){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ANY_BY_MINPOINT);
            preparedStatement.setFloat(1, minpoint);
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
            readToCollection();
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
            logger.log(Level.INFO, "Удалить по minpoint не удалось");
            return false;
        }
    }

    public boolean addLabworkToDB(LabWork labWork, User user){
        try {
            logger.log(Level.INFO, "Получена команда на добавление labwork в БД");
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
            preparedStatement.executeUpdate();
            readToCollection();
            logger.log(Level.INFO, "Команда на добавление labwork в БД выполнена успешно");
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
            logger.log(Level.INFO, "Команда на добавление labwork в БД не выполнена");
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

    public LabWork readOneLabwork(ResultSet resultSet) throws SQLException{
        long id = (long) resultSet.getInt(1);
        String name = resultSet.getString(2);
        Coordinates coordinates = new Coordinates(resultSet.getInt(3), resultSet.getInt(4));
        ZonedDateTime zonedDateTime = resultSet.getTimestamp(5).toLocalDateTime().atZone(ZoneId.systemDefault());
        Float minimalPoint = resultSet.getFloat(6);
        String difficult = resultSet.getString(7).toUpperCase();
        Difficulty Difficult = Difficulty.HARD;
        switch (difficult){
            case "VERY_EASY": Difficult = Difficulty.VERY_EASY; break;
            case "HARD": Difficult = Difficulty.HARD; break;
            case "IMPOSSIBLE": Difficult = Difficulty.IMPOSSIBLE; break;
            case "TERRIBLE": Difficult = Difficulty.TERRIBLE; break;
            default:
                Difficult = Difficulty.HARD;
        }
        Person person = new Person(resultSet.getString(8), resultSet.getLong(9), resultSet.getInt(10));
        LabWork labWork = new LabWork(id, name, coordinates, zonedDateTime, minimalPoint, Difficult, person);
        logger.log(Level.INFO, "Прочитал строку из БД");
        return labWork;
    }

    public int maxIdInCollection(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MAX_ID_IN_LABWORKS);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Integer id= resultSet.getInt(1);

            logger.log(Level.INFO, "Текущий id = "+ id);
            return id;

        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
            logger.log(Level.INFO, "hernya");
            return -1;
        }
    }

    public void setCurrentLabworkId(){
        try {
            Integer id = maxIdInCollection();
            if (id <=0) id = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(CURRENT_LABWORK_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            logger.log(Level.INFO, "Удалось успешно обновить id");
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
            logger.log(Level.INFO, "Id не обновлен");
        }

    }
}
