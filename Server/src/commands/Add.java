package commands;

import data.Coordinates;
import data.Difficulty;
import data.LabWork;
import data.Person;
import exceptions.InvalidInputException;
import exceptions.InvalidInputRangeException;
import managers.CollectionManager;
import managers.Result;

import java.io.BufferedReader;
import java.io.IOException;

public class Add extends AbstractCollectionCommand{

    //private CollectionManager collectionManager;

    public Add(CollectionManager collectionManager){
        super("add", "add labwork to collection", collectionManager);
        //this.collectionManager = collectionManager;
    }

    @Override
    public Result execute(String args, LabWork labWork){
        this.collectionManager.add(labWork);
        return new Result("Labwork был успешно добавлен в коллекцию", true);
    }

    public String executeFromScript(String args, BufferedReader csvReader){
        try {
            String LabName = csvReader.readLine();
            int X = Integer.parseInt(csvReader.readLine());
            int Y = Integer.parseInt(csvReader.readLine());
            float Point = Float.parseFloat(csvReader.readLine());
            if (Point<=0){
                throw new InvalidInputException("Кол-во баллов не может быть меньше или ровно 0");
            }
            Difficulty Difficult = Difficulty.HARD;

            String difficult=csvReader.readLine().toUpperCase();
            switch (difficult){
                case "VERY_EASY": Difficult = Difficulty.VERY_EASY; break;
                case "HARD": Difficult = Difficulty.HARD; break;
                case "IMPOSSIBLE": Difficult = Difficulty.IMPOSSIBLE; break;
                case "TERRIBLE": Difficult = Difficulty.TERRIBLE; break;
                default:
                    throw new InvalidInputException("Введена неверная сложность");
            }
            String PersonName=csvReader.readLine();
            long Height = Long.parseLong(csvReader.readLine());
            if(Height<=0){
                throw new InvalidInputRangeException("Рост не может быть меньше или ровно  0");
                //consoleManager.print("Рост не может быть меньше 0");
            }
            Integer Weight = Integer.parseInt(csvReader.readLine());
            if (Weight <= 0) {
                throw new InvalidInputRangeException("Вес не может быть меньше или ровно 0");
                //consoleManager.print("Вес не может быть меньше 0");
            }
            LabWork laba = new LabWork(LabName, new Coordinates(X, Y), Point, Difficult,
                    new Person(PersonName, Height, Weight));
            collectionManager.add(laba);
            return "Labwork был успешно добавлен в коллекцию";
        }
        catch (IOException ex){
             return ("Данные не считались");
        }
        catch (InvalidInputException ex){
            return ex.getMessage();
        }
        catch (InvalidInputRangeException ex){
            return ex.getMessage();
        }
        //return false;
    }
}