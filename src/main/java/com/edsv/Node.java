package com.edsv;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Node {
    private Stop stop;
    // All edges from this node, e.g. all trips that depart from this stop
    // private TreeSet<Edge> edges;

    private HashMap<Node, TreeSet<Edge>> edgesTo = new HashMap<>();

    public Node(Stop stop) {
        this.stop = stop;
    }

    public Stop getStop() {
        return stop;
    }

    // TODO: Add some iterator with a begin time, have that as the first element and then iterate over the rest, including looping around till we reach the beginning edge
    // should prob be done in a self-implemented treelike x skiplist structure so we can jump to a nearby edge

    public Set<Node> getDestinations() {
        return edgesTo.keySet();
    }

    public EdgeIterator getEdgesTo(Node to, Time begin) {
        if (edgesTo.containsKey(to)) {
            return new EdgeIterator(edgesTo.get(to), begin);
        }
        return new EdgeIterator(new TreeSet<>(), begin);
    }

    public EdgeIterator getEdgesTo(Node to) {
        return getEdgesTo(to, new Time(0, 0));
    }

    public void addEdge(Edge edge) {
        if (!edgesTo.containsKey(edge.getTo())) {
            edgesTo.put(edge.getTo(), new TreeSet<>());
        }
        edgesTo.get(edge.getTo()).add(edge);
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
                ", destinations=" + edgesTo.size() +
                '}';
    }
}
