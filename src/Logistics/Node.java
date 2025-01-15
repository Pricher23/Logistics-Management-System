package Logistics;

import Utils.BinaryHeap;
import java.util.Map;

public class Node implements Comparable<Node> {
    private String location;
    private int distance;

    public Node(String location, int distance) {
        this.location = location;
        this.distance = distance;
    }

    public String getLocation() {
        return location;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.distance, other.distance);
    }

    @Override
    public String toString() {
        return String.format("(%s: %d)", location, distance);
    }

    public static void demonstrateBinaryHeap(Map<String, Map<String, Integer>> network) {
        System.out.println("\n=== Binary Heap Demonstration with Network Data ===");
        BinaryHeap<Node> heap = new BinaryHeap<>();
        
        // Add each location to our heap
        System.out.println("Inserting nodes from network:");
        for (Map.Entry<String, Map<String, Integer>> entry : network.entrySet()) {
            String location = entry.getKey();
            // Find the shortest distance for this location
            int minDistance = entry.getValue().values().stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
            
            Node node = new Node(location, minDistance);
            heap.insert(node);
            System.out.println("Added: " + node);
            heap.printHeap();
        }
        
        // Now remove them in order
        System.out.println("\nRemoving nodes in order:");
        while (!heap.isEmpty()) {
            Node min = heap.extractMin();
            System.out.println("Removed: " + min);
            heap.printHeap();
        }
    }
} 