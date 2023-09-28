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
    public ClientThread(String name, Socket socket, CommandManager commandManager, DataBase dataBase){
        super(name);
        this.socket = socket;
        logger = Logger.getLogger(ClientThread.class.getName());
        this.commandManager = commandManager;
        this.dataBase = dataBase;
    }

    public void run(){
        try {
            logger.log(Level.INFO, "Соединение пользователя с сервером установлено");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            user = (User) objectInputStream.readObject();

            if (user.isSignIn()){
                if(dataBase.isUserRegistred(user)){
                    logger.log(Level.INFO, "Отправляем сообщение пользователю об успешном входе");
                    //objectOutputStream.writeBoolean(true);
                    objectOutputStream.writeObject(true);
                }
                else {
                    logger.log(Level.INFO, "Отправляем сообщение пользователю о неуспешном входе");
                    //objectOutputStream.writeBoolean(false);
                    objectOutputStream.writeObject(false);
                }
            }
            else dataBase.toRegistreUser(user);
            //objectOutputStream.flush();
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
