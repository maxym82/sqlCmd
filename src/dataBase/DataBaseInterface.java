package dataBase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DataBaseInterface {

    boolean isDBexist(String dbName) throws SQLException;

    String getDataBaseName();

    boolean createDB() throws SQLException;

    boolean connectToDataBase(String dataBaseName, String username, String password) throws SQLException;

    List<String> listTables() throws SQLException;

    boolean clearTable(String tableName) throws SQLException;

    boolean dropTable(String tableName) throws SQLException;

    boolean isConnected ();

    boolean createTable(String tableName, ArrayList<String> columns) throws SQLException;

    ArrayList<ArrayList<String>> findTable(String tableName, String condition) throws SQLException;

    boolean insertROW(String tableName, ArrayList<String> columns) throws SQLException;

    ArrayList<ArrayList<String>> updateValue(String tableName, String condition, String newValue) throws SQLException;

    ArrayList<ArrayList<String>> deleteValue(String tableName, String lookupValues) throws SQLException;

    boolean closeConnection() throws SQLException;

}
