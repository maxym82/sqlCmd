package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.view.View;

import java.sql.SQLException;
import java.util.List;

public class CreateDB implements Command {
    private DataBaseInterface dataBase;
    private View console;

    public CreateDB (DataBaseInterface dataBase, View console) {
        this.dataBase = dataBase;
        this.console = console;
    }

    @Override
    public boolean isExecutable(List<String> command) {
        if (command.get(0).equals("make")) {return true;}
        return false;
    }

    @Override
    public void execute(List<String> command) {
        if (command.size() < 2) {
            console.outputln("Command format is wrong... Pleae type \'help\' for help");
        } else {
            try {
                if (dataBase.createDB(command.get(1))) {
                    console.outputln("Data Base '" + command.get(1) + "' has been created");
                }
            } catch (SQLException e) {
                console.outputln(e.toString());
            }
        }

    }
}
