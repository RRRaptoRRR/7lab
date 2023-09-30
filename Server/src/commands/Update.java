package commands;

import data.Coordinates;
import data.Difficulty;
import data.LabWork;
import data.Person;
import exceptions.InvalidInputException;
import exceptions.InvalidInputRangeException;
import managers.CollectionManager;
import managers.DataBase;
import managers.Result;
import managers.User;

import java.io.BufferedReader;
import java.io.IOException;

public class Update extends AbstractCollectionCommand{

    //private CollectionManager collectionManager;

    public Update(CollectionManager collectionManager, DataBase dataBase){
        super("update", "update labwork in collection by id", collectionManager, dataBase);
        //this.collectionManager = collectionManager;
    }

    @Override
    public Result execute(String args, LabWork labWork, User user){
        try {
            if(args!=null){
                if (collectionManager.getSize()!=0){
                    long id = Long.parseLong(args);
                    /*for (LabWork lab:collectionManager.getCollection()){
                        if (java.lang.Long.compare(lab.getId(), id)==0){
                            //collectionManager.remove(labWork);
                            update(lab, labWork);
                            return new Result("Labwork был усешно обновлен", true);

                        }
                    }*/
                    dataBase.update(id, labWork, user);
                    return new Result("Labwork был усешно обновлен", true);

                }
                else return new Result("Коллекция пуста", false);
            }
            else {
                return new Result("Id не введен", false);
            }
        }
        catch (NumberFormatException ex){
            return new Result("Неправильно введен аргумент", false);
        }
    }

    public void update(LabWork lab, LabWork labWork){
        lab.setName(labWork.getName());
        lab.setCoordinates(labWork.getCoordinates());
        lab.setMinimalPoint(labWork.getMinimalPoint());
        lab.setDifficulty(labWork.getDifficulty());
        lab.setAuthor(labWork.getAuthor());
    }

    public String executeFromScript(String args, BufferedReader csvReader){
        try{
            if (args!=null){
                if (collectionManager.getSize()!=0){
                    long id = Long.parseLong(args);
                    boolean flag=true;
                    for (LabWork labWork:collectionManager.getCollection()){
                        if (java.lang.Long.compare(labWork.getId(), id)==0){
                            //collectionManager.remove(labWork);
                            return updateFromScript(labWork, csvReader);
                        }
                    }
                    return ("В коллекции нет LabWork с таким id");
                }
                else{
                    return ("Коллекция пуста");
                }
            }
            else {
                return ("Аргумент не введен");
            }
        }
        catch (NumberFormatException ex) {
            return ("Неправильно введен аргумент");
        }
    }

    public String updateFromScript(LabWork labWork, BufferedReader csvReader){
        try {
            String LabName = csvReader.readLine();
            labWork.setName(LabName);
            int X = Integer.parseInt(csvReader.readLine());
            int Y = Integer.parseInt(csvReader.readLine());
            labWork.setCoordinates(new Coordinates(X, Y));
            float Point = Float.parseFloat(csvReader.readLine());
            if (Point<=0){
                throw new InvalidInputException("Кол-во баллов не может быть меньше или ровно 0");
            }
            labWork.setMinimalPoint(Point);


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

            labWork.setDifficulty(Difficult);

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
            labWork.setAuthor( new Person(PersonName, Height, Weight));
            return "Labwork был усешно обновлен";
        }
        catch (IOException ex){
            return ("данные не считались");
        }
        catch (InvalidInputException ex){
            return (ex.getMessage());
        }
        catch (InvalidInputRangeException ex){
            return (ex.getMessage());
        }


    }

}
