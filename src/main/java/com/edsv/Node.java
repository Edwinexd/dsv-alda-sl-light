package com.edsv;

import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class Node {
    private Stop stop;
    private EdgeCollection edgesToByRoute = new EdgeCollection();

    public Node(Stop stop) {
        this.stop = stop;
    }

    public Stop getStop() {
        return stop;
    }

    public Set<Node> getDestinations() {
        return edgesToByRoute.getDestinations();
    }

    public SortedSet<Edge> getAllEdgesTo(Node to) {
        return edgesToByRoute.getAllEdgesTo(to);
    }

    public Set<Entry<Route, Edge>> getFirstEdgesTo(Node to, Predicate<Edge> filter) {
        return edgesToByRoute.getFirstEdgesTo(to, filter);
    }


    public void addEdge(Edge edge) {
        edgesToByRoute.addEdge(edge);
    }

    @Override
    public int hashCode() {
        return stop.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Node))
            return false;
        Node node = (Node) obj;
        return node.stop.equals(this.stop);
    }

    @Override
    public String toString() {
        return "Node{" +
                "stop=" + stop +
                ", destinations=" + edgesToByRoute.getDestinations().size() +
                '}';
    }
}
