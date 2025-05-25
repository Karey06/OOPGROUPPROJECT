import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Order {
    public static class Book {
        private final String title;
        private final String author;
        private final double price;

        public Book(String title, String author, double price) {
            this.title = title;
            this.author = author;
            this.price = price;
        }

        public double getPrice() { return price; }

        @Override
        public String toString() {
            return String.format("'%s' by %s ($%.2f)", title, author, price);
        }
    }
    public static class Customer {
        private final String name;
        private final String email;

        public Customer(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() { return name; }

        @Override
        public String toString() {
            return String.format("%s (%s)", name, email);
        }
    }

    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
    private static final AtomicLong idCounter = new AtomicLong(System.currentTimeMillis()); // Unique starting point
    private final long orderId;
    private final Customer customer;
    private final List<Book> orderedBooks;
    private final LocalDateTime orderDate;
    private final String shippingAddress;
    private final double totalPrice;
    private OrderStatus status;

    public Order(Customer customer, List<Book> orderedBooks, String shippingAddress) {
        if (customer == null || orderedBooks == null || orderedBooks.isEmpty()) {
            throw new IllegalArgumentException("Customer and book list cannot be null or empty.");
        }
        this.orderId = idCounter.incrementAndGet();
        this.customer = customer;
        this.orderedBooks = orderedBooks;
        this.shippingAddress = shippingAddress;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING; // Default status
        this.totalPrice = orderedBooks.stream().mapToDouble(Book::getPrice).sum();
    }

    public void shipOrder() {
        if (this.status == OrderStatus.PENDING || this.status == OrderStatus.PROCESSING) {
            this.status = OrderStatus.SHIPPED;
        }
    }

    public void cancelOrder() {
        if (this.status == OrderStatus.PENDING) {
            this.status = OrderStatus.CANCELLED;
        }
    }


    public long getOrderId() { return orderId; }
    public OrderStatus getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }
    public Customer getCustomer() { return customer; }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("================ ORDER SUMMARY ================\n");
        sb.append(String.format(" ID: %d       Status: %s\n", orderId, status));
        sb.append(String.format(" Date: %s\n", orderDate.format(formatter)));
        sb.append(String.format(" Customer: %s\n", customer.getName()));
        sb.append(String.format(" Ship To: %s\n", shippingAddress));
        sb.append("---------------------------------------------\n");
        sb.append(" Books:\n");
        for (Book book : orderedBooks) {
            sb.append(String.format("   - %s\n", book));
        }
        sb.append("---------------------------------------------\n");
        sb.append(String.format(" TOTAL: $%.2f\n", totalPrice));
        sb.append("=============================================\n");
        return sb.toString();
    }
}