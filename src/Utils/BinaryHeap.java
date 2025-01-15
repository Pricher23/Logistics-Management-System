package Utils;

public class BinaryHeap<T extends Comparable<T>> {
    // Using a simple array to store heap elements
    private Object[] elements;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    public BinaryHeap() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    // Makes sure it has enough space in array
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
    }

    // Helper methods to find parent and children
    private int parent(int index) { return (index - 1) / 2; }
    private int leftChild(int index) { return 2 * index + 1; }
    private int rightChild(int index) { return 2 * index + 2; }

    // Swaps two elements in the heap
    private void swap(int i, int j) {
        Object temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    // Adds a new element to the heap
    @SuppressWarnings("unchecked")
    public void insert(T element) {
        ensureCapacity();
        elements[size] = element;
        heapifyUp(size);
        size++;
    }

    // Gets and removes the smallest element
    @SuppressWarnings("unchecked")
    public T extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        T min = (T) elements[0];
        elements[0] = elements[size - 1];
        size--;
        if (size > 0) {
            heapifyDown(0);
        }
        return min;
    }

    // Moves an element up to its correct position
    @SuppressWarnings("unchecked")
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIdx = parent(index);
            // If current element is smaller than its parent, swap them
            if (((T) elements[index]).compareTo((T) elements[parentIdx]) < 0) {
                swap(index, parentIdx);
                index = parentIdx;
            } else {
                break;
            }
        }
    }

    // Moves an element down to its correct position
    @SuppressWarnings("unchecked")
    private void heapifyDown(int index) {
        int minIndex = index;
        int leftChild = leftChild(index);
        int rightChild = rightChild(index);

        // Check if left child is smaller
        if (leftChild < size && 
            ((T) elements[leftChild]).compareTo((T) elements[minIndex]) < 0) {
            minIndex = leftChild;
        }

        // Check if right child is smaller
        if (rightChild < size && 
            ((T) elements[rightChild]).compareTo((T) elements[minIndex]) < 0) {
            minIndex = rightChild;
        }

        // If found a smaller child, swap and continue down
        if (minIndex != index) {
            swap(index, minIndex);
            heapifyDown(minIndex);
        }
    }

    // Basic heap operations
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    // Shows current state of heap
    @SuppressWarnings("unchecked")
    public void printHeap() {
        System.out.println("\nCurrent Binary Heap state:");
        for (int i = 0; i < size; i++) {
            System.out.println("Index " + i + ": " + elements[i]);
        }
    }
} 