package ua.com.maksym82.contraller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.maksym82.controller.Command;
import ua.com.maksym82.controller.command.CreateDB;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.dataBase.DataBaseOperations;
import ua.com.maksym82.view.InputOutput;
import ua.com.maksym82.view.View;

import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCreateDB {

    private View view = mock(InputOutput.class);

    private DataBaseInterface dataBase = new DataBaseOperations();
    private DataBaseInterface spyDataBase = Mockito.spy(dataBase);


    @Test
    public void testCreateDBIsExecutable() {
        //given
        Command command = new CreateDB(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("make");
        boolean executable = command.isExecutable(userCommand);

        //then
        assertTrue(executable);
    }

    @Test
    public void testCreateDBIsNotExecutable() {
        //given
        Command command = new CreateDB(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("make1");
        boolean executable = command.isExecutable(userCommand);

        //then
        assertFalse(executable);
    }



    @Test
    public void testCreateDBWrongFormat() {
        //given
        Command command = new CreateDB(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("make");
        command.execute(userCommand);

        //then
        Mockito.verify(view).outputln("Command format is wrong... Pleae type 'help' for help");
    }

    @Test
    public void testCreateDB() throws SQLException {
        Command command = new CreateDB(dataBase, view);

        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("make");
        userCommand.add("newDB");

        when(spyDataBase.createDB("newDB")).thenReturn(true);

        command.execute(userCommand);

        Mockito.verify(view).outputln("Data Base 'newDB' has been created");

    }




    @Test
    public void testCreateDBException() throws SQLException {
        Command command = new CreateDB(spyDataBase, view);

        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("make");
        userCommand.add("newDataBase");


        when(spyDataBase.createDB(userCommand.get(1))).thenThrow(new SQLException("there is a problem ocure trying to create DataBase"));

        command.execute(userCommand);

        Mockito.verify(view).outputln("java.sql.SQLException: there is a problem ocure trying to create DataBase");
    }

}