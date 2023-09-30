import com.sun.tools.javac.Main;
import managers.ConsoleManager;
import managers.DataAsker;
import managers.Entrance;
import managers.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static Logger logger;

    private DataAsker dataAsker;
    private ConsoleManager consoleManager;

    private Entrance entrance;

    public static  void main(String[] args) {
        logger = Logger.getLogger(Main.class.getName());
        Client client = new Client();
        client.start();
    }

    public void start(){
        try {
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("127.0.0.1", 8000);

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream= new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            this.entrance = new Entrance(scanner, objectInputStream, objectOutputStream);
            User user= entrance.start();
            this.consoleManager = new ConsoleManager(scanner, objectInputStream, objectOutputStream, user);
            this.dataAsker = new DataAsker(consoleManager);
            outputStream.close();
            inputStream.close();
            socket.close();
        }
        catch (UnknownHostException ex){
            logger.log(Level.SEVERE, ex.getMessage());
        }
        catch (IOException ex){
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }
}
