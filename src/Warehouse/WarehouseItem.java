package Warehouse;

public class WarehouseItem implements Comparable<WarehouseItem> {
    private String id;
    private String name;
    private int priority;
    private int quantity;

    public WarehouseItem(String id, String name, int priority, int quantity) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.quantity = quantity;
    }

    // Basic getters for item properties
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public int getQuantity() {
        return quantity;
    }

    // Used when updating stock levels
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Needed for sorting items by priority
    @Override
    public int compareTo(WarehouseItem other) {
        return Integer.compare(other.priority, this.priority);
    }

    // Makes it easier to print item details
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Priority: %d, Quantity: %d",
                id, name, priority, quantity);
    }
} 