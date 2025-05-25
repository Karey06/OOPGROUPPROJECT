
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerRegistration extends JFrame {

    private JTextField nameField, emailField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton registerButton;

    public CustomerRegistration() {
        setTitle("Register New User");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Register as:"));
        roleCombo = new JComboBox<>(new String[]{"Customer", "Admin"});
        add(roleCombo);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        add(new JLabel()); // empty label for layout
        add(registerButton);

        setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = ((String) roleCombo.getSelectedItem()).toLowerCase(); // "customer" or "admin"

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try (Connection conn = DBConnector.getConnection()) {
            String query = "INSERT INTO users (name, email, username, password, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, username);
            stmt.setString(4, password);
            stmt.setString(5, role); // "customer" or "admin"
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful as " + role + "!");
            dispose();
            new LoginScreen();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new CustomerRegistration();
    }
}