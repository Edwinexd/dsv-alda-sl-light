package com.edsv;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Map.Entry;

public class AStarRouteFinder {

    private static class NodeEntry implements Comparable<NodeEntry> {
        Node node;
        int cost = Integer.MAX_VALUE; // in minutes from startTime
        List<Edge> pathTaken = new LinkedList<>();

        NodeEntry(Node node) {
            this.node = node;
        }

        @Override
        public int compareTo(NodeEntry o) {
            if (this.cost < o.cost) {
                return -1;
            } else if (this.cost > o.cost) {
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
    public static LinkedList<Edge> findRoute(HashMap<Long, Node> nodes, Node start, Node end, Time departureTime) {

        // Heuristic, distance to goal
        HashMap<Long, Integer> distanceToGoal = new HashMap<>();
        for (Node node : nodes.values()) {
            distanceToGoal.put(node.getStop().getId(), node.getStop().distanceTo(end.getStop()));
        }

        // This wastes memory as we never remove anything from this mapping
        HashMap<Node, NodeEntry> cheapestCosts = new HashMap<>();
        // for (Node node : nodes.values()) {
            // nodeEntries.put(node, new NodeEntry(node));
        // }
        NodeEntry startEntry = new NodeEntry(start);
        startEntry.cost = 0;
        cheapestCosts.put(start, startEntry);

        // Solution to updating the priority queue, we keep track of the actual cheapest in a special QueueEntry attr
        // that way we can push the same node multiple times with different costs and only process the cheapest one
        // avoids expensive lookups in the queue
        // Inspired by the idea of using a hashmap for this from:
        // https://stackoverflow.com/questions/1871253/updating-java-priorityqueue-when-its-elements-change-priority
        PriorityQueue<QueueEntry> queue = new PriorityQueue<>();
        // queue.addAll(nodeEntries.values().stream().map(ne -> new QueueEntry(ne, ne.cheapestCost)).toList());
        queue.add(new QueueEntry(startEntry, 0, 0));

        while (!queue.isEmpty()) {
            QueueEntry polled = queue.poll();
            // if (polled.cost != polled.entry.cheapestCost) {
                // System.out.println("skipping: " + polled.cost + " " + polled.entry.cheapestCost);
                // continue;
            // }
            NodeEntry current = polled.entry;
            if (current.node.equals(end)) {
                System.out.println("Done");
                // break; // decreases travel time by 20 minutes for some reason
            }

            for (Node destination : current.node.getDestinations()) {
                int heuristic = distanceToGoal.get(destination.getStop().getId());
                Time currentTime = new Time(departureTime.getMinutes() + current.cost);
                // 00:05, 06:03, 07:01 - sorted iteration
                for (Entry<Route, TreeSet<Edge>> entry : current.node.getEdgesToRoutes(destination)) {
                    Route route = entry.getKey();
                    // "Crawl" cheapest edge via each node
                    // so for each route, skip edges as long as they're in the past or we require an illegal transfer (< 5 minutes)
                    // , then choose one (which will be the cheapest)
                    for (Edge edge : entry.getValue()) {
                        // if edge has departed
                        if (edge.getDeparture().getDepartureTime().getMinutes() < currentTime.getMinutes()) {
                            continue;
                        }

                        if (current.pathTaken.size() != 0 && current.pathTaken.getLast().getArrival().getTripId() != edge.getDeparture().getTripId()) {
                            // Departure must be in more than 5 minutes if we are switching route
                            if (edge.getDeparture().getDepartureTime().getDifference(currentTime) < 5) {
                                continue;
                            }
                        }
                        // newCost = cost from start to current + wait time for the edge.getDeparture().getDepartureTime() + edge.getTravelTime()
                        int newCost = current.cost + edge.getDeparture().getDepartureTime().getDifference(currentTime) + edge.getTravelTime();
                        NodeEntry newEntry = new NodeEntry(destination);
                        newEntry.cost = newCost;
                        newEntry.pathTaken.addAll(current.pathTaken);
                        newEntry.pathTaken.add(edge);
                        if (newCost < cheapestCosts.getOrDefault(destination, new NodeEntry(destination)).cost) {
                            cheapestCosts.put(destination, newEntry);
                            queue.add(new QueueEntry(newEntry, newCost, newCost + heuristic));
                        }
                        break;
                    }
                }
            }
        }


        NodeEntry endEntry = cheapestCosts.get(end);
        // count unique trip ids
        System.out.println(endEntry.pathTaken.stream().map(e -> e.getDeparture().getTripId()).distinct().count());

        System.out.println(endEntry.pathTaken);
        System.out.println(endEntry.pathTaken.stream().map(e -> e.getDeparture().getTripId()).toList());

        System.out.println(endEntry.cost);

        // TODO: Build path
        return null;
    }
}
