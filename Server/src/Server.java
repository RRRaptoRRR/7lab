import commands.Save;
import managers.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static Logger logger;

    public static void main(String[] args)throws ClassNotFoundException {
            Class.forName("org.postgresql.Driver");
            Server server = new Server();
            logger = Logger.getLogger(Server.class.getName());
            logger.log(Level.INFO, "Запуск программы");
            server.start(args[0]);
    }

    public void start(String path){
        try {
            ServerSocket serverSocket= new ServerSocket(8000);
            logger.log(Level.INFO, "Создание серверного сокета");
            CollectionManager collectionManager = new CollectionManager();
            Save save = new Save(collectionManager);
            CommandManager commandManager = new CommandManager(collectionManager, path);
            DataBase dataBase = new DataBase("jdbc:postgresql://localhost:8080/studs", "s367128", "DzK7sCDJAPOPvHrb");
            dataBase.connectionToDataBase();
            while (true){
                Socket socket = serverSocket.accept();
                new ClientThread("client", socket, commandManager, dataBase).start();
            }
            /*Socket socket = serverSocket.accept();
            logger.log(Level.INFO, "Соединение пользователя с сервером установлено");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

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
                    if (command.getName().equals("exit")){
                        break;
                    }
                }
            }
            logger.log(Level.INFO, "Сервер прекратил свою работу");
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();*/
            //serverSocket.close();
            //save.execute("D://Итмо/Программирование/6лаба/to_save.csv");

        }
        catch (IOException ex){
            logger.log(Level.SEVERE, ex.getMessage());
        }
        /*catch (ClassNotFoundException ex){
            logger.log(Level.SEVERE, ex.getMessage());
        }*/
    }
}
