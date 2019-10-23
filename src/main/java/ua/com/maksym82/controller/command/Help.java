package ua.com.maksym82.controller.command;

import ua.com.maksym82.controller.Command;
import ua.com.maksym82.view.View;

import java.util.List;

public class Help implements Command {

    private final View console;

    public Help (View console) {
        this.console = console;
    }

    @Override
    public boolean isExecutable(List<String> command) {
        if (command.get(0).equals("help")) {return true;}
        return false;
    }

    @Override
    public void execute(List<String> command) {
        console.outputln("Plese find list of available commands here:");
        console.outputln("1. 'clear'. This command can be used to clear the content of the table.");
        console.outputln("      format: clear [your_table_name], for example (clear table1)");
        console.outputln("2. close. This commeand is used to close connection to the database and switch to unconnected mode.");
        console.outputln("3. create. This command is used to create new table on the database you are connected to.");
        console.outputln("      Format: create [your_table_name] [column1|column1_type] [column2|column2_type]...");
        console.outputln("      for example (create table1 column1|text column2|int)");
        console.outputln("      Available column types: \"BOOL\", \"CHAR\", \"VARCHAR\", \"TEXT\", \"SMALLINT\", \"INT\",\n" +
                "        \"SERIAL\", \"float\", \"DATE\", \"TIME\", \"TIMESTAMP\", \"INTERVAL\",\n" +
                "        \"UUID\", \"Array\", \"JSON\", \"hstore\"");
        console.outputln("4. delete. This command is used to delete row on a table, where columnValue = Value");
        console.outputln("      Format: delete [table_name] [column|value]");
        console.outputln("      For example (delete table1 column1|'any_text')");
        console.outputln("      In this case, any row that contains value that equels 'any_text' at column1 will be deleted");
        console.outputln("5. drop. This command is used to drop the table" );
        console.outputln("      Format drop [table_name]. For example (drop table1) table with name table1 will be drepped.");
        console.outputln("6. find This command is used to print out content of the table.");
        console.outputln("      Format: find [table_name]. For example (find table1) will print out content of the table1");
        console.outputln("7. pwd. This command is used to print the name of connected database");
        console.outputln("8. help. This command is used to get help)))))))");
        console.outputln("9. insert. This command is used to inser new row on a table.");
        console.outputln("      Format: insert [table_name] [column1|value] [column2|value]...");
        console.outputln("      For example (insert table1 column1|'any_text' column2|12) will insert new row into table table1 " +
                "       with corresponding values column1 = 'any_text' and column2 = 12");
        console.outputln("10. list. This command is used to drint all available tables.");
        console.outputln("11. update. This command is used to update value1 on a column1 where value on a column0 = value0");
        console.outputln("      Format update table_name column0|value0 colunm1|value1");
        console.outputln("      For example (update table1 column1|'roadHouse' column2|123) in this case row with value " +
                "       'roadHouse' on a column1 will be found and column2's value of this row will be changed to 123");
        console.outputln("12. make. To create new database");
        console.outputln("      Format: make [database_name]");

    }
}
