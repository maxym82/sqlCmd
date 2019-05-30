package dataBase;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.net.SocketOption;
import java.net.SocketPermission;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataBaseOperations implements DataBaseInterface {
    private String dataBaseName;
    private String dbURL = "jdbc:postgresql://localhost:5432/";
    private Connection connection = null;
    private String username;
    private String password;

    public DataBaseOperations() {
//        this.dataBaseName = dataBaseName;
//        this.username = username;
//        this.password = password;
//        this.dbURL = this.dbURL + this.dataBaseName;
//        this.connectToDataBase(this.username, this.password);
    }

    @Override
    public boolean isDBexist(String dbName) throws SQLException {
        try {
            ResultSet listOfDb = null;
            if (this.connection != null) {
                listOfDb = connection.getMetaData().getCatalogs();
                while (listOfDb.next()) {
                    String nextDB = listOfDb.getString(1);
                    if (dbName.equals(nextDB)) {
                        return true;
                    }
                }
            } else {
                throw new SQLException("unable to create database connection");
            }
            if (listOfDb != null) {
                try {
                    listOfDb.close();
                } catch (SQLException ex) {
                    throw new SQLException("something went wrong" + ex.getMessage());
//                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return false;
    }

    @Override
    public String getDataBaseName() {
        return dataBaseName;
    }

    @Override
    public boolean createDB() throws SQLException {
//        todo: implement method to create DB
        return true;
    }

    @Override
    public boolean connectToDataBase(String dataBaseName, String username, String password) throws SQLException {
        this.dataBaseName = dataBaseName;
        this.username = username;
        this.password = password;
        String dbURL = this.dbURL + this.dataBaseName;
        try {
            ResultSet listOfDb = null;
            this.connection = DriverManager.getConnection(dbURL, this.username, this.password);
            if (connection != null) {
                return true;
            } else {
                throw new RuntimeException("unable to create database connection");
            }
        } catch (SQLException e) {
            throw new SQLException("something went wrong, please check your database name, user name and password\n" + e.getMessage());
        }
    }

    @Override
    public List<String> listTables() throws SQLException {
        List<String> listOfTables = new ArrayList<>();
        if (connection != null) {
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
        if (connection != null) {
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
        if (connection != null) {
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
    public boolean isConnected() {
        if (connection != null) {
            return true;
        }
        return false;
    }


    @Override
    public boolean createTable(String tableName, DataSetInterface newTable) throws SQLException {
        String[] dataTypes = new String[]{"BOOL", "CHAR", "VARCHAR", "TEXT", "SMALLINT", "INT", "SERIAL", "float", "DATE",
                "TIME", "TIMESTAMP", "INTERVAL", "UUID", "Array", "JSON", "hstore"};
        if (this.connection != null) {
            String statementString = "CREATE TABLE public." + tableName + " (";
            for (Map.Entry<String, Object> row : newTable.entrySet()) {
                if (Arrays.stream(dataTypes).anyMatch(row.getValue().toString().toUpperCase()::equals)) {
                    statementString = statementString + row.getKey() + " " + row.getValue().toString().toUpperCase() + " NOT NULL, ";
                } else {
                    throw new RuntimeException("This data type \"" + row.getValue().toString() + "\" is not supported. Type \"help\" for help");
                }
            }
            statementString = statementString.substring(0, statementString.length() - 2) + ")";
            //System.out.println(statementString);
            try {
                Statement statement = this.connection.createStatement();
//                System.out.println(statementString);
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

    private String getColumnNames(Map data) {
        String columnName = "";
        return columnName;
    }

    public ArrayList<ArrayList<String>> findTable(String tableName) throws SQLException {
        return findTable(tableName, null);
    }


    @Override
    public ArrayList<ArrayList<String>> findTable(String tableName, String condition) throws SQLException {
        ResultSet result = null;
        String newCondition;
        ArrayList<ArrayList<String>> resultTable = new ArrayList<ArrayList<String>>();
        ResultSetMetaData rsmd;
        if (connection != null) {
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
    public boolean insertROW(String tableName, DataSetInterface newRow) throws SQLException {
        String statementString = "INSERT INTO " + tableName + " (";
        if (connection != null) {
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet tables = dbmd.getTables(null, null, tableName, null);
            if (!tables.next()) {
                throw new RuntimeException("Table \"" + tableName + "\" does not exist");
            } else {
                for (String rowName : newRow.getKeys()) {
                    statementString = statementString + rowName + ", ";

                }
                statementString = statementString.substring(0, statementString.length() - 2) + ") VALUES (";

                for (Object rowValue : newRow.getAllData()) {
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
                throw new SQLException(e);
            }
        } else {
            return false;
        }

    }

    @Override
    public ArrayList<ArrayList<String>> updateValue(String tableName, String lookupValues, String newValues) throws SQLException {
        ArrayList<ArrayList<String>> updatedRow = new ArrayList<ArrayList<String>>();
        String statementString = "UPDATE " + tableName + " SET " + newValues + " WHERE " + lookupValues;
        if (connection != null) {
            try {
                Statement statement = this.connection.createStatement();
                statement.executeUpdate(statementString);
                updatedRow = this.findTable(tableName, newValues);
            } catch (SQLException e) {
                throw new SQLException(e.getMessage());
            }
        }
        return updatedRow;
    }

    @Override
    public ArrayList<ArrayList<String>> deleteValue(String tableName, String lookupValues) throws SQLException {
        ArrayList<ArrayList<String>> rowToPrint = new ArrayList<ArrayList<String>>();
        if (connection != null) {
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
            connection.close();
            connection = null;
            return true;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

}
