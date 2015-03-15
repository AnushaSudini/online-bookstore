package uom.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionInfo {

    public final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public final static String DB_URL = "jdbc:mysql://127.0.0.1:3306/";
    public final static String DB_NAME = "dbproj";
    public final static String DB_USER = "root";
    public final static String DB_PASSWD = "root";

    /**
     * Use this method to get the singleton instance of this class.
     */
    public static Connection getInstance() {
        Connection connection = null;
        if (connection == null) {
            try {
                // The newInstance() call is a work around for some broken Java implementations.
                Class.forName(JDBC_DRIVER).newInstance();
                connection = DriverManager.getConnection(DB_URL + DB_NAME,
                        DB_USER,
                        DB_PASSWD);
//            } catch (ClassNotFoundException e) {
//                // Couldn't find the driver class.
//                // TODO Handle the error.
//                e.printStackTrace();
            } catch (Exception e) {
                // Other problems with loading the driver class.
                // TODO Handle the error.
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Constructor intentionally private.
     */
    private DatabaseConnectionInfo() {
    }
}
