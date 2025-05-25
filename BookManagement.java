import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BookManagement extends JFrame {

    private JTable booksTable;
    private DefaultTableModel booksTableModel;

    private JTextField titleInput, authorInput, genreInput, categoryInput, priceInput, stockInput;
    private JButton addBookButton, updateBookButton, deleteBookButton;

    public BookManagement() {
        setTitle("Book Management");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Setup Table
        String[] columnNames = {"ID", "Title", "Author", "Genre", "Category", "Price", "Stock"};
        booksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // prevent direct edit in table cells
            }
        };
        booksTable = new JTable(booksTableModel);
        loadBooksFromDatabase();

        add(new JScrollPane(booksTable), BorderLayout.CENTER);

        // Setup Form Panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));

        formPanel.add(new JLabel("Title:"));
        titleInput = new JTextField();
        formPanel.add(titleInput);

        formPanel.add(new JLabel("Author:"));
        authorInput = new JTextField();
        formPanel.add(authorInput);

        formPanel.add(new JLabel("Genre:"));
        genreInput = new JTextField();
        formPanel.add(genreInput);

        formPanel.add(new JLabel("Category:"));
        categoryInput = new JTextField();
        formPanel.add(categoryInput);

        formPanel.add(new JLabel("Price:"));
        priceInput = new JTextField();
        formPanel.add(priceInput);

        formPanel.add(new JLabel("Stock:"));
        stockInput = new JTextField();
        formPanel.add(stockInput);

        // Setup Buttons
        addBookButton = new JButton("Add Book");
        updateBookButton = new JButton("Update Selected");
        deleteBookButton = new JButton("Delete Selected");

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonsPanel.add(addBookButton);
        buttonsPanel.add(updateBookButton);
        buttonsPanel.add(deleteBookButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Button Listeners
        addBookButton.addActionListener(e -> addBook());
        updateBookButton.addActionListener(e -> updateSelectedBook());
        deleteBookButton.addActionListener(e -> deleteSelectedBook());

        // Table row selection fills form fields
        booksTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow >= 0) {
                titleInput.setText(booksTableModel.getValueAt(selectedRow, 1).toString());
                authorInput.setText(booksTableModel.getValueAt(selectedRow, 2).toString());
                genreInput.setText(booksTableModel.getValueAt(selectedRow, 3).toString());
                categoryInput.setText(booksTableModel.getValueAt(selectedRow, 4).toString());
                priceInput.setText(booksTableModel.getValueAt(selectedRow, 5).toString());
                stockInput.setText(booksTableModel.getValueAt(selectedRow, 6).toString());
            }
        });

        setVisible(true);
    }

    private void loadBooksFromDatabase() {
        booksTableModel.setRowCount(0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] bookRow = {
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getString("category"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("stock")
                };
                booksTableModel.addRow(bookRow);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load books: " + e.getMessage());
        }
    }

    private void addBook() {
        try {
            String title = titleInput.getText().trim();
            String author = authorInput.getText().trim();
            String genre = genreInput.getText().trim();
            String category = categoryInput.getText().trim();
            double price = Double.parseDouble(priceInput.getText().trim());
            int stock = Integer.parseInt(stockInput.getText().trim());

            if (title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Author are required.");
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String insertSQL = "INSERT INTO books (title, author, genre, category, price, stock) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(insertSQL);
                statement.setString(1, title);
                statement.setString(2, author);
                statement.setString(3, genre);
                statement.setString(4, category);
                statement.setDouble(5, price);
                statement.setInt(6, stock);

                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                loadBooksFromDatabase();
                clearFormFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and stock.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + e.getMessage());
        }
    }

    private void updateSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.");
            return;
        }

        try {
            int bookId = (int) booksTableModel.getValueAt(selectedRow, 0);
            String title = titleInput.getText().trim();
            String author = authorInput.getText().trim();
            String genre = genreInput.getText().trim();
            String category = categoryInput.getText().trim();
            double price = Double.parseDouble(priceInput.getText().trim());
            int stock = Integer.parseInt(stockInput.getText().trim());

            if (title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Author are required.");
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String updateSQL = "UPDATE books SET title=?, author=?, genre=?, category=?, price=?, stock=? WHERE book_id=?";
                PreparedStatement statement = connection.prepareStatement(updateSQL);
                statement.setString(1, title);
                statement.setString(2, author);
                statement.setString(3, genre);
                statement.setString(4, category);
                statement.setDouble(5, price);
                statement.setInt(6, stock);
                statement.setInt(7, bookId);

                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book updated successfully!");
                loadBooksFromDatabase();
                clearFormFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and stock.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + e.getMessage());
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        int confirmDelete = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirmDelete != JOptionPane.YES_OPTION) return;

        try (Connection connection = DatabaseConnection.getConnection()) {
            int bookId = (int) booksTableModel.getValueAt(selectedRow, 0);
            String deleteSQL = "DELETE FROM books WHERE book_id=?";
            PreparedStatement statement = connection.prepareStatement(deleteSQL);
            statement.setInt(1, bookId);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            loadBooksFromDatabase();
            clearFormFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage());
        }
    }

    private void clearFormFields() {
        titleInput.setText("");
        authorInput.setText("");
        genreInput.setText("");
        categoryInput.setText("");
        priceInput.setText("");
        stockInput.setText("");
        booksTable.clearSelection();
    }

    // Utility to get all books details text (for customer view)
    public static String getAllBooksInfoText() {
        StringBuilder sb = new StringBuilder();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                sb.append("Title: ").append(rs.getString("title")).append("\n");
                sb.append("Author: ").append(rs.getString("author")).append("\n");
                sb.append("Genre: ").append(rs.getString("genre")).append("\n");
                sb.append("Category: ").append(rs.getString("category")).append("\n");
                sb.append("Price: $").append(rs.getBigDecimal("price")).append("\n");
                sb.append("Stock: ").append(rs.getInt("stock")).append("\n");
                sb.append("-----------------------------\n");
            }
        } catch (SQLException e) {
            return "Failed to load books: " + e.getMessage();
        }

        return sb.toString();
    }

}
