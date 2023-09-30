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
    public User start() throws IOException{
        System.out.println("Для доступа к коллекции вы должны аторизоваться/зарегестрироваться");

        boolean flag=true;
        String answer;
        User user = null;

        while (flag){
            System.out.println("Выберете действие, которое хотите совершить: Sign(in/up)");
            answer = scanner.nextLine();
            switch (answer){
                case "in": user=signIn(); flag =false; break;
                case "up": user = signUp(); flag = false; break;
            }
        }
        return user;
    }

    public User signIn() throws IOException{
        String login = requestLogin();
        String password = requestPassword();
        User user = new User(login, password, true);

        objectOutputStream.writeObject(user);
        //objectOutputStream.flush();
        //System.out.println("Я дошел до получения result");
        boolean result = false;
        try {
            result = (boolean) objectInputStream.readObject();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("blya");
        }

        //System.out.println("Я получил result");
        if(result){
            System.out.println("Вы успешно вошли в систему");
            return user;
        }
        else {
            System.out.println("Неверно указан логин/пароль");
            start();
            return null;
        }
    }

    public User signUp() throws IOException{
        String login = requestLogin();
        String password = requestPassword();
        User user = new User(login, password, false);

        objectOutputStream.writeObject(user);
        boolean result = false;
        try {
            result = (boolean) objectInputStream.readObject();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("blya");
        }

        //System.out.println("Я получил result");
        if(result){
            System.out.println("Вы зарегистрировались");
            return user;
        }
        else {
            System.out.println("Пользователь с таким логином уже существует");
            start();
            return null;
        }
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
