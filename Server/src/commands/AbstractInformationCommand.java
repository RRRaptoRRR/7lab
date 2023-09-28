package commands;

abstract public class AbstractInformationCommand implements InformationCommand{

    private String name;
    private String discription;
    public AbstractInformationCommand(String name, String discription){
        this.name=name;
        this.discription=discription;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDiscription() {
        return discription;
    }
}
