import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JFrame {

    public CustomerDashboard(String username) {
        setTitle("Browse Books - Welcome " + username);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.NORTH);

        JTextArea bookList = new JTextArea();
        bookList.setText("📚 Available Books:\n" +
                "1. Introduction to Java\n" +
                "2. Object-Oriented Programming\n" +
                "3. Data Structures and Algorithms\n" +
                "4. Web Development with HTML/CSS/JS\n" +
                "5. Database Systems");
        bookList.setEditable(false);
        bookList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(bookList), BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new CustomerDashboard("JohnDoe"));
    }
}
