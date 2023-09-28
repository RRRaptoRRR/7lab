package commands;

import data.Coordinates;
import data.Difficulty;
import data.LabWork;
import data.Person;
import exceptions.InvalidInputException;
import exceptions.InvalidInputRangeException;
import managers.CollectionManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Read{
    private CollectionManager collectionManager;

    public Read(CollectionManager collectionManager){
        this.collectionManager=collectionManager;
    }


    public void execute(String args) {
        try {
            //String pathToCsv = "D://Итмо/Программирование/программы для лаб/default.csv";
            //args = pathToCsv;
            BufferedReader csvReader = new BufferedReader(new FileReader(args));
            String row;
            ArrayList<String []> data= new ArrayList<String[]>();
            csvReader.readLine();

            while ((row = csvReader.readLine()) != null) {
                String [] element = row.split(",");
                if (element.length != 10){
                    System.out.println("В одном из элементов файла не хватает параметров");
                    break;
                }
                else {

                    String LabName=element[1];
                    int X = Integer.parseInt(element[2]);
                    int Y = Integer.parseInt(element[3]);
                    ZonedDateTime creationDate = ZonedDateTime.parse(element[4]);
                    float Point = Float.parseFloat(element[5]);
                    if (Point<=0){
                        throw new InvalidInputException("Кол-во баллов не может быть меньше или ровно 0");
                    }
                    Difficulty Difficult = Difficulty.HARD;

                    String difficult=element[6].toUpperCase();
                    switch (difficult){
                        case "VERY_EASY": Difficult = Difficulty.VERY_EASY; break;
                        case "HARD": Difficult = Difficulty.HARD; break;
                        case "IMPOSSIBLE": Difficult = Difficulty.IMPOSSIBLE; break;
                        case "TERRIBLE": Difficult = Difficulty.TERRIBLE; break;
                        default:
                            throw new InvalidInputException("Введена неверная сложность");
                    }
                    String PersonName=element[7];
                    long Height = Long.parseLong(element[8]);
                    if(Height<=0){
                        throw new InvalidInputRangeException("Рост не может быть меньше или ровно  0");
                        //consoleManager.print("Рост не может быть меньше 0");
                    }
                    Integer Weight = Integer.parseInt(element[9]);
                    if (Weight <= 0) {
                        throw new InvalidInputRangeException("Вес не может быть меньше или ровно 0");
                        //consoleManager.print("Вес не может быть меньше 0");
                    }
                    LabWork laba = new LabWork(LabName, new Coordinates(X, Y), Point, Difficult,
                            new Person(PersonName, Height, Weight));
                    collectionManager.add(laba);
                }
                data.add(element);
            }
            csvReader.close();
        }
        catch (FileNotFoundException ex){
            System.out.println("Указан неверный путь");
        }
        catch (IOException ex){
            System.out.println("Файл не считался");
        }
        catch (NumberFormatException ex){
            System.out.println("Неправильные формат данных");
        }
        catch (InvalidInputException ex){
            System.out.println(ex.getMessage());
        }
        catch (InvalidInputRangeException ex){
            System.out.println(ex.getMessage());
        }
    }
}
