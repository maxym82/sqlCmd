package ua.com.maksym82.integration;

import ua.com.maksym82.Main;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class integrationTest {


    private static testInputStream in;
    private static testOutputStream out;


    @BeforeClass
    public static void createEnv () {
        in = new testInputStream();
        out = new testOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        in.addInput("connect tracklist maksym");
        in.addInput("password");
        in.addInput("make newtracklist");
        in.addInput("close");
        in.addInput("y");
        in.addInput("exit");
        in.addInput("y");

        Main.main(new String[0]);
    }

    @AfterClass
    public static void deleteEnv () {
        in.addInput("connect tracklist maksym");
        in.addInput("password");
        in.addInput("erase newtracklist");
        in.addInput("close");
        in.addInput("y");
        in.addInput("exit");
        in.addInput("y");

        Main.main(new String[0]);
    }
    @Before
    public void clearInOut () throws IOException {
        in.reset();
        out.clearLog();
    }

    @Test
    public void testExit () throws IOException {
        //given
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testHelpWhenNotConnected () throws IOException {
        //given
        in.addInput("help");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> On unconnected mode you can use following commands:\n" +
                "1. help. For help\n" +
                "2. connect. To connect to database\n" +
                "      Format: connect [database_name]\n" +
                "3. exit. To exit from the program\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testHelpWhenConnected () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("help");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Plese find list of available commands here:\n" +
                "1. 'clear'. This command can be used to clear the content of the table.\n" +
                "      format: clear [your_table_name], for example (clear table1)\n" +
                "2. close. This commeand is used to close connection to the database and switch to unconnected mode.\n" +
                "3. create. This command is used to create new table on the database you are connected to.\n" +
                "      Format: create [your_table_name] [column1|column1_type] [column2|column2_type]...\n" +
                "      for example (create table1 column1|text column2|int)\n" +
                "      Available column types: \"BOOL\", \"CHAR\", \"VARCHAR\", \"TEXT\", \"SMALLINT\", \"INT\",\n" +
                "        \"SERIAL\", \"float\", \"DATE\", \"TIME\", \"TIMESTAMP\", \"INTERVAL\",\n" +
                "        \"UUID\", \"Array\", \"JSON\", \"hstore\"\n" +
                "4. delete. This command is used to delete row on a table, where columnValue = Value\n" +
                "      Format: delete [table_name] [column|value]\n" +
                "      For example (delete table1 column1|'any_text')\n" +
                "      In this case, any row that contains value that equels 'any_text' at column1 will be deleted\n" +
                "5. drop. This command is used to drop the table\n" +
                "      Format drop [table_name]. For example (drop table1) table with name table1 will be drepped.\n" +
                "6. find This command is used to print out content of the table.\n" +
                "      Format: find [table_name]. For example (find table1) will print out content of the table1\n" +
                "7. pwd. This command is used to print the name of connected database\n" +
                "8. help. This command is used to get help)))))))\n" +
                "9. insert. This command is used to inser new row on a table.\n" +
                "      Format: insert [table_name] [column1|value] [column2|value]...\n" +
                "      For example (insert table1 column1|'any_text' column2|12) will insert new row into table table1        with corresponding values column1 = 'any_text' and column2 = 12\n" +
                "10. list. This command is used to drint all available tables.\n" +
                "11. update. This command is used to update value1 on a column1 where value on a column0 = value0\n" +
                "      Format update table_name column0|value0 colunm1|value1\n" +
                "      For example (update table1 column1|'roadHouse' column2|123) in this case row with value        'roadHouse' on a column1 will be found and column2's value of this row will be changed to 123\n" +
                "12. make. To create new database\n" +
                "      Format: make [database_name]\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testUsupportedCommand () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("anycommand");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> This command is not supported, please type help for \"help\"\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }



    @Test
    public void testCreateDBWrongFormat () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("make");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Command format is wrong... Pleae type 'help' for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testCreateDBAlreadyExist () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("make newtracklist");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> java.sql.SQLException: ERROR: database \"newtracklist\" already exists\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void dropDBWrongFormat () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("erase");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Command format is wrong... Pleae type 'help' for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void dropDBWDoesNotExist () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("erase anyDBxxxxx");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> java.sql.SQLException: ERROR: database \"anydbxxxxx\" does not exist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testClearWithoutConnect () throws IOException {
        //given
        in.addInput("clear anyTable");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> This command is not supported on this mode, please type help for \"help\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testCloseWithoutConnect () throws IOException {
        //given
        in.addInput("close");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> This command is not supported on this mode, please type help for \"help\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testDeleteWithoutConnect () throws IOException {
        //given
        in.addInput("delete songs|'song1'");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> This command is not supported on this mode, please type help for \"help\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testDropWithoutConnect () throws IOException {
        //given
        in.addInput("drop");
        in.addInput("exit");
        in.addInput("Y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> This command is not supported on this mode, please type help for \"help\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testConnect () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testConnectError () throws IOException {
        //given
        in.addInput("connect anydb maksym");
        in.addInput("password");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: something went wrong, please check your database name, user name and password\n" +
                "FATAL: database \"anydb\" does not exist\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }




    @Test
    public void testConnectWrongInput () throws IOException {
        //given
        in.addInput("connect newtracklist");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> Incorrect command format, please type \"help\" for help\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testUnsupported () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("anything");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> This command is not supported, please type help for \"help\"\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testClearTable () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|'track1' track_id|12");
        in.addInput("insert tracks track_name|'track2' track_id|21");
        in.addInput("clear tracks");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been successfully cleaned!\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testClearTableIncorrectFormat () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("clear");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Incorrect format, please type \"help\" for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testClearTableIncorrectTable () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("clear tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> org.postgresql.util.PSQLException: ERROR: relation \"tracks\" does not exist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testDropIncorrectFormat () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("drop");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Incorrect format, please type \"help\" for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testDropIncorrectTable () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> org.postgresql.util.PSQLException: ERROR: table \"tracks\" does not exist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }




    @Test
    public void testInsertIncorrectFormat () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Incorrect command format, please type \"help\" for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testInsertIncorrectValue () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|21 track_id|text");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Something went wrong \n" +
                "java.sql.SQLException: Check data you entered\n" +
                "org.postgresql.util.PSQLException: ERROR: column \"text\" does not exist\n" +
                "  Position: 55\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testInsertIncorrectTable () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("insert tracks1 track_name|21 track_id|text");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Something went wrong \n" +
                "java.lang.RuntimeException: Table \"tracks1\" does not exist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testFind () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|'track1' track_id|12");
        in.addInput("insert tracks track_name|'track2' track_id|21");
        in.addInput("find tracks");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> \n" +
                "track_name  track_id  \n" +
                "track1      12        \n" +
                "track2      21        \n" +
                "\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testFindIncorrectFormat () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("find");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Incorrect command format, please type \"help\" for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testFindIncorrectName () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("find tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> org.postgresql.util.PSQLException: ERROR: relation \"tracks\" does not exist\n" +
                "  Position: 16\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testCreateTable () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testDeleteValue () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|'track1' track_id|12");
        in.addInput("insert tracks track_name|'track2' track_id|21");
        in.addInput("delete tracks track_id|12");
        in.addInput("Y");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to delete row that contains Value track_id = 12\n" +
                "Plese confirm (Y/N): Following row was deleted fron the table \"tracks:\n" +
                "\n" +
                "track_name  track_id  \n" +
                "track1      12        \n" +
                "\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testDeleteIncorrectFormat () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|'track1' track_id|12");
        in.addInput("insert tracks track_name|'track2' track_id|21");
        in.addInput("delete tracks");
        in.addInput("Y");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Incorrect command format, please type \"help\" for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> This command is not supported, please type help for \"help\"\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testDeleteIncorrectTable () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("delete tracks track_id|12");
        in.addInput("Y");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to delete row that contains Value track_id = 12\n" +
                "Plese confirm (Y/N): Something went wrong \n" +
                "java.sql.SQLException: org.postgresql.util.PSQLException: ERROR: relation \"tracks\" does not exist\n" +
                "  Position: 16\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testGetDataBaseName () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("pwd");;
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testListTablesWhenEmpty () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("list");;
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> On Data Base \" newtracklist \" you can find this tables available: \n" +
                "No tables available. DB is empty\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testListTables () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("create artists artist_name|text artist_id|int");
        in.addInput("list");
        in.addInput("drop tracks");
        in.addInput("drop artists");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"artists\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> On Data Base \" newtracklist \" you can find this tables available: \n" +
                "+------------------+\n" +
                "| 1 |    artists   |\n" +
                "+------------------+\n" +
                "+-----------------+\n" +
                "| 2 |    tracks   |\n" +
                "+-----------------+\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"artists\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testListTablesWrongInput () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("list something");;
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Incorrect command format, please type \"help\" for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testUpdateValue () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|'track1' track_id|12");
        in.addInput("insert tracks track_name|'track2' track_id|21");
        in.addInput("update tracks track_name|'track1' track_id|121");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Following row will be modified:\n" +
                "track_name  track_id  \n" +
                "track1      12        \n" +
                "\n" +
                "New row:\n" +
                "track_name  track_id  \n" +
                "track1      121       \n" +
                "\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testUpdateValueWrongInput () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|'track1' track_id|12");
        in.addInput("insert tracks track_name|'track2' track_id|21");
        in.addInput("update tracks");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Command format is wrong... Pleae type 'help' for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testCreateTableWrongFormat () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Command format is wrong... Pleae type 'help' for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testCreateTableWrongData () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks trak|anything");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> java.lang.RuntimeException: This data type \"anything\" is not supported. Type \"help\" for help\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> org.postgresql.util.PSQLException: ERROR: table \"tracks\" does not exist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testUpdateValueWrongCondition () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|'track1' track_id|12");
        in.addInput("insert tracks track_name|'track2' track_id|21");
        in.addInput("update tracks track_name|'track4' track_id|121");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Following row will be modified:\n" +
                "track_name  track_id  \n" +
                "\n" +
                "Please check data you entered\n" +
                "There is no such an values in this table\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

    @Test
    public void testUpdateValueWrongValue () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("create tracks track_name|text track_id|int");
        in.addInput("insert tracks track_name|'track1' track_id|12");
        in.addInput("insert tracks track_name|'track2' track_id|21");
        in.addInput("update tracks track_name|'track1' track_id|text");
        in.addInput("drop tracks");
        in.addInput("close");
        in.addInput("Y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> DB \"tracks\" has been created\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Row was successfully inserted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Following row will be modified:\n" +
                "track_name  track_id  \n" +
                "track1      12        \n" +
                "\n" +
                "Please check data you entered\n" +
                "ERROR: column \"text\" does not exist\n" +
                "  Position: 30\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> Table \"tracks\" has been deleted\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }


    @Test
    public void testCloseWithoutY () throws IOException {
        //given
        in.addInput("connect newtracklist maksym");
        in.addInput("password");
        in.addInput("close");
        in.addInput("n");
        in.addInput("close");
        in.addInput("e");
        in.addInput("close");
        in.addInput("y");
        in.addInput("exit");
        in.addInput("y");
        //when
        Main.main(new String[0]);
        //then
        String outputPattern = "Welcome to sqlCmd.\n" +
                "For list of commands available type help.\n" +
                "For help on a particular command type command following by \"?\"\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> password: You have connected to DB:newtracklist\n" +
                "You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd/newtracklist/_> You are about to close connection to DB, please confirm (Y/N): You are on connected mode\n" +
                "Type your command here\n" +
                "sqlCmd_> You are about to close connection to DB, please confirm (Y/N): Please select only Y or N: Please select only Y or N: Connection to DB \" newtracklist \" closed\n" +
                "You are on an unconnected mode.\n" +
                "Type your commend here\n" +
                "sqlCmd_> You are about to close the progran, please confirm (Y/N): Programm successfully closed\n";

        assertEquals(outputPattern, out.getData());
    }

}
