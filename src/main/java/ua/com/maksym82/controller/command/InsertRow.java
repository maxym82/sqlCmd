package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.view.View;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InsertRow implements Command {
    private DataBaseInterface dataBase;
    private View console;

    public InsertRow (DataBaseInterface dataBase, View console) {
        this.dataBase = dataBase;
        this.console = console;
    }


    @Override
    public boolean isExecutable(List<String> command) {
        if (command.get(0).equals("insert")) {return true;}
        return false;
    }

    @Override
    public void execute(List<String> command) {
        if (command.size() < 2) {
            console.outputln("Incorrect command format, please type \"help\" for help");
        } else {
            Map<String, Object> newRow = new LinkedHashMap<>();
            for (String column: command.subList(2, command.size())) {
                newRow.put(column.split("\\|")[0], column.split("\\|")[1]);
            }
            try {
                if (dataBase.insertROW(command.get(1), newRow)) {
                    console.outputln("Row was successfully inserted");
                }
            } catch (Exception e) {
                console.outputln("Something went wrong \n" + e.toString());
            }
        }

    }
}
