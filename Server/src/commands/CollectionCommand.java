package commands;

import data.LabWork;
import managers.Result;
import managers.User;

public interface CollectionCommand {
    String getName();
    String getDiscription();

    Result execute(String args, LabWork labWork, User user);

}
