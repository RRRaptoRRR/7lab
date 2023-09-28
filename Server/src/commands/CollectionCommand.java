package commands;

import data.LabWork;
import managers.Result;

public interface CollectionCommand {
    String getName();
    String getDiscription();

    Result execute(String args, LabWork labWork);
}
