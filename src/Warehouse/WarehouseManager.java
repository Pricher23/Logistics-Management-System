package Warehouse;

import java.io.*;
import java.util.*;

public class WarehouseManager {
    // List to store all our items
    private List<WarehouseItem> inventory;

    public WarehouseManager() {
        this.inventory = new ArrayList<>();
    }

    // Creates a new ID for items (001, 002, etc.)
    private String generateNextId() {
        int maxId = inventory.stream()
            .map(item -> Integer.parseInt(item.getId()))
            .max(Integer::compareTo)
            .orElse(0);
        return String.format("%03d", maxId + 1);
    }

    // Adds new item or updates quantity if it exists
    public void addItem(String name, int priority, int quantity) {
        if (itemExists(name)) {
            // If item exists, just update its quantity
            WarehouseItem existingItem = inventory.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .get();
            System.out.println("Item already exists with priority " + 
                             existingItem.getPriority() + 
                             ". Adding " + quantity + " to existing quantity.");
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Create new item with new ID
            String id = generateNextId();
            inventory.add(new WarehouseItem(id, name, priority, quantity));
        }
    }

    // Checks if we already have this item
    public boolean itemExists(String name) {
        return inventory.stream()
            .anyMatch(item -> item.getName().equalsIgnoreCase(name));
    }

    // Shows all items in stock
    public void viewInventory() {
        inventory.stream()
            .sorted(Comparator.comparing(WarehouseItem::getId))
            .forEach(System.out::println);
    }

    // Gets item to dispatch (highest priority or specific item)
    public WarehouseItem dispatchItem(String name) {
        if (inventory.isEmpty()) {
            throw new IllegalStateException("Warehouse is empty");
        }

        WarehouseItem itemToDispatch;
        if (name.isEmpty()) {
            // Get highest priority item
            itemToDispatch = inventory.stream()
                .max(Comparator.comparingInt(WarehouseItem::getPriority))
                .orElseThrow(() -> new IllegalStateException("No items available"));
        } else {
            // Get specific item by name
            itemToDispatch = inventory.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + name));
        }

        return itemToDispatch;
    }

    // Loads inventory from CSV file
    public void loadInventoryFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;  // Skip header row
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    int priority = Integer.parseInt(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);
                    inventory.add(new WarehouseItem(id, name, priority, quantity));
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load inventory file. Starting with empty inventory.");
        }
    }

    // Saves inventory to CSV file
    public void saveInventoryToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID,Name,Priority,Quantity");
            for (WarehouseItem item : inventory) {
                writer.printf("%s,%s,%d,%d%n",
                    item.getId(),
                    item.getName(),
                    item.getPriority(),
                    item.getQuantity());
            }
        } catch (IOException e) {
            System.out.println("Error: Could not save inventory to file.");
        }
    }
}