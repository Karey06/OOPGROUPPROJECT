package BookNestApp;

import javax.swing.*;
import java.awt.*;

public class BrowseBooks extends JFrame {

    public BrowseBooks() {
        setTitle("Browse Books");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea bookDisplay = new JTextArea();
        bookDisplay.setEditable(false);
        bookDisplay.setText(getDummyBooks()); // For now, shows placeholder data

        JScrollPane scrollPane = new JScrollPane(bookDisplay);
        add(scrollPane, BorderLayout.CENTER);
    }

    private String getDummyBooks() {
        return """
                1. Introduction to Java - Author: John Doe
                2. Advanced Python - Author: Jane Smith
                3. Web Development with HTML & CSS - Author: Alice Brown
                4. Database Systems - Author: Bob Johnson
                """;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BrowseBooks().setVisible(true);
        });
    }
}