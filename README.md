# Logistics Management System
## Binary Heap Implementation in Warehouse Management

### Data Structure: Binary Heap
**Implementation Details:**
- Custom implementation using raw array
- No Java collections used
- Core operations: insert O(log n), extractMin O(log n)
- Used for priority queue in Dijkstra's algorithm

**Key Components:**
- `Node.java`: Represents a node in the network with location and distance.
- `LogisticsManager.java`: Manages the logistics network and provides methods for shortest path calculation.
- `WarehouseManager.java`: Manages the warehouse inventory with priority queue for efficient item retrieval.
- `Main.java`: The main application that allows user interaction and demonstrates the system.

### Application Overview
**Purpose:** Warehouse and Logistics Management System that:
1. Manages warehouse inventory with priorities
2. Finds optimal delivery routes between cities
3. Tracks item quantities and locations

**Key Features:**
1. **Warehouse Management**
   - Priority-based item storage (1-10 scale)
   - Automatic dispatch of highest priority items
   - Quantity tracking and updates

2. **Route Finding**
   - Uses Binary Heap for Dijkstra's algorithm
   - Finds shortest paths between cities
   - Visual output of path calculation:
   ```
   Processing location: London (distance: 0)
   Found shorter path to Paris (new distance: 7)
   Found shorter path to Berlin (new distance: 5)
   ```

3. **Data Persistence**
   - Saves/loads warehouse inventory (CSV)
   - Maintains logistics network data (TXT)

### Binary Heap Usage Example
```java
// Example of Binary Heap usage in path finding
BinaryHeap<Node> minHeap = new BinaryHeap<>();
minHeap.insert(new Node("London", 0));
Node min = minHeap.extractMin();  // O(log n) operation

// Example in LogisticsManager
public List<String> findShortestPath(String start, String end) {
    BinaryHeap<Node> heap = new BinaryHeap<>();
    heap.insert(new Node(start, 0));
    // ... Dijkstra's algorithm implementation
}
```

### Why Binary Heap?
1. **Efficiency**
   - O(log n) for insert and extractMin
   - Perfect for Dijkstra's priority queue
   - Better than O(n) array operations

2. **Practical Benefits**
   - Fast route calculations between cities
   - Efficient priority management for warehouse
   - Scalable for larger networks

### Implementation Benefits
1. **From Scratch**
   - Full control over operations
   - No dependency on Java collections
   - Clear demonstration of understanding

2. **Practical Application**
   - Real-world logistics problem
   - Visual feedback of operations
   - Integrated into larger system

### Project Structure
```
src/
  ├── Main.java
  ├── Data/
  │   ├── logistics_network.txt    # City connections
  │   └── warehouse_inventory.csv  # Item inventory
  ├── Logistics/
  │   ├── Location.java           # City representation
  │   ├── LogisticsManager.java   # Network management
  │   └── Node.java              # For path finding
  ├── Utils/
  │   ├── BinaryHeap.java        # Core data structure
  │   └── FileHandler.java       # File operations
  └── Warehouse/
      ├── WarehouseItem.java     # Item representation
      └── WarehouseManager.java  # Inventory management
```

### How to Run
1. Ensure Java is installed on your system
2. Clone/download the project
3. Navigate to the project directory
4. Run Main.java
5. The program will:
   - Load the logistics network and warehouse data
   - Automatically set the first city from logistics_network.txt as the starting point
   - Display "Starting point set to: [CityName]"
6. Follow the menu prompts to:
   - Manage warehouse inventory
   - Find optimal routes between cities
   - Dispatch items to different locations
   - Change starting point during dispatch operations

Note: The starting point can be changed at any time during dispatch by entering '0' when prompted for a destination.
