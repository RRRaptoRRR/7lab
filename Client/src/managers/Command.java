package managers;

import data.LabWork;

import java.io.Serializable;

public class Command implements Serializable {

    private String name;

    private String args;

    private LabWork labWork;


    public Command(String command){
        this.name = command;
    }
    public Command(String command, String args){
        this.name = command;
        this.args = args;
    }
    public Command(String command, String args, LabWork labWork){
        this.name = command;
        this.args = args;
        this.labWork = labWork;
    }

    public void setLabWork(LabWork labWork) {
        this.labWork = labWork;
    }

    public String getName() {
        return name;
    }

    public String getArgs(){
        return args;
    }
    public LabWork getLabWork() {
        return labWork;
    }



    @Override
    public String toString(){
        //if (labWork.equals(null))
        if (labWork!=null){
            if (args!=null){
                return "name: "+ getName()+ "\n" + "args: "+ getArgs() + "\n" + labWork.toString();
            }
            else {
                return "name: "+getName()+ "\n" + labWork.toString();
            }
        }
        else {
            if (args!=null){
                return "name: "+getName()+ "\n" + "args: "+getArgs();
            }
            else return "name: "+getName();
        }
    }
}
