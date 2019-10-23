package ua.com.maksym82.dataBase;

import java.sql.*;
import java.util.*;

public class DataBaseOperations implements DataBaseInterface {
    private String dataBaseName;
    private String dbURL = "jdbc:postgresql://localhost:5432/";
    private Connection connection = null;
    private Properties prop = new Properties();
    private final String[] dataTypes = new String[]{"BOOL", "CHAR", "VARCHAR", "TEXT", "SMALLINT", "INT", "SERIAL", "float", "DATE",
            "TIME", "TIMESTAMP", "INTERVAL", "UUID", "Array", "JSON", "hstore"};


    public DataBaseOperations() {
    }

    @Override
    public boolean isDBexist(String dbName) throws SQLException {
        try {
            ResultSet listOfDb = null;
            if (isConnected()) {
                listOfDb = connection.getMetaData().getCatalogs();
                while (listOfDb.next()) {
                    String nextDB = listOfDb.getString(1);
                    if (dbName.equals(nextDB)) {
                        return true;
                    }
                }
            }
            if (listOfDb != null) {
                    listOfDb.close();
                }
            } catch (SQLException e) {
            throw new SQLException("Something went wrong\n" + e.getMessage());
        }
        return false;
    }

    @Override
    public String getDataBaseName() {
        return dataBaseName;
    }


    @Override
    public boolean connectToDataBase(String dataBaseName, String username, String password) throws SQLException {
        this.dataBaseName = dataBaseName;
        this.prop.setProperty("user", username);
        this.prop.setProperty("password", password);
        String dbURL = this.dbURL + this.dataBaseName;
        try {
            ResultSet listOfDb = null;
            this.connection = DriverManager.getConnection(dbURL, prop);
            return true;
        } catch (SQLException e) {
            throw new SQLException("something went wrong, please check your database name, user name and password\n" + e.getMessage());
        }
    }

