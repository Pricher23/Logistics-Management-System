package Main;

import Logistics.LogisticsManager;
import Warehouse.WarehouseItem;
import Warehouse.WarehouseManager;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final WarehouseManager warehouseManager = new WarehouseManager();
    private static final LogisticsManager logisticsManager = new LogisticsManager();
    private static String startingPoint;

    public static void main(String[] args) {
        loadInitialData();
        mainMenu();
    }

    // Gets input from user and makes sure it's valid
    private static int getValidatedInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    // Loads our saved data when the program starts
    private static void loadInitialData() {
        warehouseManager.loadInventoryFromFile("src/Data/warehouse_inventory.csv");
        logisticsManager.loadLogisticsFromFile("src/Data/logistics_network.txt");
        
        // Set starting point to first city in network
        Set<String> locations = logisticsManager.getAllLocationNames();
        if (!locations.isEmpty()) {
            startingPoint = locations.iterator().next();  // Gets first location
            System.out.println("Starting point set to: " + startingPoint);
        } else {
            System.out.println("Warning: No locations loaded. Please add locations first.");
        }
    }

    // Main menu of our program
    private static void mainMenu() {
        while (true) {
            System.out.println("\n=== Logistics Management System ===");
            System.out.println("1. Warehouse");
            System.out.println("2. Locations");
            System.out.println("3. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = getValidatedInput();
            switch (choice) {
                case 1:
                    warehouseMenu();
                    break;
                case 2:
                    locationsMenu();
                    break;
                case 3:
                    saveAndExit();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Menu for warehouse operations
    private static void warehouseMenu() {
        while (true) {
            System.out.println("\n--- Warehouse Menu ---");
            System.out.println("1. Add Item to Warehouse");
            System.out.println("2. View Warehouse Inventory");
            System.out.println("3. Dispatch Item");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getValidatedInput();
            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    warehouseManager.viewInventory();
                    break;
                case 3:
                    dispatchItem();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Menu for managing locations
    private static void locationsMenu() {
        while (true) {
            System.out.println("\n--- Locations Menu ---");
            System.out.println("1. View Network");
            System.out.println("2. Add Location");
            System.out.println("3. Add Road");
            System.out.println("4. Remove Location");
            System.out.println("5. Remove Road");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getValidatedInput();
            switch (choice) {
                case 1:
                    logisticsManager.printNetwork();
                    break;
                case 2:
                    addLocation();
                    break;
                case 3:
                    addRoad();
                    break;
                case 4:
                    removeLocation();
                    break;
                case 5:
                    removeRoad();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Adds a new item or updates existing quantity
    private static void addItem() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine().trim();
        
        int priority;
        if (!warehouseManager.itemExists(name)) {
            System.out.print("Enter priority (1-10): ");
            priority = getValidatedInput();
        } else {
            priority = 0;  // Not used for existing items
        }
        
        System.out.print("Enter quantity: ");
        int quantity = getValidatedInput();
        
        try {
            warehouseManager.addItem(name, priority, quantity);
            System.out.println("Item added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Handles item dispatch and delivery route
    private static void dispatchItem() {
        System.out.print("Enter item name to dispatch (or leave empty for highest-priority item): ");
        String itemName = scanner.nextLine().trim();
        
        try {
            WarehouseItem item = warehouseManager.dispatchItem(itemName);
            
            // Get delivery location
            System.out.println("Current starting point: " + startingPoint);
            String destination = getDeliveryLocation();
            if (destination == null) return;

            // Find and show the route
            List<String> route = logisticsManager.findShortestPath(startingPoint, destination);
            if (route == null || route.isEmpty()) {
                System.out.println("No route available to " + destination);
                return;
            }

            System.out.println("Route: " + String.join(" -> ", route));
            System.out.print("Is this route acceptable? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                System.out.println("Dispatch cancelled.");
                return;
            }

            // Get quantity to dispatch
            System.out.print("Enter quantity to dispatch: ");
            int quantity = getValidatedInput();
            if (quantity <= 0 || quantity > item.getQuantity()) {
                System.out.println("Invalid quantity.");
                return;
            }

            // Update item quantity and confirm
            item.setQuantity(item.getQuantity() - quantity);
            System.out.println("\nDispatched " + quantity + " of " + 
                             item.getName() + " to " + destination + ".");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    // Gets a valid delivery location from user
    private static String getDeliveryLocation() {
        while (true) {
            System.out.print("Enter destination (or 0 to change starting point): ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("0")) {
                System.out.print("Enter new starting point: ");
                startingPoint = scanner.nextLine().trim();
                System.out.println("Starting point updated to: " + startingPoint);
                continue;
            }
            
            if (!logisticsManager.getAllLocationNames().contains(input)) {
                System.out.println("Invalid destination. Please try again.");
                continue;
            }
            
            return input;
        }
    }

    // Adds a new location to the network
    private static void addLocation() {
        System.out.print("Enter location name: ");
        String name = scanner.nextLine().trim();
        try {
            logisticsManager.addLocation(name);
            System.out.println("Location added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Adds a new road between locations
    private static void addRoad() {
        System.out.print("Enter first location: ");
        String from = scanner.nextLine().trim();
        System.out.print("Enter second location: ");
        String to = scanner.nextLine().trim();
        System.out.print("Enter distance: ");
        int distance = getValidatedInput();

        try {
            logisticsManager.addRoad(from, to, distance);
            System.out.println("Road added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Removes a location and all its roads
    private static void removeLocation() {
        System.out.print("Enter location to remove: ");
        String name = scanner.nextLine().trim();
        try {
            logisticsManager.deleteLocation(name);
            System.out.println("Location removed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Removes a road between locations
    private static void removeRoad() {
        System.out.print("Enter first location: ");
        String from = scanner.nextLine().trim();
        System.out.print("Enter second location: ");
        String to = scanner.nextLine().trim();

        try {
            logisticsManager.deleteRoad(from, to);
            System.out.println("Road removed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Saves everything and exits
    private static void saveAndExit() {
        warehouseManager.saveInventoryToFile("src/Data/warehouse_inventory.csv");
        logisticsManager.saveLogisticsToFile("src/Data/logistics_network.txt");
        System.out.println("Data saved. Goodbye!");
    }
}
