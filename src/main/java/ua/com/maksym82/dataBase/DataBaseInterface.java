package ua.com.maksym82.dataBase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataBaseInterface {

    boolean isDBexist(String dbName) throws SQLException;

    String getDataBaseName();

    boolean connectToDataBase(String dataBaseName, String username, String password) throws SQLException;

    List<String> listTables() throws SQLException;

    boolean clearTable(String tableName) throws SQLException;

    boolean dropTable(String tableName) throws SQLException;

    boolean isConnected ();

    boolean createTable(String tableName, Map<String, Object> newTable) throws SQLException;

    List<List<String>> findTable(String tableName, String condition) throws SQLException;

    boolean insertROW(String tableName, Map<String, Object> newRow) throws SQLException;

    List<List<String>> updateValue(String tableName, String condition, String newValue) throws SQLException;

    List<List<String>> deleteValue(String tableName, String lookupValues) throws SQLException;

    boolean closeConnection() throws SQLException;

    boolean createDB(String dbName) throws SQLException;

    boolean dropDB(String dbName) throws SQLException;
}
