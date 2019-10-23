package ua.com.maksym82;

import ua.com.maksym82.controller.CommandInvoker;

public class Main {
    public static void main(String[] args) {

        CommandInvoker commandInterface = new CommandInvoker();

        commandInterface.start();
    }
}
