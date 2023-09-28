package commands;

import data.LabWork;
import managers.CollectionManager;
import managers.Result;

public class Clear extends AbstractCollectionCommand{

    public Clear(CollectionManager collectionManager){
        super("clear", "cleaer the collection", collectionManager);
    }

    @Override
    public Result execute(String args, LabWork labWork){
        collectionManager.clear();
        return new Result("Коллекция успешно очищена", true);
    }
}
