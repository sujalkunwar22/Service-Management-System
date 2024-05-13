package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "MyDB";
    private static final String DBUSER = "root";
    private static final String DBPASS = "Ss234@##*";

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName(DRIVER);
            // Create the connection URL
            String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
            // Establish the connection
            conn = DriverManager.getConnection(url, DBUSER, DBPASS);
        } catch (ClassNotFoundException ex) {
            // Handle driver class not found exception
            ex.printStackTrace();
            throw new SQLException("Error loading JDBC driver.");
        } catch (SQLException ex) {
            // Handle SQL exception
            ex.printStackTrace();
            throw new SQLException("Error connecting to the database.");
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        // Close the connection if it's not null
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // Handle closing connection exception
                e.printStackTrace();
            }
        }
    }

    public static void connect() {
        try {
            // Establish a connection to the database
            Connection conn = getConnection();
            System.out.println("Connected to database: " + DATABASE);
            // Close the connection
            closeConnection(conn);
        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        System.out.println("Disconnected from database: " + DATABASE);
    }
}
