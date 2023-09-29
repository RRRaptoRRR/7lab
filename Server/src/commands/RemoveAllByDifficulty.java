package commands;

import data.Difficulty;
import data.LabWork;
import managers.CollectionManager;
import managers.DataBase;
import managers.Result;
import managers.User;

import java.util.ArrayList;

public class RemoveAllByDifficulty extends AbstractCollectionCommand {

    public RemoveAllByDifficulty(CollectionManager collectionManager, DataBase dataBase){
        super("remove_all_by_difficulty", "remove all labwork from collection, which have same difficulty", collectionManager, dataBase);
    }

    @Override
    public Result execute(String args, LabWork labWork, User user){
        if (args!= null){
            if (collectionManager.getSize()!=0){
                Difficulty difficult = Difficulty.HARD;
                boolean flag=true;
                args = args.toUpperCase();
                switch (args){
                    case "VERY_EASY": difficult = Difficulty.VERY_EASY; break;
                    case "HARD": difficult = Difficulty.HARD; break;
                    case "IMPOSSIBLE": difficult = Difficulty.IMPOSSIBLE; break;
                    case "TERRIBLE": difficult = Difficulty.TERRIBLE; break;
                    default:
                        return new Result("Введена неверная сложность", true);
                }
                ArrayList<LabWork> toRemove = new ArrayList<LabWork>();
                flag=false;
                for (LabWork lab:collectionManager.getCollection()) {
                    if (lab.getDifficulty() == difficult) {
                        toRemove.add(lab);
                        flag = true;
                    }
                }
                String answer = "";
                if(flag) {
                    for (LabWork lab : toRemove) {
                        collectionManager.remove(lab);
                        answer+=lab.toString();
                    }
                    return new Result("Labwork с указанным Difficult были успешно удалены. \n Вот, что было удалено\n"+ answer, true);
                }
                else {
                    return new Result("В коллекции нет LabWork с таким Difficult", true);
                }

            }
            else return new Result("Коллекция пуста", true);
        }
        else return new Result("Аргумент не введен", true);
    }
}
