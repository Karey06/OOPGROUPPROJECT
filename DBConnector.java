import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    private Connection connection;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Successfully connected to the database.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Add the jar to your project.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed! Check your credentials and DB status.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close connection.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        DBConnector dbConn = new DBConnector();
        dbConn.connect();

        // You can test queries here with dbConn.getConnection()

        dbConn.disconnect();
    }
}
