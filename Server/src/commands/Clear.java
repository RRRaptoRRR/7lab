package commands;

import data.LabWork;
import managers.CollectionManager;
import managers.DataBase;
import managers.Result;
import managers.User;

public class Clear extends AbstractCollectionCommand{

    public Clear(CollectionManager collectionManager, DataBase dataBase){
        super("clear", "cleaer the collection", collectionManager, dataBase);
    }

    @Override
    public Result execute(String args, LabWork labWork, User user){
        collectionManager.clear();
        return new Result("Коллекция успешно очищена", true);
    }
}
