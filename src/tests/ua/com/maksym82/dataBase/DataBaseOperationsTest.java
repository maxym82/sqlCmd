package ua.com.maksym82.dataBase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.maksym82.dataBase.DataBaseOperations;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class DataBaseOperationsTest {
    private static DataBaseOperations operations = new DataBaseOperations();
    private static String tableName = "tracks";
    private static Map<String, Object> newTable = new LinkedHashMap<>();

    @BeforeClass
    public static void setup() throws SQLException {
        operations.connectToDataBase("tracklist", "maksym", "password");
        operations.createDB("newtracklist");
        operations.closeConnection();
        operations.connectToDataBase("newtracklist", "maksym", "password");
        newTable.put("track_name", "text");
        newTable.put("track_id", "int");

    }

    @AfterClass
    public static void close() throws SQLException {
        operations.closeConnection();
        operations.connectToDataBase("tracklist", "maksym", "password");
        operations.dropDB("newTrackList");
        operations.closeConnection();
    }

    private void createTableWithValues() throws SQLException {
        Map<String, Object> newTable = new LinkedHashMap<>();
        Map<String, Object> row1 = new LinkedHashMap<>();
        Map<String, Object> row2 = new LinkedHashMap<>();
        row1.put("track_name", "'track_1'");
        row1.put("track_id", 1);
        row2.put("track_name", "'track_2'");
        row2.put("track_id", 2);
        operations.createTable(this.tableName, this.newTable);
        operations.insertROW(this.tableName, row1);
        operations.insertROW(this.tableName, row2);

    }

    @Test
    public void isDBexistTest() throws SQLException {
        String dbExist = "newtracklist";
        String dbDoesNotExist = "AnyDB";
        assertEquals(true, operations.isDBexist(dbExist));
        assertEquals(false, operations.isDBexist(dbDoesNotExist));
    }

    @Test
    public void getDataBaseNameTest() {
        assertEquals("newtracklist", operations.getDataBaseName());

    }

    @Test
    public void createDBTestWrong() {
        try {
            assertTrue(!operations.createDB("newtracklist"));
        } catch (SQLException e) { //do nothing, this is wat is expected
        }
    }

    @Test
    public void dropDBTestWrong() {
        try {
            assertTrue(!operations.dropDB("noSuchDB"));
        } catch (SQLException e) { // do nothing, this is wat is expected

        }
    }

    @Test
    public void connectToDataBaseTest() throws SQLException {
        assertTrue(operations.isConnected());
    }

    @Test
    public void listTablesTest() throws SQLException {
        this.createTableWithValues();
        String[] tableNames = operations.listTables().toArray(new String[0]);
        assertArrayEquals(new String[] {this.tableName}, tableNames);
        operations.dropTable(this.tableName);
    }

    @Test
    public void clearTableTest() throws SQLException {
        this.createTableWithValues();
        assertTrue(operations.clearTable(this.tableName));
        operations.dropTable(this.tableName);
    }

    @Test
    public void dropTableTest() throws SQLException {
        operations.createTable(this.tableName, this.newTable);
        assertTrue(operations.dropTable(this.tableName));
    }

    @Test
    public void isConnectedTest() throws SQLException {
        assertTrue(operations.isConnected());
    }

    @Test
    public void createTableTest() throws SQLException {
        assertTrue(operations.createTable(this.tableName, this.newTable));
        operations.dropTable(this.tableName);
    }

    @Test
    public void findTableTest() throws SQLException {
        this.createTableWithValues();
        assertEquals("[[track_name, track_id], [track_1, 1], [track_2, 2]]", operations.findTable(this.tableName).toString());
        operations.dropTable(this.tableName);

    }

    @Test
    public void insertROWTest() throws SQLException {
        this.createTableWithValues();
        Map<String, Object> row3 = new LinkedHashMap<>();
        row3.put("track_name", "'track_3'");
        row3.put("track_id", 3);
        assertTrue(operations.insertROW(this.tableName, row3));
        operations.dropTable(this.tableName);
    }

    @Test
    public void updateValueTest() throws SQLException {
        this.createTableWithValues();
        String lookUp = "track_name = 'track_1'";
        String newValue = "track_id = 21";
        assertEquals("[[track_name, track_id], [track_1, 21]]", operations.updateValue(this.tableName, lookUp, newValue).toString());
        operations.dropTable(this.tableName);
    }

    @Test
    public void deleteValueTest() throws SQLException {
        this.createTableWithValues();
        String lookUp = "track_name = 'track_1'";
        assertEquals("[[track_name, track_id], [track_1, 1]]", operations.deleteValue(this.tableName, lookUp).toString());
        operations.dropTable(this.tableName);
    }

    @Test
    public void closeConnectionTest() throws SQLException {
        assertTrue(operations.closeConnection());
        operations.connectToDataBase("newtracklist", "maksym", "password");
    }
}