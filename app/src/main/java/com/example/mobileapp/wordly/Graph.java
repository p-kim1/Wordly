//
// Graph.java
// Joseph Nguyen
//
package com.example.mobileapp.wordly;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;

class GraphNode<T> {
    private T value;

    // HashSet allows for quick checking of neighbors.
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

    @NonNull
    @Override
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


    public ArrayList<GraphNode<T>> getPath(GraphNode<T> fromNode, GraphNode<T> toNode, int depthLimit) {
        LinkedList<GraphNode<T>> nodeQueue = new LinkedList<>();
        LinkedList<Integer> depthQueue = new LinkedList<>();
        HashSet<GraphNode<T>> visitedNodes = new HashSet<>();
        HashMap<GraphNode<T>, GraphNode<T>> parentMap = new HashMap<>();

        nodeQueue.add(fromNode);
        depthQueue.add(0);
        parentMap.put(fromNode, null);
        visitedNodes.add(fromNode);

        while (!nodeQueue.isEmpty()) {
            GraphNode<T> temp = nodeQueue.removeFirst();
            int depth = depthQueue.removeFirst();

            if (depth == depthLimit || temp == null) {
                return null;
            }

            for (GraphNode<T> neighbor : temp.getNeighbors()) {
                if (!visitedNodes.contains(neighbor)) {
                    parentMap.put(neighbor, temp);
                    nodeQueue.add(neighbor);
                    depthQueue.add(depth + 1);

                    if (neighbor == toNode) {
                        return constructPath(parentMap, toNode);
                    }

                    visitedNodes.add(neighbor);
                }
            }
        }

        return null;
    }

    public ArrayList<GraphNode<T>> constructPath(HashMap<GraphNode<T>, GraphNode<T>> parentMap, GraphNode<T> toNode) {
        ArrayList<GraphNode<T>> path = new ArrayList<>();

        path.add(toNode);
        GraphNode<T> parent = parentMap.get(toNode);

        while (parent != null) {
            path.add(parent);
            parent = parentMap.get(parent);
        }

        Collections.reverse(path);
        return path;
    }

    public ArrayList<GraphNode<T>> getRandomPath(int pathSize) {
        if (isEmpty()) {
            return null;
        }

        ArrayList<GraphNode<T>> path = null;
        while (path == null) {
            path = getRandomPathHelper(pathSize);
        }

        return path;
    }


    public ArrayList<GraphNode<T>> getRandomPathHelper(int pathSize) {
        HashSet<T> uniqueValues = new HashSet<>();
        ArrayList<GraphNode<T>> path = new ArrayList<>();
        GraphNode<T> vertex = getRandomVertex();

        path.add(vertex);
        uniqueValues.add(vertex.getValue());

        while (path.size() < pathSize) {
            GraphNode<T> next = vertex.getRandomNeighbor();
            if (uniqueValues.contains(next.getValue())) {
                return null;
            } else {
                uniqueValues.add(next.getValue());
                path.add(next);
            }
            vertex = next;
        }

        return path;
    }

    @NonNull
    @Override
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

    // Removes vertices with no neighbors.
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
