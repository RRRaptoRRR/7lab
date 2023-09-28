package commands;

import managers.CollectionManager;
import managers.CommandManager;
import managers.Result;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteScript extends AbstractInformationCommand{

    private CollectionManager collectionManager;
    private CommandManager commandManager;
    public ExecuteScript(CollectionManager collectionManager, CommandManager commandManager){
        super("execute_script", "excecute script from path");
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Result execute(String args) {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(args));
            String row;
            String answer="";
            while ((row = csvReader.readLine()) != null) {
                String [] Input;
                String command=null;
                String arg= null;
                Input=row.trim().split(" ");
                if (Input.length==1){
                    command=Input[0];
                }
                else if(Input.length==2) {
                    command=Input[0];
                    arg=Input[1];
                }
                else{
                    answer += "В команде "+command+"введено слишком много аргументов";
                    continue;
                }
                answer += "\n"+commandManager.RunCommandFromScript(command, arg, csvReader);
            }
            csvReader.close();
            return new Result(answer, true);
        }
        catch (IOException ex){
            return new Result("Файл не считался", true);
        }
    }
}
