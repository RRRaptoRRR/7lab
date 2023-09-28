package commands;

import managers.CollectionManager;
import managers.Result;

public class Exit extends AbstractInformationCommand{

    private Save save;
    private CollectionManager collectionManager;

    public Exit(CollectionManager collectionManager){
        super("exit", "stop the programm");
        this.collectionManager =collectionManager;
        this.save = new Save(collectionManager);
    }

    @Override
    public Result execute(String args){
        //save.execute("D://Итмо/Программирование/6лаба/to_save.csv");
        System.exit(0);
        return new Result("Вы завершили программу", true);

    }
}
