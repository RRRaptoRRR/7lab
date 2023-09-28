package commands;

import data.LabWork;
import managers.CollectionManager;
import managers.Result;

public class RemoveById extends AbstractCollectionCommand{

    public RemoveById (CollectionManager collectionManager){
        super("remove_by_id", "remove labwork from collection by id", collectionManager);
    }

    @Override
    public Result execute(String args, LabWork labWork){
        try{
            if (args!=null){
                if (this.collectionManager.getSize()!=0){
                    long id = Long.parseLong(args);
                    for (LabWork lab:collectionManager.getCollection()){
                        if (java.lang.Long.compare(lab.getId(), id)==0){
                            collectionManager.remove(lab);

                            return new Result("labwork с таким id был успешно удален", true);
                        }
                    }
                    return new Result("В коллекции нет LabWork с таким id", false);
                }
                else{
                    return new Result("Коллекция пуста", false);

                }
            }
            else {
                return new Result("Аргумент не введен", false);
            }

        }
        catch (NumberFormatException ex) {
            return new Result("Неправильно введен аргумент", false);
        }
    }
}
