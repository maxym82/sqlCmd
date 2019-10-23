package ua.com.maksym82.view;

import java.io.Console;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputOutput implements View {
    private Scanner userInput;
    @Override
    public String input(String prompt) {
        System.out.print(prompt);
        try {
            userInput = new Scanner(System.in);
            return userInput.nextLine();
        } catch (NoSuchElementException e) {return null;}
    }

    @Override
    public void outputln(String text) {
        System.out.println(text);
    }

    @Override
    public void output(String text) {
        System.out.print(text);
    }


    public String inputPassword () {
        Console console = System.console();
        if (console == null) {
            //running from IDE...
            return this.input("password: ");

        }
        return String.valueOf(console.readPassword("password: "));
    }


    public void outputln(List<List<String>> table) {
        System.out.println(this.formatAsTable(table));;
    }

    public static String formatAsTable(List<List<String>> rows)
    {
        if (rows.size() == 0) {
            return rows.toString();
        }
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows)
        {
            for (int i = 0; i < row.size(); i++)
            {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths)
        {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (List<String> row : rows)
        {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }
}
