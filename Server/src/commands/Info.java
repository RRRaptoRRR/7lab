package commands;

import managers.CollectionManager;
import managers.Result;

public class Info extends AbstractInformationCommand{

    private CollectionManager collectionManager;
    public Info(CollectionManager collectionManager){
        super("info", "information about collection");
        this.collectionManager = collectionManager;
    }
    @Override
    public Result execute(String args){
        return new  Result("Тип хранимого объекта: "+ collectionManager.getCollection().getClass() +
                "\nДата инициализации: "+collectionManager.getInitDate().toString() +
                "\nКоличество элементов: "+collectionManager.getSize(), true);
    }
}
