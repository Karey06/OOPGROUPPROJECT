import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboard extends JFrame {
    public AdminDashboard(String username) {
        setTitle("Admin Panel - Welcome " + username);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(63, 81, 181));  // Indigo-ish background

        // Welcome label
        JLabel welcomeLabel = new JLabel("Hello Admin " + username + ", Welcome to BookNest Admin Panel", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        // Optional shadow effect (simple, by layering)
        welcomeLabel.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 120)); // shadow color
                g2.drawString(welcomeLabel.getText(), 11, 31);
                g2.dispose();
                super.paint(g, c);
            }
        });

        add(welcomeLabel, BorderLayout.NORTH);

        // Buttons Panel with padding
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 40, 100));
        buttonPanel.setBackground(new Color(48, 63, 159));  // Dark Indigo background

        // Create styled buttons
        JButton manageBooksBtn = createStyledButton("Manage Books (Add/Update/Delete)");
        JButton manageUsersBtn = createStyledButton("Manage Users (Ban/Update)");
        JButton processOrdersBtn = createStyledButton("Process Orders");
        JButton logoutBtn = createStyledButton("Logout");

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

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(103, 58, 183)); // Deep purple
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Rounded corners
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(button.getBackground());
                g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 20, 20);
                super.paint(g, c);
                g2.dispose();
            }
        });

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(81, 45, 168)); // Slightly darker purple on hover
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(103, 58, 183)); // Original color
            }
        });

        return button;
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