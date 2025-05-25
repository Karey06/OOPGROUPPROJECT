import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard(String username) {
        setTitle("Admin Panel - Welcome " + username);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(147, 112, 219)); // medium purple (also known as "MediumSlateBlue")


        // Welcome label
        JLabel welcomeLabel = new JLabel("Hello Admin " + username + ", Welcome to BookNest Admin Panel", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setBackground(new Color(75, 0, 130));
        buttonPanel.setForeground(Color.WHITE);

        JButton manageBooksBtn = new JButton("Manage Books (Add/Update/Delete)");
        JButton manageUsersBtn = new JButton("Manage Users (Ban/Update)");
        JButton processOrdersBtn = new JButton("Process Orders");
        JButton logoutBtn = new JButton("Logout");

        buttonPanel.add(manageBooksBtn);
        buttonPanel.add(manageUsersBtn);
        buttonPanel.add(processOrdersBtn);
        buttonPanel.add(logoutBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // Button listeners
        manageBooksBtn.addActionListener(e -> new ManageBooksWindow());
        manageUsersBtn.addActionListener(e -> new ManageUsersWindow());
        processOrdersBtn.addActionListener(e -> new ProcessOrdersWindow());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen(); // Go back to login screen
        });

        setVisible(true);
    }

    // Placeholder windows
    public static class ManageBooksWindow extends JFrame {
        public ManageBooksWindow() {
            setTitle("Manage Books");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            add(new JLabel("<html><center>Add, Update, or Delete books.<br>(Hook to DB here)</center></html>", JLabel.CENTER), BorderLayout.CENTER);
            setVisible(true);
        }
    }

    public static class ManageUsersWindow extends JFrame {
        public ManageUsersWindow() {
            setTitle("Manage Users");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            add(new JLabel("<html><center>Ban or Update Users.<br>(Hook to DB here)</center></html>", JLabel.CENTER), BorderLayout.CENTER);
            setVisible(true);
        }
    }

    public static class ProcessOrdersWindow extends JFrame {
        public ProcessOrdersWindow() {
            setTitle("Process Orders");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            add(new JLabel("<html><center>Process user orders.<br>(Hook to DB here)</center></html>", JLabel.CENTER), BorderLayout.CENTER);
            setVisible(true);
        }
    }
}
