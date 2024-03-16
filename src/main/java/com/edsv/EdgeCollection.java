package com.edsv;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Sorted skip-list like collection of edges allowing loop-around iteration from a specific time.
 * Does <b>not</b> support removal of elements as the dataset is immutable. Support could be trivially be added if needed.
 */
public class EdgeCollection {
    // TreeSet<Edge> is used to keep the edges sorted by departure time
    private HashMap<Node, HashMap<Route, TreeSet<Edge>>> edgesToByRoute = new HashMap<>();
    
    public void addEdge(Edge edge) {
        if (!edgesToByRoute.containsKey(edge.getTo())) {
            edgesToByRoute.put(edge.getTo(), new HashMap<>());
        }
        if (!edgesToByRoute.get(edge.getTo()).containsKey(edge.getDeparture().getTrip().getRoute())) {
            edgesToByRoute.get(edge.getTo()).put(edge.getDeparture().getTrip().getRoute(), new TreeSet<>());
        }
        edgesToByRoute.get(edge.getTo()).get(edge.getDeparture().getTrip().getRoute()).add(edge);
    }

    public Set<Node> getDestinations() {
        return Collections.unmodifiableSet(edgesToByRoute.keySet());
    }
    

    public SortedSet<Edge> getAllEdgesTo(Node to) {
        TreeSet<Edge> edges = new TreeSet<>();
        for (TreeSet<Edge> edgeSet : edgesToByRoute.get(to).values()) {
            edges.addAll(edgeSet);
        }
        return Collections.unmodifiableSortedSet(edges);
    }

    public SortedSet<Edge> getEdgesTo(Node to, Route route) {
        return Collections.unmodifiableSortedSet(edgesToByRoute.get(to).get(route));
    }

    /*
     * Returns the first suitable edge to the given node via each possible route.
     * Filters edges by the given predicate. The given predicate could for example be to discard edges that are in the past based on a locally available current time.
     * 
     * @param to The destination node
     * @param filter A filter to apply to the edges
     */
    public Set<Entry<Route, Edge>> getFirstEdgesTo(Node to, Predicate<Edge> filter) {
        return edgesToByRoute.get(to).entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().stream().filter(filter).findFirst()))
                .filter(entry -> entry.getValue().isPresent())
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().get()))
                .collect(Collectors.toUnmodifiableSet());
    }
}
