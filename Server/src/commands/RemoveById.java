package commands;

import data.LabWork;
import managers.CollectionManager;
import managers.DataBase;
import managers.Result;
import managers.User;

public class RemoveById extends AbstractCollectionCommand{

    public RemoveById (CollectionManager collectionManager, DataBase dataBase){
        super("remove_by_id", "remove labwork from collection by id", collectionManager, dataBase);
    }

    @Override
    public Result execute(String args, LabWork labWork, User user){
        try{
            if (args!=null){
                if (this.collectionManager.getSize()!=0){
                    long id = Long.parseLong(args);
                    /*for (LabWork lab:collectionManager.getCollection()){
                        if (java.lang.Long.compare(lab.getId(), id)==0){
                            collectionManager.remove(lab);

                            return new Result("labwork с таким id был успешно удален", true);
                        }
                    }*/
                    dataBase.removeById(id, user);
                    return new Result("labwork с таким id был успешно удален", true);
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
