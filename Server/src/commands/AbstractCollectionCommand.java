package commands;

import managers.CollectionManager;

abstract public class AbstractCollectionCommand implements CollectionCommand {

    private String name;
    private String discription;
    protected CollectionManager collectionManager;
    public AbstractCollectionCommand(String name, String discription, CollectionManager collectionManager){
        this.name=name;
        this.discription=discription;
        this.collectionManager = collectionManager;
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
