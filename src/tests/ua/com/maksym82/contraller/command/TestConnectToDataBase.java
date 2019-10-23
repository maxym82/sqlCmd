package ua.com.maksym82.contraller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.maksym82.controller.Command;
import ua.com.maksym82.controller.command.ConnectToDataBase;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.dataBase.DataBaseOperations;
import ua.com.maksym82.view.InputOutput;

import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestConnectToDataBase {

    private InputOutput view = mock(InputOutput.class);

    private DataBaseInterface dataBase = new DataBaseOperations();
    private DataBaseInterface spyDataBase = Mockito.spy(dataBase);


    @Test
    public void testConnectIsExecutable() {
        //given
        Command command = new ConnectToDataBase(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("connect");
        boolean executable = command.isExecutable(userCommand);

        //then
        assertTrue(executable);
    }



    @Test
    public void testConnectWrongFormat() {
        //given
        Command command = new ConnectToDataBase(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("connect");
        command.execute(userCommand);

        //then
        Mockito.verify(view).outputln("Incorrect command format, please type \"help\" for help");
    }


    @Test (expected = SQLException.class)
    public void testConnectException() throws SQLException {
        Command command = new ConnectToDataBase(spyDataBase, view);

        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("connect");
        userCommand.add("table");
        userCommand.add("user");

        when(spyDataBase.connectToDataBase(userCommand.get(1), userCommand.get(2), "password")).thenReturn(true);

        command.execute(userCommand);

        Mockito.verify(view).outputln("");

    }

}
