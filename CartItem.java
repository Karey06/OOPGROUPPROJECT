public class CartItem{
    private String bookId;
    private String title;
    private double price;
    private int quantity;


    public CartItem(String bookId, String title, double price, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }


    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public double getTotalPrice() {
        return price * quantity;
    }


    @Override
    public String toString() {
        return title + " (x" + quantity + ") - Ksh " + getTotalPrice();
    }
}
