package commands;

import managers.Result;

public interface InformationCommand {
    String getName();
    String getDiscription();

    Result execute(String args);
}
