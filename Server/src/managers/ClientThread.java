package managers;

import commands.Save;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread{

    Socket socket;
    private static Logger logger;
    private CommandManager commandManager;

    private User user;

    private DataBase dataBase;

    private CollectionManager collectionManager;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    //private Result result = new Result("", false);
    //private Command command = new Command("", "", null);

    private final BlockingQueue<Command> queueToProcess;

    private final BlockingQueue<Result>queueToSend;
    public ClientThread(String name, Socket socket,  DataBase dataBase, CollectionManager collectionManager) throws IOException{//public ClientThread(String name, Socket socket, CommandManager commandManager, DataBase dataBase){
        super(name);
        this.socket = socket;
        logger = Logger.getLogger(ClientThread.class.getName());
        this.collectionManager = collectionManager;
        this.dataBase = dataBase;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.queueToProcess = new LinkedBlockingQueue<>();
        this.queueToSend = new LinkedBlockingQueue<>();
    }

    public void run(){
        try {

            logger.log(Level.INFO, "Соединение пользователя с сервером установлено");
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
            new Thread(() ->{
                while (true){
                    try {
                        Command command = (Command) objectInputStream.readObject();
                        queueToProcess.add(command);
                    }catch (IOException ex){
                        logger.log(Level.INFO, "Net prihoda");
                    }
                    catch (ClassNotFoundException ex){
                        logger.log(Level.INFO, "net classa");
                    }
                }
            } ).start();
            new Thread(() ->{
                Result result;
                while (true){
                    try {
                        result = queueToSend.take();
                        if(!result.equals(null)){
                            if (!result.getMessage().equals("") & !result.getMessage().equals(null)){
                                objectOutputStream.writeObject(result);
                            }
                        }
                    }
                    catch (IOException ex){

                        logger.log(Level.INFO, "Net prihoda");
                    }
                    catch (InterruptedException ex){
                        logger.log(Level.INFO, ex.getMessage());
                    }

                }
            }).start();
            Thread executeThread = new Thread(()->{
                Command command = new Command("", "", null);
                Result result;
                while (true){
                    logger.log(Level.INFO, "Нахожусь в обработке запроса");
                    try {
                        command = queueToProcess.take();
                    }
                    catch (InterruptedException ex){
                        logger.log(Level.INFO, ex.getMessage());
                    }
                    if (!command.equals(null)){
                        if (!command.getName().equals("")){
                            logger.log(Level.INFO, "Получено сообщение от пользователя: "+ command.toString());
                            logger.log(Level.INFO, "Начато выполнение команды "+ command.getName());
                            result =commandManager.RunCommand(command.getName(), command.getArgs(), command.getLabWork());
                            queueToSend.add(result);
                            logger.log(Level.INFO, "Команда " + command.getName() + " исполнена, передача ее результатов клиенту");
                            //objectOutputStream.writeObject(result);
                            //objectOutputStream.flush();

                            if (command.getName().equals("exit")){
                                break;
                            }
                        }
                    }

                }
            });
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
            executor.execute(executeThread);
            /*Command command = new Command("", "", null);
            Result result;
            while (true){
                logger.log(Level.INFO, "Нахожусь в обработке запроса");
                try {
                    command = queueToProcess.take();
                }
                catch (InterruptedException ex){
                    logger.log(Level.INFO, ex.getMessage());
                }
                if (!command.equals(null)){
                    if (!command.getName().equals("")){
                        logger.log(Level.INFO, "Получено сообщение от пользователя: "+ command.toString());
                        logger.log(Level.INFO, "Начато выполнение команды "+ command.getName());
                        result =commandManager.RunCommand(command.getName(), command.getArgs(), command.getLabWork());
                        queueToSend.add(result);
                        logger.log(Level.INFO, "Команда " + command.getName() + " исполнена, передача ее результатов клиенту");
                        //objectOutputStream.writeObject(result);
                        //objectOutputStream.flush();

                        if (command.getName().equals("exit")){
                            break;
                        }
                    }
                }
            }*/
            while (true){

            }
            /*logger.log(Level.INFO, "Сервер прекратил свою работу");
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();*/
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

    /*public void readRequest(){
        try {
            this.command = (Command) objectInputStream.readObject();
        }
        catch (IOException ex){
            logger.log(Level.INFO, "Net prihoda");
        }
        catch (ClassNotFoundException ex){
            logger.log(Level.INFO, "net classa");
        }
    }*/
    /*public void sendRequest(){
        try {
            if(!result.getMessage().equals("")){
                logger.log(Level.INFO, "Команда " + command.getName() + " исполнена, передача ее результатов клиенту");
                objectOutputStream.writeObject(result);
            }

        }
        catch (IOException ex){
            logger.log(Level.INFO, "Net prihoda");
        }
        catch (ClassNotFoundException ex){
            logger.log(Level.INFO, "net classa");
        }
    }*/

}
