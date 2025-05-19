import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bookstore"; // use your DB name
        String user = "root";
        String password = ""; // default XAMPP password is empty

        return DriverManager.getConnection(url, user, password);
    }
}
