package commands;

import data.Difficulty;
import data.LabWork;
import managers.CollectionManager;
import managers.DataBase;
import managers.Result;
import managers.User;

import java.util.ArrayList;

public class FilterByDifficulty extends AbstractCollectionCommand{

    public FilterByDifficulty(CollectionManager collectionManager, DataBase dataBase){
        super("filter_by_difficulty difficulty", "show all labworls from collection, where difficulty is the same", collectionManager, dataBase);
    }

    @Override
    public Result execute(String args, LabWork labWork, User user) {
        if (args!=null){
            if (collectionManager.getSize()!=0){
                Difficulty difficult = Difficulty.HARD;
                args = args.toUpperCase();
                switch (args){
                    case "VERY_EASY": difficult = Difficulty.VERY_EASY; break;
                    case "HARD": difficult = Difficulty.HARD; break;
                    case "IMPOSSIBLE": difficult = Difficulty.IMPOSSIBLE; break;
                    case "TERRIBLE": difficult = Difficulty.TERRIBLE; break;
                    default:
                        //flag=false;
                        return new Result("Введена неверная сложность", true);
                }
                boolean flag= false;
                String answer = "";
                ArrayList<LabWork> toShow = new ArrayList<LabWork>();
                for (LabWork lab:collectionManager.getCollection()){
                    if(lab.getDifficulty() == difficult){
                        answer+=(lab.toString());
                        flag=true;
                    }
                }
                if (flag){
                    return new Result(answer, true);
                }
                else {
                    return new Result("В коллекции нет LabWork с таким Difficulty", true);
                }
            }
            else {
                return new Result("Коллекция пуста", true);
            }
        }
        else {
            return new Result("Аргумент не введен", true);
        }
    }
}
