package Logistics;

import java.util.HashMap;
import java.util.Map;

public class Location {
    private String name;
    // Stores connections to other locations and their distances
    private Map<Location, Integer> connections;

    public Location(String name) {
        this.name = name;
        this.connections = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<Location, Integer> getConnections() {
        return connections;
    }

    // Adds a new road to another location
    public void addConnection(Location destination, int distance) {
        connections.put(destination, distance);
    }

    // Removes a road to a location
    public void removeConnection(Location destination) {
        connections.remove(destination);
    }

    // Makes it easier to print location names
    @Override
    public String toString() {
        return name;
    }
}