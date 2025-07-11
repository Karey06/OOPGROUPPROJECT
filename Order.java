

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {

    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED;

        // Convert DB string to enum (case-insensitive)
        public static OrderStatus fromString(String status) {
            return OrderStatus.valueOf(status.toUpperCase());
        }
    }

    private int orderId;  // from DB, auto-increment
    private int customerId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private String shippingAddress;

    private List<OrderItem> orderItems;

    public Order(int customerId, String shippingAddress) {
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.orderItems = new ArrayList<>();
    }

    // Getters and setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public List<OrderItem> getOrderItems() { return orderItems; }

    // Add a book to the order with quantity
    public void addOrderItem(int bookId, int quantity, double price) {
        orderItems.add(new OrderItem(bookId, quantity, price));
    }

    // Calculate total price of the order
    public double getTotalPrice() {
        return orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    // Save order and items to DB
    public boolean saveOrder() {
        String insertOrderSQL = "INSERT INTO orders (customer_id, order_date, status) VALUES (?, ?, ?)";
        String insertOrderItemSQL = "INSERT INTO order_items (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction

            // Insert into orders table
            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, customerId);
                psOrder.setTimestamp(2, Timestamp.valueOf(orderDate));
                psOrder.setString(3, status.name());

                int affectedRows = psOrder.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = psOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.orderId = generatedKeys.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Insert order items
            try (PreparedStatement psItem = conn.prepareStatement(insertOrderItemSQL)) {
                for (OrderItem item : orderItems) {
                    psItem.setInt(1, orderId);
                    psItem.setInt(2, item.getBookId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getPrice());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void shipOrder() {
        if (status == OrderStatus.PENDING || status == OrderStatus.PROCESSING) {
            status = OrderStatus.SHIPPED;
        }
    }

    public void cancelOrder() {
        if (status == OrderStatus.PENDING) {
            status = OrderStatus.CANCELLED;
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("================ ORDER SUMMARY ================\n");
        sb.append(String.format("ID: %d     Status: %s\n", orderId, status));
        sb.append(String.format("Date: %s\n", orderDate.format(formatter)));
        sb.append(String.format("Customer ID: %d\n", customerId));
        sb.append(String.format("Ship To: %s\n", shippingAddress));
        sb.append("---------------------------------------------\n");
        sb.append("Books:\n");
        for (OrderItem item : orderItems) {
            sb.append(String.format(" - Book ID: %d | Quantity: %d | Price each: $%.2f\n",
                    item.getBookId(), item.getQuantity(), item.getPrice()));
        }
        sb.append("---------------------------------------------\n");
        sb.append(String.format("TOTAL: $%.2f\n", getTotalPrice()));
        sb.append("=============================================\n");
        return sb.toString();
    }

    // Inner class for order items
    public static class OrderItem {
        private final int bookId;
        private final int quantity;
        private final double price;

        public OrderItem(int bookId, int quantity, double price) {
            this.bookId = bookId;
            this.quantity = quantity;
            this.price = price;
        }

        public int getBookId() { return bookId; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
    }
}
