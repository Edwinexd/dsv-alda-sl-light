package com.edsv;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map.Entry;

public class AStarRouteFinder {

    private static class NodeEntry implements Comparable<NodeEntry> {
        // TODO Add getters and setters for inner class as people in the course lead
        // complains about this ...
        Node node;
        int cheapestCost = Integer.MAX_VALUE; // in minutes from startTime
        NodeEntry cheapestPrevious = null; // ~~TODO: This may be outdated~~ we update this one, just duplicates in the
                                           // queue
        Edge takenEdgeFromPrevious = null;

        NodeEntry(Node node) {
            this.node = node;
        }

        @Override
        public int compareTo(NodeEntry o) {
            if (this.cheapestCost < o.cheapestCost) {
                return -1;
            } else if (this.cheapestCost > o.cheapestCost) {
                return 1;
            } else {
                return 0;
            }

        }
    }

    private static class QueueEntry implements Comparable<QueueEntry> {
        NodeEntry entry;
        int cost;
        int priority; // cost + heuristics

        QueueEntry(NodeEntry entry, int cost, int priority) {
            this.entry = entry;
            this.cost = cost;
            this.priority = priority;
        }

        @Override
        public int compareTo(QueueEntry o) {
            if (this.priority < o.priority) {
                return -1;
            } else if (this.priority > o.priority) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // TODO: Remove duplicate routes, e.g. a->b b->c may both be on tripId 1 and we
    // should then only return a->c
    public static LinkedList<Node> findRoute(HashMap<Long, Node> nodes, Node start, Node end, Time departureTime) {
        // TODO: Add penalty for changing trip, teleporting between trains to save a minute doesn't work in reality

        // Heuristic, distance to goal
        HashMap<Long, Integer> distanceToGoal = new HashMap<>();
        for (Node node : nodes.values()) {
            distanceToGoal.put(node.getStop().getId(), node.getStop().distanceTo(end.getStop()));
        }

        // This wastes memory as we never remove anything from this mapping
        HashMap<Node, NodeEntry> nodeEntries = new HashMap<>();
        for (Node node : nodes.values()) {
            nodeEntries.put(node, new NodeEntry(node));
        }
        nodeEntries.get(start).cheapestCost = 0;

        // Solution to updating the priority queue, we keep track of the actual cheapest in a special QueueEntry attr
        // that way we can push the same node multiple times with different costs and only process the cheapest one
        // avoids expensive lookups in the queue
        // Inspired by the idea of using a hashmap for this from:
        // https://stackoverflow.com/questions/1871253/updating-java-priorityqueue-when-its-elements-change-priority
        PriorityQueue<QueueEntry> queue = new PriorityQueue<>();
        // queue.addAll(nodeEntries.values().stream().map(ne -> new QueueEntry(ne, ne.cheapestCost)).toList());
        queue.add(new QueueEntry(nodeEntries.get(start), 0, 0));

        while (!queue.isEmpty()) {
            QueueEntry polled = queue.poll();
            if (polled.cost != polled.entry.cheapestCost) {
                // System.out.println("skipping: " + polled.cost + " " + polled.entry.cheapestCost);
                continue;
            }
            NodeEntry current = polled.entry;
            if (current.node.equals(end)) {
                System.out.println("Done");
                break;
            }

            // TODO Use distanceToGoal heuristic
            for (Node destination : current.node.getDestinations()) {
                if (current.node == start) {
                    System.out.println("Destination: " + destination.getStop().getName());
                }
                // TODO: This needs to take into account changing trip
                Edge cheapestEdge = current.node
                        .getEdgesTo(destination, new Time(current.cheapestCost + departureTime.getMinutes()))
                        .next();
                // Cost to get here + time until edge.getArrival() is the cost to get to the next node
                int newCost = current.cheapestCost;
                if (current.node == start) {
                    System.out.println("Cheapest edge: " + cheapestEdge.getDeparture().getDepartureTime() + " vs " + departureTime);
                }
                int minutesTillArrival = cheapestEdge.getArrival().getArrivalTime().getDifference(new Time(current.cheapestCost + departureTime.getMinutes()));

                newCost += minutesTillArrival;

                // heuristic
                // newCost += distanceToGoal.get(destination.getStop().getId());

                if (newCost < 0) {
                    System.out.println("Negative cost: " + newCost);
                }

                NodeEntry destinationEntry = nodeEntries.get(destination);
                if (newCost < destinationEntry.cheapestCost) {
                    if (current.node == start) {
                        System.out.println("New cost: " + newCost);
                    }
                    destinationEntry.cheapestCost = newCost;
                    destinationEntry.cheapestPrevious = current;
                    destinationEntry.takenEdgeFromPrevious = cheapestEdge;
                    queue.add(new QueueEntry(destinationEntry, newCost, newCost));   // includes revisting an already
                                                                                     // visited node with a
                                                                                     // cheaper cost
                                                                                     // as we might be able to take a
                                                                                     // "cheaper connection"
                                                                                     // from it now
                }
            }
        }

        // TODO: Build path
        LinkedList<Node> path = new LinkedList<>();
        LinkedList<Edge> edges = new LinkedList<>();
        Node current = end;
        while (current != start) {
            path.addFirst(current);
            edges.addFirst(nodeEntries.get(current).takenEdgeFromPrevious);
            current = nodeEntries.get(current).cheapestPrevious.node;
        }
        path.addFirst(start);
        
        // print path, excluding repeating the name of the same trip
        long lastTripId = -1;
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            if (edge.getDeparture().getTripId() != lastTripId) {
                // print what station and time we swap trip
                if (i > 0) {
                    System.out.println(edges.get(i-1).getTo().getStop().getName());
                    System.out.println(edge.getDeparture().getDepartureTime());
                }
                System.out.println(edge.getDeparture().getTripId());
                lastTripId = edge.getDeparture().getTripId();
            }
            // System.out.println(edge.getDeparture().getDepartureTime() + " -> " + edge.getArrival().getArrivalTime());
            // System.out.println(edge.getTo().getStop().getName());
        }

        return path;
    }
}
