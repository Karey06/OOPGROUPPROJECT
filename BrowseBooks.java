import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BrowseBooks extends JFrame {

    private final DefaultTableModel bookTableModel;
    private final JTable bookTable;
    private final List<Book> books;

    public BrowseBooks() {
        setTitle("Browse Books");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Load books from DB (assumed DatabaseConnection.getAllBooks() exists)
        books = DatabaseConnection.getAllBooks();

        // Table columns
        String[] columns = {"ID", "Title", "Author", "Genre", "Category", "Price", "Stock"};
        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Populate table rows
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books available at the moment.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Book b : books) {
                Object[] row = {
                        b.getBookId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getGenre(),
                        b.getCategory(),
                        String.format("$%.2f", b.getPrice()),
                        b.getStock()
                };
                bookTableModel.addRow(row);
            }
        }

        bookTable = new JTable(bookTableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton refreshBtn = new JButton("Refresh List");
        JButton closeBtn = new JButton("Close");

        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        refreshBtn.addActionListener(e -> refreshBookList());
        closeBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void refreshBookList() {
        // Clear current rows
        bookTableModel.setRowCount(0);
        List<Book> refreshedBooks = DatabaseConnection.getAllBooks();

        if (refreshedBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books available at the moment.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Book b : refreshedBooks) {
                Object[] row = {
                        b.getBookId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getGenre(),
                        b.getCategory(),
                        String.format("$%.2f", b.getPrice()),
                        b.getStock()
                };
                bookTableModel.addRow(row);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BrowseBooks::new);
    }
}
