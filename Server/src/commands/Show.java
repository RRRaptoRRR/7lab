package commands;

import data.LabWork;
import managers.CollectionManager;
import managers.Result;

public class Show extends AbstractInformationCommand{

    private CollectionManager collectionManager;

    public Show(CollectionManager collectionManager){
        super("show", "show the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public Result execute(String args){
        String answer="";
        if (collectionManager.getSize()!=0){
            for (LabWork labWork:collectionManager.getCollection()){
                answer+=labWork.toString();
            }
            answer+="---------------------";
            return new Result(answer, true);
        }
        else{
            return new Result("Коллекция пуста", true);
        }
    }
}
