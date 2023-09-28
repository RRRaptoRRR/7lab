package managers;

import java.io.*;
import java.util.Scanner;

public class Entrance {

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;
    private Scanner scanner;

    public Entrance(Scanner scanner, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) throws IOException {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.scanner = scanner;
    }
    public void start() throws IOException{
        System.out.println("Для доступа к коллекции вы должны аторизоваться/зарегестрироваться");

        boolean flag=true;
        String answer;

        while (flag){
            System.out.println("Выберете действие, которое хотите совершить: Sign(in/up)");
            answer = scanner.nextLine();
            switch (answer){
                case "in": signIn(); flag =false; break;
                case "up": signUp(); flag = false; break;
            }
        }
    }

    public void signIn() throws IOException{
        String login = requestLogin();
        String password = requestPassword();
        User user = new User(login, password, true);

        objectOutputStream.writeObject(user);
        //objectOutputStream.flush();
        System.out.println("Я дошел до получения result");
        boolean result = false;
        try {
            result = (boolean) objectInputStream.readObject();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("blya");
        }

        System.out.println("Я получил result");
        if(result){
            System.out.println("Вы успешно вошли в систему");
        }
        else {
            System.out.println("Неверно указан логин/пароль");
            start();
        }
    }

    public void signUp() throws IOException{
        String login = requestLogin();
        String password = requestPassword();
        User user = new User(login, password, false);

        objectOutputStream.writeObject(user);
        //objectOutputStream.flush();
    }

    public String requestLogin(){
        String login;
        while (true){
            System.out.println("Введите ваш логин:");
            login = scanner.nextLine();
            if(login.equals("")){
                System.out.println("Вы ничего не ввели");
                continue;
            }
            if (login.matches("[a-zA-Z0-9_-]+")){
                return login;
            }

        }
    }
    public String requestPassword(){

        String password;
        while (true){
            System.out.println("Введите ваш пароль:");
            password = scanner.nextLine();
            if(password.equals("")){
                System.out.println("Вы ничего не ввели");
                continue;
            }
            if (password.matches("[a-zA-Z0-9а-яА-Я_-]+")){
                return password;
            }

        }
    }

}
