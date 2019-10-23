package ua.com.maksym82.controller;

import java.util.List;

public interface Command {

    boolean isExecutable(List<String> command);

    void execute(List<String> command);
    //void undo();
}
