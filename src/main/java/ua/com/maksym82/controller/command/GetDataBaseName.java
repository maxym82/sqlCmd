package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.view.View;

import java.util.List;

public class GetDataBaseName implements Command {
    private DataBaseInterface dataBase;
    private View console;

    public GetDataBaseName (DataBaseInterface dataBase, View console) {
        this.dataBase = dataBase;
        this.console = console;
    }

    @Override
    public boolean isExecutable(List<String> command) {
        if (command.get(0).equals("pwd")) {return true;}
        return false;
    }

    @Override
    public void execute(List<String> command) {
        console.outputln(this.dataBase.getDataBaseName());

    }
}
