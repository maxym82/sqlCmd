package ua.com.maksym82.controller;


import ua.com.maksym82.controller.command.ClearTable;
import ua.com.maksym82.controller.command.CreateDB;
import ua.com.maksym82.controller.command.Help;
import ua.com.maksym82.controller.command.CloseConnection;
import ua.com.maksym82.controller.command.ConnectToDataBase;
import ua.com.maksym82.controller.command.CreateTable;
import ua.com.maksym82.controller.command.DeleteValue;
import ua.com.maksym82.controller.command.DropDB;
import ua.com.maksym82.controller.command.DropTable;
import ua.com.maksym82.controller.command.FindTable;
import ua.com.maksym82.controller.command.GetDataBaseName;
import ua.com.maksym82.controller.command.InsertRow;
import ua.com.maksym82.controller.command.ListTables;
import ua.com.maksym82.controller.command.UpdateValue;

import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.dataBase.DataBaseOperations;
import ua.com.maksym82.view.InputOutput;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandInvoker {
    private InputOutput inputOutput;
    private DataBaseInterface dataBase;
    private List<Command> commands;
    private Connection connection;
    private ArrayList<String> userInput;
    private String prompt;
    private final String[] commandsAvailableWhenConnected = {"clear", "close", "create", "createTable", "drop", "delete", "find", "pdn", "help", "insert", "print", "update", "exit"};
    private final String[] commandsWhenNotConnected = {"connect", "help", "exit"};

    public CommandInvoker () {
    this.inputOutput = new InputOutput();
    this.dataBase = new DataBaseOperations();
    commands = new ArrayList<>();
    userInput = new ArrayList<String>();
    this.setCommandList();
    this.setPrompt();
    }

    private void setPrompt (String line) {
        this.prompt = line;
    }

    private void setPrompt() {
        this.prompt = "sqlCmd_> ";
    }

    private void setCommandList () {
        commands.add(new ClearTable(this.dataBase, this.inputOutput));
        commands.add(new CloseConnection(this.dataBase, this.inputOutput));
        commands.add(new CreateTable(this.dataBase, this.inputOutput));
        commands.add(new DeleteValue(this.dataBase, this.inputOutput));
        commands.add(new DropTable(this.dataBase, this.inputOutput));
        commands.add(new FindTable(this.dataBase, this.inputOutput));
        commands.add(new GetDataBaseName(this.dataBase, this.inputOutput));
        commands.add(new Help(this.inputOutput));
        commands.add(new InsertRow(this.dataBase, this.inputOutput));
        commands.add(new ListTables(this.dataBase, this.inputOutput));
        commands.add(new UpdateValue(this.dataBase, this.inputOutput));
        commands.add(new CreateDB(this.dataBase, this.inputOutput));
        commands.add(new DropDB(this.dataBase, this.inputOutput));
    }

    public void start () {
        inputOutput.outputln("Welcome to sqlCmd.");
        inputOutput.outputln("For list of commands available type help.");
        inputOutput.outputln("For help on a particular command type command following by \"?\"");

        loop: while (true) {
            if (!this.dataBase.isConnected()) {
                inputOutput.outputln("You are on an unconnected mode.");
                inputOutput.outputln("Type your commend here");
                String strUserInput = inputOutput.input(prompt);
                if (strUserInput.equals("") || strUserInput == null || strUserInput.equals(" ") || strUserInput.equals("null")) {
                    System.out.println("Icorrect input, type \"help\" for help");
                    continue;
                }
                userInput = new ArrayList<String>(Arrays.asList(strUserInput.split(" +")));
                if (Arrays.stream(commandsWhenNotConnected).anyMatch(userInput.get(0)::equals)) {
                    if (userInput.get(0).equals("help")) {
                        inputOutput.outputln("On unconnected mode you can use following commands:");
                        inputOutput.outputln("1. help. For help");
                        inputOutput.outputln("2. connect. To connect to database");
                        inputOutput.outputln("      Format: connect [database_name]");
                        inputOutput.outputln("3. exit. To exit from the program");
                    }
                    if (userInput.get(0).equals("exit")) {
                        String userSelect = inputOutput.input("You are about to close the progran, please confirm (Y/N): ").toUpperCase();
                        while (true) {
//                        todo: check if i need to close any connection. should not be a problem, we did check it on a condition above
                            if (userSelect.equals("Y")){
                                inputOutput.outputln("Programm successfully closed");
                                break loop;
                            }else if (userSelect.equals("N")) {break;}
                            else {userSelect = inputOutput.input("Please select \"Y\" or \"N\" (Y/N): ").toUpperCase();}
                        }
                    }
                    if (userInput.get(0).equals("connect")) {
                        ConnectToDataBase doConnect = new ConnectToDataBase(this.dataBase, this.inputOutput);
                        if (doConnect.isExecutable(userInput)) {
                            doConnect.execute(userInput);
                            if (this.dataBase.isConnected()) {setPrompt("sqlCmd/" + this.dataBase.getDataBaseName() + "/_> ");}
                        }
                    }

                }else {
                    inputOutput.outputln("This command is not supported on this mode, please type help for \"help\"");
                }

            }
            else {
                boolean executed = false;
                inputOutput.outputln("You are on connected mode");
                inputOutput.outputln("Type your command here");
                userInput = new ArrayList<String>(Arrays.asList(inputOutput.input(prompt).split(" +")));
                for (Command command : commands) {
                    if (command.isExecutable(userInput)) {
                        executed = true;
                        command.execute(userInput);
                        if (userInput.get(0).equals("close")) {
                            setPrompt();
                            break;
                        }
                    }
                }
                if (!executed) {
                    inputOutput.outputln("This command is not supported, please type help for \"help\"");
                }

            }

        }

    }

}
