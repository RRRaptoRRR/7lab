package commands;

import managers.Result;

import java.util.ArrayList;

public class History extends AbstractInformationCommand{

    private ArrayList<String> history;
    public History(ArrayList<String> history){
        super("history", "g");
        this.history = history;
    }

    @Override
    public Result execute(String args) {
        String answer = "";
        if(history.size()==0){
            return new Result("Вы не ввели еще ни одной команды", true);
        }
        else {
            answer+="7 последних выполненных команд:\n";
            int i =history.size()-1;
            for (int kol =0; kol<7; kol++){
                answer+=(history.get(i) + "\n");
                i--;
                if (i<0) break;
            }
            return new Result(answer, true);
        }

    }
}
