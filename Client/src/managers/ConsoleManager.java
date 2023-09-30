package managers;

import data.Coordinates;
import data.Difficulty;
import data.LabWork;
import data.Person;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;


public class ConsoleManager {
    private Scanner scanner;

    //private CommandManager commandManager;
    private DataAsker dataAsker;
    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;
    private User user;

    public ConsoleManager(Scanner scanner, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, User user) throws IOException{

        //this.objectInputStream = new ObjectInputStream(inputStream);
        //this.objectOutputStream = new ObjectOutputStream(outputStream);
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.scanner=scanner;
        this.dataAsker = new DataAsker(this);
        this.user = user;
        start();
    }
    public  void start(){//CommandManager commandManager){

        //this.commandManager=commandManager;
        print("Введите help, чтобы узнать доступные команды");
        while (true){
            System.out.print("$");
            //print("$");
            String [] Input;
            String commant=null;
            String arg= null;
            Input=input().trim().split(" ");
            if (Input.length==1){
                commant=Input[0];
            }
            else if(Input.length==2) {
                commant=Input[0];
                arg=Input[1];
            }
            else{
                print("Введено слишком много аргументов");
                continue;
            }
            Command command;
            commant = commant.trim().toLowerCase();
            switch (commant){
                case "help": command = new Command("help"); break;
                case "info": command = new Command("info"); break;
                case "show": command = new Command("show"); break;
                case "add": command = new Command("add", null, askData()); break;
                case "update": command = new Command("update", arg, askData()); break;
                case "remove_by_id": command = new Command("remove_by_id", arg); break;
                case "clear": command = new Command("clear"); break;
                //case "read": command = new Command("read", arg); break;
                //case "save": command = new Command("save", arg); break;
                case "execute_script": command = new Command("execute_script", arg);break;

                case "exit": command = new Command("exit"); print("Вы закончили работу");System.exit(0);
                case "add_if_max": command = new Command("add_if_max", null, askData()); break;
                case "add_if_min": command = new Command("add_if_min", null, askData()); break;
                case "history": command = new Command("history"); break;
                case "remove_all_by_difficulty": command = new Command("remove_all_by_difficulty", arg); break;
                case "remove_any_by_minimal_point": command = new Command("remove_any_by_minimal_point", arg); break;
                case "filter_by_difficulty": command = new Command("filter_by_difficulty", arg); break;
                default:
                    print("Команда не распознана" +
                            "Введите help, чтобы узнать доступные команды");
                    continue;
            }


            sendToServer(command);
            if (command.getName().equals("exit")){
                break;
            }
            //commandManager.RunCommand(command, arg);

        }
    }

    public void print(String printable){
        System.out.println(printable);
    }
    public String input(String print){
        System.out.println(print);
        String str=scanner.nextLine();
        return str;
    }
    public String input(){
        String str=scanner.nextLine();
        return str;
    }

    public void sendToServer(Command command){
        try {
            objectOutputStream.writeObject(command);
            //objectOutputStream.flush();
            getFromServer();
        }
        catch (IOException ex){
            print("Сервер упал :(");
            String trying = "yes"; //= input("Будем пытаться подключиться к серверу через 10 секунд?(yes or no)").toLowerCase();
            boolean flag = false;
            Socket socket;
            while (trying.equals("yes")){
                trying = input("Будем пытаться подключиться к серверу через 10 секунд?(yes or no)").toLowerCase();
                if (trying.equals("no")) break;
                if (TryToReconnect()){
                    flag = true;
                    break;
                }
            }
            if (!flag){
                print("Сожалеем, что наш сервер упал ;(");
                System.exit(0);
            }
        }
    }

    public void getFromServer(){
        try {
            Result result = (Result) objectInputStream.readObject();
            print(result.getMessage());
            if (!result.getResult()){
                LabWork.reduceId();
            }
        }
        catch (IOException ex){
            print("Сервер упал :(");
            String trying = "yes"; //= input("Будем пытаться подключиться к серверу через 10 секунд?(yes or no)").toLowerCase();
            boolean flag = false;
            Socket socket;
            while (trying.equals("yes")){
                trying = input("Будем пытаться подключиться к серверу через 10 секунд?(yes or no)").toLowerCase();
                if (trying.equals("no")) break;
                if (TryToReconnect()){
                    flag = true;
                    break;
                }
            }
            if (!flag){
                print("Сожалеем, что наш сервер упал ;(");
                System.exit(0);
            }
        }
        catch (ClassNotFoundException ex){
            print(ex.getMessage());
        }

    }

    public LabWork askData(){
        String LabName = dataAsker.AskLabName();
        print("Введите координаты:");
        int X = dataAsker.AskX();
        int Y = dataAsker.AskY();
        float Point =dataAsker.AskPoint();
        Difficulty Difficult = dataAsker.AskDifficult();
        String Name = dataAsker.AskPersonName();
        long height = dataAsker.AskHeight();
        Integer weight = dataAsker.AskWeight();
        LabWork laba = new LabWork(LabName, new Coordinates(X, Y), Point, Difficult,
                new Person(Name, height, weight));
        return laba;
    }

    public boolean TryToReconnect(){
        try {
            Socket socket = new Socket("127.0.0.1", 8000);
            if(socket.isConnected()){
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                this.objectInputStream = new ObjectInputStream(inputStream);
                this.objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(user);
                try {
                    objectInputStream.readObject();
                }
                catch (ClassNotFoundException ex){
                    print("vso ploho");
                }
                print("Удалось переподключится к серверу");
                return true;
            }
            return false;
        }
        catch (UnknownHostException exe){
            print(exe.getMessage());
            return false;
        }
        catch (IOException exe){
            print("Попытка не удалась");
            return false;
        }
    }


}
