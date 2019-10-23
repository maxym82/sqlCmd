package ua.com.maksym82.contraller.command;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.maksym82.controller.Command;
import ua.com.maksym82.controller.command.ListTables;
import ua.com.maksym82.dataBase.DataBaseInterface;
import ua.com.maksym82.dataBase.DataBaseOperations;
import ua.com.maksym82.view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class TestList {

    private View view = mock(View.class);

    private DataBaseInterface dataBase = new DataBaseOperations();
    private DataBaseInterface spyDataBase = Mockito.spy(dataBase);


    @Test
    public void testListIsExecutable() {
        //given
        Command command = new ListTables(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("list");
        boolean executable = command.isExecutable(userCommand);

        //then
        assertTrue(executable);
    }

    @Test
    public void testListIsNotExecutable() {
        //given
        Command command = new ListTables(dataBase, view);

        //when
        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("list1");
        boolean executable = command.isExecutable(userCommand);

        //then
        assertFalse(executable);
    }

    @Test
    public void testListTables() throws SQLException {
        Command command = new ListTables(spyDataBase, view);

        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("list");
        List<String> returnTables = new ArrayList<>();
        returnTables.add("songs");
        returnTables.add("artists");
        returnTables.add("albums");

        when(spyDataBase.getDataBaseName()).thenReturn("tracklist");
        when(spyDataBase.listTables()).thenReturn(returnTables);

        command.execute(userCommand);

        String expected = "[On Data Base \" tracklist \" you can find this tables available: " +
                ", +----------------+,     " +
                "songs   |, +----------------+," +
                " +------------------+,     " +
                "artists   |, +------------------+, " +
                "+-----------------+,     " +
                "albums   |, +-----------------+]";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).outputln(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());

    }

    @Test
    public void testListException() throws SQLException {
        Command command = new ListTables(spyDataBase, view);

        ArrayList<String> userCommand = new ArrayList<>();
        userCommand.add("list");

        when(spyDataBase.getDataBaseName()).thenReturn("tracklist");
        when(spyDataBase.listTables()).thenThrow(new SQLException("there is a problem ocure trying to list tables"));

        command.execute(userCommand);

        Mockito.verify(view).outputln("there is a problem ocure trying to list tables");

    }

}
