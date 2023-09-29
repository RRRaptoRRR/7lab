package managers;

import commands.*;
import data.Coordinates;
import data.Difficulty;
import data.LabWork;
import data.Person;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;

public class CommandManager {

    private ArrayList<String> hist;
    private ArrayList<String> scriptHist;
    private CollectionManager collectionManager;
    private Add add;
    private Help help;
    private Info info;
    private Show show;
    private Update update;
    private RemoveById removeById;
    private Clear clear;
    private Exit exit;
    private AddIfMax addIfMax;
    private AddIfMin addIfMin;
    private History history;
    private RemoveAllByDifficulty removeAllByDifficulty;
    private RemoveAnyByMinimalPoint removeAnyByMinimalPoint;
    private FilterByDifficulty filterByDifficulty;
    private Read read;
    private Save save;
    private ExecuteScript executeScript;

    private DataBase dataBase;

    private User user;

    public CommandManager(CollectionManager collectionManager, String path, DataBase dataBase, User user){//, ConsoleManager consoleManager){
        hist = new ArrayList<String>();
        scriptHist = new ArrayList<String>();
        this.collectionManager=collectionManager;
        this.dataBase = dataBase;
        this.user = user;
        //this.inputStream = inputStream;
        //this.consoleManager=consoleManager;
        //this.dataAsker=new DataAsker(consoleManager);
        this.add = new Add(collectionManager, dataBase);
        this.help = new Help();
        this.info = new Info(collectionManager);
        this.show = new Show(collectionManager);
        this.update = new Update(collectionManager, dataBase);
        this.removeById = new RemoveById(collectionManager, dataBase);
        this.clear = new Clear(collectionManager, dataBase);
        this.read = new Read(collectionManager);
        this.save = new Save(collectionManager);


        this.exit = new Exit(collectionManager);
        this.addIfMax = new AddIfMax(collectionManager, dataBase);
        this.addIfMin = new AddIfMin(collectionManager, dataBase);
        this.history = new History(hist);
        this.removeAllByDifficulty = new RemoveAllByDifficulty(collectionManager, dataBase);
        this.removeAnyByMinimalPoint = new RemoveAnyByMinimalPoint(collectionManager, dataBase);
        this.filterByDifficulty = new FilterByDifficulty(collectionManager, dataBase);

        this.executeScript = new ExecuteScript(collectionManager, this);
        read.execute(path);
    }

    synchronized public Result RunCommand(String command, String args, LabWork labWork){
        command= command.toLowerCase();
        if(labWork!= null){
            labWork.setId(collectionManager.getCurrentId());
        }

        switch (command){
            case "help": hist.add(help.getName()); return help.execute(args);
            case "info": hist.add(info.getName()); return info.execute(args);
            case "show": hist.add(show.getName()); return show.execute(args);
            case "add": hist.add(add.getName()); return add.execute(args, labWork, user);
            case "update": hist.add(update.getName()); return update.execute(args, labWork, user);
            case "remove_by_id": hist.add(removeById.getName()); return removeById.execute(args, labWork, user);
            case "clear": hist.add(clear.getName()); return clear.execute(args, labWork, user);
            //case "read": read.execute(args); hist.add(read.getName()); break;
            // case "save": save.execute(args); hist.add(save.getName()); break;
            case "execute_script": scriptHist.add(args); hist.add(executeScript.getName()); return executeScript.execute(args);
            //break;

            case "exit": hist.add(exit.getName()); return exit.execute(args);
            case "add_if_max": hist.add(addIfMax.getName()); return addIfMax.execute(args, labWork, user);
            case "add_if_min": hist.add(addIfMin.getName()); return addIfMin.execute(args, labWork, user);
            case "history": hist.add(history.getName()); return history.execute(args);
            case "remove_all_by_difficulty": hist.add(removeAllByDifficulty.getName()); return removeAllByDifficulty.execute(args, labWork, user);
            case "remove_any_by_minimal_point": hist.add(removeAnyByMinimalPoint.getName()); return removeAnyByMinimalPoint.execute(args, labWork, user);
            case "filter_by_difficulty":  hist.add(filterByDifficulty.getName()); return filterByDifficulty.execute(args, labWork, user);
            default:
                return new Result("Команда не распознана" +
                        "Введите help, чтобы узнать доступные команды", true);
        }
    }

    public String RunCommandFromScript(String command, String args, BufferedReader csvReader){
        command= command.toLowerCase();
        LabWork labWork = new LabWork("1", new Coordinates(1, 1 ), (float)1, Difficulty.HARD, new Person("1", 1, 1));
        switch (command){
            case "help":  hist.add(help.getName()); return help.execute(args).getMessage();
            case "info":  hist.add(info.getName()); return info.execute(args).getMessage();
            case "show":  hist.add(show.getName()); return show.execute(args).getMessage();
            case "add":  hist.add(add.getName()); return add.executeFromScript(args, csvReader);
            case "update":  hist.add(update.getName()); return update.executeFromScript(args, csvReader);
            case "remove_by_id":  hist.add(removeById.getName()); return removeById.execute(args, labWork, user).getMessage();
            case "clear":  hist.add(clear.getName()); return clear.execute(args, labWork, user).getMessage();
            //case "read": read.execute(args); hist.add(read.getName()); break;
            //case "save": save.execute(args); hist.add(read.getName()); break;
            case "execute_script":
                if (scriptHist.size()!=0 ){
                    if (scriptHist.contains(args)){
                        scriptHist.clear();
                        return ("Данный скрипт " +args+" уже выполнялся. Пожалуйста, не делайте рекурсию *_*");
                    }
                    else {
                        scriptHist.add(args);
                        hist.add(executeScript.getName());
                        return executeScript.execute(args).getMessage();
                    }
                }
                else {
                    scriptHist.add(executeScript.getName());
                    hist.add(executeScript.getName());
                    return executeScript.execute(args).getMessage();
                }

            case "exit":  hist.add(exit.getName()); return exit.execute(args).getMessage();
            case "add_if_max":  hist.add(addIfMin.getName()); return addIfMax.executeFromScript(args, csvReader);
            case "add_if_min":  hist.add(addIfMax.getName()); return addIfMin.executeFromScript(args, csvReader);
            case "history":  hist.add(history.getName()); return history.execute(args).getMessage();
            case "remove_all_by_difficulty":  hist.add(removeAllByDifficulty.getName()); return removeAllByDifficulty.execute(args, labWork, user).getMessage();
            case "remove_any_by_minimal_point":  hist.add(removeAnyByMinimalPoint.getName()); return removeAnyByMinimalPoint.execute(args, labWork, user).getMessage();
            case "filter_by_difficulty":  hist.add(filterByDifficulty.getName()); return filterByDifficulty.execute(args, labWork, user).getMessage();
            default:
                return ("Команда не распознана" +
                        "Введите help, чтобы узнать доступные команды");
        }
        //return "";
    }
    public ArrayList<String> getScriptHist(){
        return scriptHist;
    }

}
