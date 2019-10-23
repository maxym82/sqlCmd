package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.dataBase.DataBaseOperations;
import ua.com.maksym82.view.InputOutput;

import java.util.List;

public class FindTable implements Command {
    private DataBaseOperations dataBase;
    private InputOutput console;
    private String tableName;

    public FindTable (DataBaseInterface dataBase, InputOutput console) {
        this.console = console;
        if (dataBase instanceof DataBaseOperations) {
            this.dataBase = (DataBaseOperations) dataBase;
        }
    }

    @Override
    public boolean isExecutable(List<String> command) {
        if (!command.get(0).equals("find")) {
            return false;
        }
        else {return true;}
    }

    @Override
    public void execute(List<String> command) {
        if (!command.get(0).equals("find") || command.size() != 2) {
            console.outputln("Incorrect command format, please type \"help\" for help");
        } else {
            try {

                List<List<String>> tableToPrint = dataBase.findTable(command.get(1));
                console.outputln("");
                console.outputln(tableToPrint);
            } catch (Exception e) {
                console.outputln(e.getMessage());
            }

        }
    }
}