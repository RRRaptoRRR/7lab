package commands;

import data.LabWork;
import managers.CollectionManager;
import managers.Result;

public class RemoveAnyByMinimalPoint extends AbstractCollectionCommand{

    public RemoveAnyByMinimalPoint(CollectionManager collectionManager){
        super("remove_any_by_minimal_point", "remove_any_by_minimal_point",  collectionManager);
    }

    @Override
    public Result execute(String args, LabWork labWork){
        try {
            if(args!=null){
                if (collectionManager.getSize()!=0){
                    float minPoint = Float.parseFloat(args);
                    for(LabWork lab:collectionManager.getCollection()){
                        if(lab.getMinimalPoint()==minPoint){
                            collectionManager.remove(lab);
                            return new Result("LabWork с таким же MinimalPoint был успешно удален. \n Вот, что было удалено\n"+ lab.toString(), true);
                        }
                    }
                    return new Result("В коллекции нет LabWork с таким же MinimalPoint", true);
                }
                else {
                    return new Result("Коллекцимя пуста", true);
                }
            }
            else {
                return new Result("MinimalPoint не введен", true);
            }
        }
        catch (NumberFormatException ex){
            return new Result("Неправильный ввод числа", true);
        }
    }
}
