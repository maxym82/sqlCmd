package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.view.View;

import java.util.List;

public class ClearTable implements Command {
    private DataBaseInterface dataBase;
    private String tableName;
    private View console;
    public ClearTable(DataBaseInterface dataBase, View console) {
        this.dataBase = dataBase;
        this.console = console;
    }

    @Override
    public boolean isExecutable(List<String> command) {
        if (command.get(0).equals("clear")) {return true;}
        return false;
    }

    @Override
    public void execute(List<String> command) {
        if (command.size()!= 2) {
            console.outputln("Incorrect format, please type \"help\" for help");
            return;
        }
        try {
            if (this.dataBase.clearTable(command.get(1))) {
                console.outputln("Table \"" + command.get(1) + "\" has been successfully cleaned!" );
            };
        } catch (Exception e) {
            console.outputln(e.getMessage());
        }
    }
}
