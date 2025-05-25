public class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private String category;
    private double price;
    private int stock;

    public Book(int bookId, String title, String author, String genre, String category, double price, int stock) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isInStock() {
        return stock > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity <= stock) {
            stock -= quantity;
        } else {
            System.out.println("Not enough stock to fulfill the order.");
        }
    }

    @Override
    public String toString() {
        return String.format("%s by %s | Genre: %s | Category: %s | $%.2f | Stock: %d",
                title, author, genre, category, price, stock);
    }
}