    @Override
    public List<String> listTables() throws SQLException {
        List<String> listOfTables = new ArrayList<>();
        if (isConnected()) {
            try {
                DatabaseMetaData md = connection.getMetaData();
                ResultSet rs = md.getTables(this.dataBaseName, null, "%", new String[]{"TABLE"});
                while (rs.next()) {
                    listOfTables.add(rs.getString(3));
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
        return listOfTables;
    }

    @Override
    public boolean clearTable(String tableName) throws SQLException {
        if (isConnected()) {
            try {
                Statement stmt = connection.createStatement();
                // Use TRUNCATE
                String sql = "TRUNCATE " + tableName;
                // Execute deletion
                stmt.executeUpdate(sql);
                stmt.close();
                return true;
            } catch (SQLException e) {
                throw new SQLException(e);
            }

        }
        return false;
    }

    @Override
    public boolean dropTable(String tableName) throws SQLException {
        if (isConnected()) {
            String stringStatement = "DROP TABLE " + tableName;
            try {
                Statement statement = connection.createStatement();
                statement.execute(stringStatement);
                statement.close();
                return true;
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isConnected(){
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            }
        }catch (SQLException e) {
            return false;
        }
        return false;
    }


    @Override
    public boolean createTable(String tableName, Map<String, Object> newTable) throws SQLException {
        if (isConnected()) {
            String statementString = "CREATE TABLE public." + tableName + " (";
            for (Map.Entry<String, Object> row : newTable.entrySet()) {
                if (Arrays.stream(this.dataTypes).anyMatch(row.getValue().toString().toUpperCase()::equals)) {
                    statementString = statementString + row.getKey() + " " + row.getValue().toString().toUpperCase() + " NOT NULL, ";
                } else {
                    throw new RuntimeException("This data type \"" + row.getValue().toString() + "\" is not supported. Type \"help\" for help");
                }
            }
            statementString = statementString.substring(0, statementString.length() - 2) + ")";
            try {
                Statement statement = this.connection.createStatement();
                statement.execute(statementString);
                statement.close();
                return true;
            } catch (SQLException e) {
                throw new SQLException(e);
            }

        } else {
            return false;
        }
    }

    public List<List<String>> findTable(String tableName) throws SQLException {
        return findTable(tableName, null);
    }


    @Override
    public List<List<String>> findTable(String tableName, String condition) throws SQLException {
        ResultSet result = null;
        String newCondition;
        List<List<String>> resultTable = new ArrayList<List<String>>();
        ResultSetMetaData rsmd;
        if (isConnected()) {
            if (!(condition == null || condition.equals(""))) {
                newCondition = " WHERE " + condition;
            } else {
                newCondition = "";
            }
            try {
                Statement statement = connection.createStatement();
                result = statement.executeQuery("SELECT  * from " + tableName + newCondition);
                rsmd = result.getMetaData();
                int columnNumber = rsmd.getColumnCount();
                if (columnNumber == 0) {
                    throw new SQLException("Table \" " + tableName + " \" does not exist");
                }
                ArrayList<String> row;
                ArrayList<String> header = new ArrayList<String>();
                for (int i = 1; i <= columnNumber; i++) {
                    header.add(rsmd.getColumnName(i));
                }
                resultTable.add(header);
                while (result.next()) {
                    row = new ArrayList<>();
                    for (int i = 1; i <= columnNumber; i++) {
                        if (result.getString(i) != null) {
                            row.add(result.getString(i));
                        }
                    }
                    resultTable.add(row);
                }
                statement.close();
                result.close();

            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
        return resultTable;
    }

    @Override
    public boolean insertROW(String tableName, Map<String, Object> newRow) throws SQLException {
        String statementString = "INSERT INTO " + tableName + " (";
        if (isConnected()) {
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet tables = dbmd.getTables(null, null, tableName, null);
            if (!tables.next()) {
                throw new RuntimeException("Table \"" + tableName + "\" does not exist");
            } else {
                for (String rowName : newRow.keySet()) {
                    statementString = statementString + rowName + ", ";

                }
                statementString = statementString.substring(0, statementString.length() - 2) + ") VALUES (";

                for (Object rowValue : newRow.values()) {
                    statementString = statementString + rowValue.toString() + ", ";
                }
                statementString = statementString.substring(0, statementString.length() - 2) + ")";
            }
            try {
                Statement statement = this.connection.createStatement();
                //System.out.println(statementString);
                statement.execute(statementString);
                statement.close();
                tables.close();
                return true;

            } catch (Exception e) {
                throw new SQLException("Check data you entered\n" + e);
            }
        } else {
            return false;
        }

    }

    @Override
    public List<List<String>> updateValue(String tableName, String lookupValues, String newValues) throws SQLException {
        List<List<String>> updatedRow = new ArrayList<List<String>>();
        String statementString = "UPDATE " + tableName + " SET " + newValues + " WHERE " + lookupValues;
        if (isConnected()) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resalt = statement.executeQuery("SELECT  * from " + tableName + " WHERE " + lookupValues);
                if (!resalt.next()) {throw new SQLException("There is no such an values in this table");}
                statement = this.connection.createStatement();
                statement.executeUpdate(statementString);
                updatedRow = this.findTable(tableName, newValues);
            } catch (SQLException e) {
                throw new SQLException("Please check data you entered\n" + e.getMessage());
            }
        }
        return updatedRow;
    }

    @Override
    public List<List<String>> deleteValue(String tableName, String lookupValues) throws SQLException {
        List<List<String>> rowToPrint = new ArrayList<List<String>>();
        if (isConnected()) {
            try {
                rowToPrint = this.findTable(tableName, lookupValues);

                Statement statement = this.connection.createStatement();
                statement.executeUpdate("DELETE from " + tableName + " WHERE " + lookupValues);
                statement.close();
            } catch (Exception e) {
                throw new SQLException(e.getMessage());
            }
        }
        return rowToPrint;
    }

    @Override
    public boolean closeConnection() throws SQLException {
        try {
            if (isConnected()) {connection.close();}
            connection = null;
            return true;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean createDB(String dbName) throws SQLException {
        if (isConnected()) {
            try {
                Statement statement = this.connection.createStatement();
                statement.executeUpdate("CREATE DATABASE " + dbName);
                statement.close();
            } catch (Exception e) {
                throw new SQLException(e.getMessage());
            }
        }
        return true;
    }

    @Override
    public boolean dropDB(String dbName) throws SQLException {
        if (isConnected()) {
            try {
                Statement statement = this.connection.createStatement();
                statement.executeUpdate("DROP DATABASE " + dbName);
                statement.close();
            } catch (Exception e) {
                throw new SQLException(e.getMessage());
            }
        }
        return true;
    }

}
