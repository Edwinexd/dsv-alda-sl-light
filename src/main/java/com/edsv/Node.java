/* 
dsv-alda-sl-light: A representation of the SL transit system using a graph with path finding capabilities using the A* algorithm, written in Java.
Copyright (C) 2024 Edwin Sundberg

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.edsv;

import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;
import java.util.function.Predicate;

/*
 * A node in the graph, representing a stop.
 * All nodes are stored in a map with their id as key.
 * Each node has a collection of edges to other nodes grouped by their respective routes.
 * Edges are sorted by departure time (ascending) as we usually iterate from a specific time to find the first suitable departure.
 */
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
