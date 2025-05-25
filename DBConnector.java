import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    // Replace with your actual MySQL port (default 3306 or your custom)
    private static final String URL = "jdbc:mysql://localhost:3306/MyBookstoreDB?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Successfully connected to MySQL database!");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Connection to database failed.");
            e.printStackTrace();
        }

        return connection;
    }

    public static void main(String[] args) {
        // Test connection
        Connection conn = getConnection();

        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
