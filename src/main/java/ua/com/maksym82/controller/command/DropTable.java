package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.view.View;

import java.util.List;

public class DropTable implements Command {
    private DataBaseInterface dataBase;
    private View console;

    public DropTable (DataBaseInterface dataBase, View console) {
        this.dataBase = dataBase;
        this.console = console;
    }

    @Override
    public boolean isExecutable(List<String> command) {
        if (command.get(0).equals("drop")) {
            return true;
        } return false;
    }

    @Override
    public void execute(List<String> command) {
        if (command.size() != 2) {
            console.outputln("Incorrect format, please type \"help\" for help");
            return;
        }
        try {
            if (dataBase.dropTable(command.get(1))) {
                console.outputln("Table \"" + command.get(1) + "\" has been deleted");
            } else {
                //console.outputln("Something went wrong, check your spelling");
            }
        } catch (Exception e) {
            console.outputln(e.getMessage());
        }

    }
}
