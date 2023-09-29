package managers;

import commands.Save;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread{

    Socket socket;
    private static Logger logger;
    private CommandManager commandManager;

    private User user;

    private DataBase dataBase;

    private CollectionManager collectionManager;
    public ClientThread(String name, Socket socket,  DataBase dataBase, CollectionManager collectionManager){//public ClientThread(String name, Socket socket, CommandManager commandManager, DataBase dataBase){
        super(name);
        this.socket = socket;
        logger = Logger.getLogger(ClientThread.class.getName());
        this.collectionManager = collectionManager;
        this.dataBase = dataBase;

    }

    public void run(){
        try {
            logger.log(Level.INFO, "Соединение пользователя с сервером установлено");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


            boolean flag=false;
            while (true){
                user = (User) objectInputStream.readObject();
                if (user.isSignIn()){
                    if(dataBase.isUserRegistred(user)){
                        logger.log(Level.INFO, "Отправляем сообщение пользователю об успешном входе");
                        objectOutputStream.writeObject(true);
                        break;
                    }
                    else {
                        logger.log(Level.INFO, "Отправляем сообщение пользователю о неуспешном входе");
                        objectOutputStream.writeObject(false);
                    }
                }
                else {
                    if(dataBase.toRegistreUser(user)){
                        logger.log(Level.INFO, "Пользователь успешно зарегестрирован");
                        objectOutputStream.writeObject(true);
                        break;
                    }
                    else {
                        logger.log(Level.INFO, "Данный пользователь уже зарегистрирован");
                        objectOutputStream.writeObject(false);
                    }
                }
            }
            this.commandManager = new CommandManager(collectionManager, "", dataBase, user);
            Result result;
            Command command;
            while (true){
                command = (Command) objectInputStream.readObject();
                if (!command.getName().equals("")){
                    logger.log(Level.INFO, "Получено сообщение от пользователя: "+ command.toString());
                    logger.log(Level.INFO, "Начато выполнение команды "+ command.getName());
                    result =commandManager.RunCommand(command.getName(), command.getArgs(), command.getLabWork());
                    logger.log(Level.INFO, "Команда " + command.getName() + " исполнена, передача ее результатов на сервер");
                    objectOutputStream.writeObject(result);
                    //objectOutputStream.flush();

                    if (command.getName().equals("exit")){
                        break;
                    }
                }
            }
            logger.log(Level.INFO, "Сервер прекратил свою работу");
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        }
        catch (IOException ex){
            logger.log(Level.INFO, "Net prihoda");
        }
        catch (ClassNotFoundException ex){
            logger.log(Level.INFO, "net classa");
        }
        catch (SQLException ex){
            logger.log(Level.INFO, ex.getMessage());
        }
    }
}
