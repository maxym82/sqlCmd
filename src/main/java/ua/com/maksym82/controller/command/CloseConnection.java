package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.view.View;

import java.util.List;

public class CloseConnection implements Command {
    private DataBaseInterface dataBase;
    private View console;

    public CloseConnection (DataBaseInterface dataBase, View console) {
        this.console = console;
        this.dataBase = dataBase;
    }

    @Override
    public boolean isExecutable(List<String> command) {
        if (command.get(0).equals("close")) {return true;}
        else return false;
    }

    @Override
    public void execute(List<String> command) {
            String userInput = console.input("You are about to close connection to DB, please confirm (Y/N): ").toUpperCase();
        while (true) {
            if (userInput.equals("Y")) {
                try {
                    if (this.dataBase.closeConnection()) {
                        console.outputln("Connection to DB \" " + this.dataBase.getDataBaseName() + " \" closed");
                        return;
                    } else {
                        //it can not be called from unconnected mode...
                    }
                } catch (Exception e) {
                    console.outputln(e.getMessage());
                    return;}
            } else if (userInput.equals("N")) {
                return;
            } else {userInput = console.input("Please select only Y or N: ").toUpperCase();}

        }
    }
}
