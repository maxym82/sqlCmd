package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.view.InputOutput;

import java.util.List;

public class ConnectToDataBase implements Command {
    private DataBaseInterface dataBase;
    private InputOutput console;
    private String userPassword;
    private final boolean debug = true;

    public ConnectToDataBase (DataBaseInterface dataBase, InputOutput console) {
        this.console = console;
        this.dataBase = dataBase;
    }

    @Override
    public boolean isExecutable(List<String> command) {
        return true;
    }

    @Override
    public void execute(List<String> command) {
        if (command.size() != 3) {
            console.outputln("Incorrect command format, please type \"help\" for help");
        } else {
            this.setPassword();
            try {
                if (dataBase.connectToDataBase(command.get(1), command.get(2), userPassword)) {
                    console.outputln("You have connected to DB:" + command.get(1));
                } else {
                    //here works exception
                }
            } catch (Exception e) {
                console.outputln(e.getMessage());
            }
        }

    }

    private void setPassword () {
        this.userPassword = this.console.inputPassword();
    }
}
