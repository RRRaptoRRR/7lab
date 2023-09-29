package commands;

import managers.CollectionManager;
import managers.DataBase;

abstract public class AbstractCollectionCommand implements CollectionCommand {

    private String name;
    private String discription;
    protected CollectionManager collectionManager;

    protected DataBase dataBase;
    public AbstractCollectionCommand(String name, String discription, CollectionManager collectionManager, DataBase dataBase){
        this.name=name;
        this.discription=discription;
        this.collectionManager = collectionManager;
        this.dataBase = dataBase;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDiscription() {
        return discription;
    }
}
