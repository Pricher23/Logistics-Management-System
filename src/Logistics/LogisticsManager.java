package Logistics;

import Utils.BinaryHeap;
import java.io.*;
import java.util.*;

public class LogisticsManager {
    // Keeps track of all locations in network
    private Map<String, Location> locations;

    public LogisticsManager() {
        this.locations = new HashMap<>();
    }

    // Adds a new location to network
    public void addLocation(String name) {
        if (locations.containsKey(name)) {
            throw new IllegalArgumentException("Location already exists: " + name);
        }
        locations.put(name, new Location(name));
    }

    // Connects two locations with a road
    public void addRoad(String from, String to, int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be positive");
        }

        Location fromLoc = locations.get(from);
        Location toLoc = locations.get(to);

        if (fromLoc == null || toLoc == null) {
            throw new IllegalArgumentException("Both locations must exist");
        }

        // Add road in both directions
        fromLoc.addConnection(toLoc, distance);
        toLoc.addConnection(fromLoc, distance);
    }

    // Uses binary heap to find the shortest path between locations
    public List<String> findShortestPath(String start, String end) {
        if (!locations.containsKey(start) || !locations.containsKey(end)) {
            return null;
        }

        // Setup for Dijkstra's algorithm
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        BinaryHeap<Node> minHeap = new BinaryHeap<>();

        // Set all initial distances to infinity except start
        for (String location : locations.keySet()) {
            distances.put(location, Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        // Start with starting location
        minHeap.insert(new Node(start, 0));

        // Main loop of Dijkstra's algorithm
        System.out.println("\nCalculating shortest path using Binary Heap:");
        while (!minHeap.isEmpty()) {
            Node current = minHeap.extractMin();
            String currentLocation = current.getLocation();
            
            System.out.println("Looking at: " + currentLocation + 
                             " (distance so far: " + current.getDistance() + ")");

            if (currentLocation.equals(end)) {
                break;
            }

            if (current.getDistance() > distances.get(currentLocation)) {
                continue;
            }

            // Check all connected locations
            Location currentLoc = locations.get(currentLocation);
            Map<Location, Integer> connections = currentLoc.getConnections();

            for (Map.Entry<Location, Integer> connection : connections.entrySet()) {
                String nextLocation = connection.getKey().getName();
                int newDistance = distances.get(currentLocation) + connection.getValue();

                // If found a shorter path, update it
                if (newDistance < distances.get(nextLocation)) {
                    System.out.println("Found better path to " + nextLocation + 
                                     " (new distance: " + newDistance + ")");
                    distances.put(nextLocation, newDistance);
                    previous.put(nextLocation, currentLocation);
                    minHeap.insert(new Node(nextLocation, newDistance));
                }
            }
        }

        // If couldn't reach the end, return null
        if (!previous.containsKey(end)) {
            return null;
        }

        // Build the path from end to start
        List<String> path = new ArrayList<>();
        String current = end;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        return path;
    }

    // Removes a location and all its roads
    public void deleteLocation(String name) {
        Location location = locations.get(name);
        if (location == null) {
            throw new IllegalArgumentException("Location doesn't exist: " + name);
        }

        // Remove all roads to this location
        for (Location other : locations.values()) {
            other.removeConnection(location);
        }

        locations.remove(name);
    }

    // Removes a road between two locations
    public void deleteRoad(String from, String to) {
        Location fromLoc = locations.get(from);
        Location toLoc = locations.get(to);

        if (fromLoc == null || toLoc == null) {
            throw new IllegalArgumentException("Both locations must exist");
        }

        fromLoc.removeConnection(toLoc);
        toLoc.removeConnection(fromLoc);
    }

    public Set<String> getAllLocationNames() {
        return new HashSet<>(locations.keySet());
    }

    // Shows all locations and their connections
    public void printNetwork() {
        for (Location location : locations.values()) {
            System.out.print(location.getName() + ": ");
            Map<Location, Integer> connections = location.getConnections();
            if (connections.isEmpty()) {
                System.out.println("No connections");
            } else {
                List<String> connectionStrings = new ArrayList<>();
                for (Map.Entry<Location, Integer> connection : connections.entrySet()) {
                    connectionStrings.add(connection.getKey().getName() + 
                                        "(" + connection.getValue() + ")");
                }
                System.out.println(String.join(", ", connectionStrings));
            }
        }
    }

    // Loads the network from a file
    public void loadLogisticsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(":");
                if (parts.length != 2) continue;

                String locationName = parts[0].trim();
                if (!locations.containsKey(locationName)) {
                    addLocation(locationName);
                }

                if (parts[1].trim().isEmpty()) continue;

                // Process each connection for this location
                String[] connections = parts[1].split(",");
                for (String connection : connections) {
                    connection = connection.trim();
                    if (connection.matches("\\w+\\(\\d+\\)")) {
                        String destName = connection.substring(0, connection.indexOf('('));
                        int distance = Integer.parseInt(connection.substring(
                            connection.indexOf('(') + 1, 
                            connection.indexOf(')')
                        ));
                        
                        if (!locations.containsKey(destName)) {
                            addLocation(destName);
                        }
                        
                        // Only add road if it doesn't exist
                        Location fromLoc = locations.get(locationName);
                        Location toLoc = locations.get(destName);
                        if (!fromLoc.getConnections().containsKey(toLoc)) {
                            addRoad(locationName, destName, distance);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't load the network file.");
        }
    }

    // Saves the network to a file
    public void saveLogisticsToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Location location : locations.values()) {
                writer.print(location.getName() + ": ");
                Map<Location, Integer> connections = location.getConnections();
                if (connections.isEmpty()) {
                    writer.println();
                } else {
                    List<String> connectionStrings = new ArrayList<>();
                    for (Map.Entry<Location, Integer> connection : connections.entrySet()) {
                        connectionStrings.add(connection.getKey().getName() + 
                                           "(" + connection.getValue() + ")");
                    }
                    writer.println(String.join(", ", connectionStrings));
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't save the network file.");
        }
    }
}