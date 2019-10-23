package ua.com.maksym82.contraller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.maksym82.controller.Command;
import ua.com.maksym82.controller.command.CloseConnection;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.dataBase.DataBaseOperations;
import ua.com.maksym82.view.View;

import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestClose {

    private View view = mock(View.class);

    private DataBaseInterface dataBase = new DataBaseOperations();
    private DataBaseInterface spyDataBase = Mockito.spy(dataBase);


    @Test
    public void testExit () {
        //given
        Command command = new CloseConnection(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("close");
        boolean executable = command.isExecutable(userCommand);

        //then
        assertTrue(executable);
    }

    @Test
    public void testExitWrong () {
        //given
        Command command = new CloseConnection(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("close1");
        boolean executable = command.isExecutable(userCommand);

        //then
        assertFalse(executable);
    }

    @Test
    public void testCloseRunBeforeConnect() {
        //given
        Command command = new CloseConnection(dataBase, view);

        //when

        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("close");

        when(view.input("You are about to close connection to DB, please confirm (Y/N): ")).thenReturn("y");
        command.execute(userCommand);


        //then

        Mockito.verify(view).outputln("Connection to DB \" null \" closed");

    }

    @Test
    public void testCloseException() throws SQLException {
        Command command = new CloseConnection(spyDataBase, view);

        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("close");

        when(view.input("You are about to close connection to DB, please confirm (Y/N): ")).thenReturn("y");
        when(spyDataBase.closeConnection()).thenThrow(new SQLException("there is a problem ocure trying to close connection"));

        command.execute(userCommand);

        Mockito.verify(view).outputln("there is a problem ocure trying to close connection");

    }

}
