//
// Graph.java
// Joseph Nguyen
//
package com.example.mobileapp.wordly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.ArrayList;

class GraphNode<T> {
    private T value;

    // hashset for quick checking of neighbors
    private HashSet<T> uniqueNeighborValues = new HashSet<>();
    private ArrayList<GraphNode<T>> neighbors = new ArrayList<>();

    GraphNode(T value) {
        this.value = value;
    }

    T getValue() {
        return value;
    }

    ArrayList<GraphNode<T>> getNeighbors() {
        return neighbors;
    }

    void addNeighbor(GraphNode<T> toNode) {
        neighbors.add(toNode);
        uniqueNeighborValues.add(toNode.getValue());
    }

    GraphNode<T> getRandomNeighbor() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(neighbors.size());
        return neighbors.get(randomIndex);
    }

    boolean isNeighbor(GraphNode<T> node) {
        return uniqueNeighborValues.contains(node.getValue());
    }

    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof GraphNode)) {
            return false;
        }

        return this.getValue() == ((GraphNode) object).getValue();
    }
}

public class Graph<T> {
    protected HashMap<T, GraphNode<T>> valueMap = new HashMap<>();
    protected ArrayList<GraphNode<T>> vertices = new ArrayList<>();

    public ArrayList<GraphNode<T>>  getVertices() {
        return vertices;
    }

    public void addEdge(T fromValue, T toValue) {
        GraphNode<T> fromNode = getVertexWithValue(fromValue);
        GraphNode<T> toNode = getVertexWithValue(toValue);
        fromNode.addNeighbor(toNode);
    }

    public void addVertex(T value) {
        GraphNode<T> newNode = new GraphNode<>(value);
        vertices.add(newNode);
        valueMap.put(value, newNode);
    }

    public boolean isEmpty() {
        return vertices.size() == 0;
    }

    public GraphNode<T> getVertexWithValue(T value) {
        return valueMap.get(value);
    }

    public GraphNode<T> getRandomVertex() {
        if (isEmpty()) {
            return null;
        }

        Random generator = new Random();
        int randomIndex = generator.nextInt(vertices.size());
        return vertices.get(randomIndex);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (GraphNode<T> vertex : vertices) {
            stringBuilder.append(vertex.toString());
            stringBuilder.append(": ");
            stringBuilder.append(vertex.getNeighbors().toString());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    // removes vertices with no neighbors
    protected void prune() {
        int i = 0;
        while (i < vertices.size()) {
            GraphNode<T> vertex = vertices.get(i);
            if (vertex.getNeighbors().size() == 0) {
                vertices.remove(vertex);
                i--;
            }
            i++;
        }
    }

}
