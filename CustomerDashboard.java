import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDashboard extends JFrame {

    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    private String customerName;

    public CustomerDashboard(String customerName) {
        this.customerName = customerName;

        setTitle("Browse Books - Welcome " + customerName);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + customerName + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.NORTH);

        // Table Columns: ID(hidden), Title, Author, Price, Stock
        String[] columns = {"ID", "Title", "Author", "Price", "Stock"};
        booksTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new JTable(booksTableModel);
        booksTable.removeColumn(booksTable.getColumnModel().getColumn(0)); // hide book ID

        loadBooks();

        add(new JScrollPane(booksTable), BorderLayout.CENTER);

        JButton buyButton = new JButton("Buy Selected Book");
        buyButton.addActionListener(e -> buySelectedBook());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose(); // Close the dashboard
            JOptionPane.showMessageDialog(null, "You have been logged out.");
            // Optionally, you can redirect to login screen here
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(buyButton);
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadBooks() {
        booksTableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT book_id, title, author, price, stock FROM books WHERE stock > 0";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                booksTableModel.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load books: " + e.getMessage());
        }
    }

    private void buySelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to buy.");
            return;
        }

        int modelRow = booksTable.convertRowIndexToModel(selectedRow);
        int bookId = (int) booksTableModel.getValueAt(modelRow, 0);
        String title = (String) booksTableModel.getValueAt(modelRow, 1);
        String author = (String) booksTableModel.getValueAt(modelRow, 2);
        BigDecimal price = (BigDecimal) booksTableModel.getValueAt(modelRow, 3);
        int stock = (int) booksTableModel.getValueAt(modelRow, 4);

        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 5));
        panel.add(new JLabel("Book: " + title));
        panel.add(new JLabel("Author: " + author));
        panel.add(new JLabel("Price: $" + price));
        panel.add(new JLabel("Available Stock: " + stock));
        panel.add(new JLabel("Enter Quantity to Buy:"));
        JTextField quantityField = new JTextField("1");
        panel.add(quantityField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Buy Book", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0 || quantity > stock) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm purchase of " + quantity + " x \"" + title + "\" for $" + price.multiply(BigDecimal.valueOf(quantity)) + "?",
                "Confirm Purchase",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Recheck stock to prevent race conditions
            PreparedStatement checkStmt = conn.prepareStatement("SELECT stock FROM books WHERE book_id = ?");
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || rs.getInt("stock") < quantity) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Insufficient stock. Purchase cancelled.");
                return;
            }

            // Update stock
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE books SET stock = stock - ? WHERE book_id = ?");
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, bookId);
            updateStmt.executeUpdate();

            // Optional: You could add an 'orders' table entry here for the purchase record

            conn.commit();

            JOptionPane.showMessageDialog(this, "Purchase successful! Thank you, " + customerName + ".");

            loadBooks(); // refresh book list to update stock
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Purchase failed: " + ex.getMessage());
        }
    }
}
