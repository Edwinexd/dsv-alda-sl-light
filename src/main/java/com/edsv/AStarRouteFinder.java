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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Map.Entry;

public class AStarRouteFinder {
    // This value comes from a brute force calculation of the dataset*
    private static final int MAX_SPEED = 1169; // meters per minute

    private static class NodeEntry implements Comparable<NodeEntry> {
        Node node;
        int cost = Integer.MAX_VALUE; // in minutes from startTime
        NodeEntry previous; // for reconstructing path
        Edge previousEdge;

        NodeEntry(Node node, int cost) {
            this.node = node;
            this.cost = cost;
        }
        NodeEntry(Node node, int cost, NodeEntry previous, Edge previousEdge) {
            this.node = node;
            this.cost = cost;
            this.previous = previous;
            this.previousEdge = previousEdge;
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
    /*
    public static LinkedList<Edge> findRoute(HashMap<Long, Node> nodes, Node start, Node end, Time departureTime) {

        // Heuristic, distance to goal
        HashMap<Long, Integer> distanceToGoal = new HashMap<>();
        for (Node node : nodes.values()) {
            distanceToGoal.put(node.getStop().getId(), node.getStop().distanceTo(end.getStop()));
        }

        // This wastes memory as we never remove anything from this mapping
        // ... because this map lacks the "time" dimension we can't have a simple
        // greedy algorithm and we instead need to follow the actual navigated path (based on NodeEntry.previous => start)
        HashMap<Node, NodeEntry> cheapestCosts = new HashMap<>();
        // for (Node node : nodes.values()) {
            // nodeEntries.put(node, new NodeEntry(node));
        // }
        NodeEntry startEntry = new NodeEntry(start, 0);
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
                LinkedList<Edge> path = new LinkedList<>();
                NodeEntry currentEntry = current;
                while (currentEntry.previous != null) {
                    path.addFirst(currentEntry.previousEdge);
                    currentEntry = currentEntry.previous;
                }
                return path;
            }

            for (Node destination : current.node.getDestinations()) {
                int goalDistance = distanceToGoal.get(destination.getStop().getId());
                Time currentTime = new Time(departureTime.getMinutes() + current.cost);

                // 00:05, 06:03, 07:01 - sorted iteration
                for (Entry<Route, Edge> entry : current.node.getFirstEdgesTo(destination,
                    (Edge e) -> {
                        if (e.getDeparture().getDepartureTime().getMinutes() < currentTime.getMinutes()) {
                            return false;
                        }
                        if (current.previousEdge != null && current.previousEdge.getArrival().getTripId() != e.getDeparture().getTripId()) {
                            if (e.getDeparture().getDepartureTime().getDifference(currentTime) < 5) {
                                return false;
                            }
                        }
                        return true;
                    }
                )) {
                    Route route = entry.getKey();
                    Edge edge = entry.getValue();
                    // "Crawl" cheapest edge via each node
                    // so for each route, skip edges as long as they're in the past or we require an illegal transfer (< 5 minutes)
                    // , then choose one (which will be the cheapest)

                    // newCost = cost from start to current + wait time for the edge.getDeparture().getDepartureTime() + edge.getTravelTime()
                    int newCost = current.cost + edge.getDeparture().getDepartureTime().getDifference(currentTime) + edge.getTravelTime();
                    NodeEntry newEntry = new NodeEntry(destination, newCost, current, edge);
                    if (newCost < cheapestCosts.getOrDefault(destination, new NodeEntry(destination, Integer.MAX_VALUE)).cost) {
                        cheapestCosts.put(destination, newEntry);
                    }
                    int estimatedMinutesToGoal = goalDistance / MAX_SPEED;
                    int priority = newCost + estimatedMinutesToGoal;
                    // TODO: Penalizing transfers is prob a good idea but shouldn't be done from this
                    // priority += numberOfTransfers; // if two routes end up having the same cost we prefer the one with fewer transfers
                    queue.add(new QueueEntry(newEntry, newCost, priority));
                }
            }
        }

        return null;
    }
    */
    public static LinkedList<Edge> findRoute(HashMap<Long, Node> nodes, Node start, Node end, Time departureTime) {
        // Heuristic, distance to goal
        HashMap<Stop, Integer> distanceToGoal = new HashMap<>();
        for (Node node : nodes.values()) {
            distanceToGoal.put(node.getStop(), node.getStop().distanceTo(end.getStop()));
        }

        // To accurately keep track of cheapest costs there are 2 (3) dimensions to consider:
        // 1. To which node
        // 2. Which route/trip id we took
        // (3. Time)
        HashMap<Node, HashMap<Route, NodeEntry>> cheapestCosts = new HashMap<>();

        NodeEntry startEntry = new NodeEntry(start, 0);

        cheapestCosts.put(start, new HashMap<>());
        cheapestCosts.get(start).put(null, startEntry);


        // Inspired by the idea not updating the priorit queue per:
        // https://stackoverflow.com/questions/1871253/updating-java-priorityqueue-when-its-elements-change-priority
        // and instead keeping track of the actual cheapest cost/priority separately and validating after each poll
        PriorityQueue<QueueEntry> queue = new PriorityQueue<>();
        queue.add(new QueueEntry(startEntry, 0, 0));

        while (!queue.isEmpty()) {
            QueueEntry polled = queue.poll();

            NodeEntry current = polled.entry;

            if (current.previousEdge != null && polled.cost != cheapestCosts.get(current.node).get(current.previousEdge.getDeparture().getTrip().getRoute()).cost) {
                continue;
            }

            if (current.node.equals(end)) {
                LinkedList<Edge> path = new LinkedList<>();
                NodeEntry currentEntry = current;
                while (currentEntry.previous != null) {
                    path.addFirst(currentEntry.previousEdge);
                    currentEntry = currentEntry.previous;
                }
                return path;
            }

            for (Node destination : current.node.getDestinations()) {
                int goalDistance = distanceToGoal.get(destination.getStop());
                Time currentTime = new Time(departureTime.getMinutes() + current.cost);

                for (Entry<Route, Edge> entry : current.node.getFirstEdgesTo(destination,
                    (Edge e) -> {
                        if (e.getDeparture().getDepartureTime().getMinutes() < currentTime.getMinutes()) {
                            return false;
                        }
                        if (current.previousEdge != null && current.previousEdge.getArrival().getTripId() != e.getDeparture().getTripId()) {
                            if (e.getDeparture().getDepartureTime().getDifference(currentTime) < 5) {
                                return false;
                            }
                        }
                        return true;
                    }
                )) {
                    Route route = entry.getKey();
                    Edge edge = entry.getValue();

                    // newCost = cost from start to current + wait time for the edge.getDeparture().getDepartureTime() + edge.getTravelTime()
                    int newCost = current.cost + edge.getDeparture().getDepartureTime().getDifference(currentTime) + edge.getTravelTime();

                    cheapestCosts.putIfAbsent(destination, new HashMap<>());
                    if (!cheapestCosts.get(destination).containsKey(route) || newCost < cheapestCosts.get(destination).get(route).cost) {
                        NodeEntry newEntry = new NodeEntry(destination, newCost, current, edge);

                        cheapestCosts.get(destination).put(route, newEntry);
                        int estimatedMinutesToGoal = goalDistance / MAX_SPEED;
                        int priority = newCost + estimatedMinutesToGoal;
                        queue.add(new QueueEntry(newEntry, newCost, priority));
                    }
                }
            }
        }
        return null;
    }


    // This is a pure A* implementation, it is practically useless as it 
    // can't consider taking different routes to the same destination
    // which in turn may allow a cheaper route to be found
    // for example: taking a later connection at T-Centralen -> Gamla Stan may be more expensive right now
    // but may be cheaper to get to Hagsätra in the end
    public static LinkedList<Edge> findRoutePureAStar(HashMap<Long, Node> nodes, Node start, Node end, Time departureTime) {
        // Heuristic, distance to goal
        HashMap<Long, Integer> distanceToGoal = new HashMap<>();
        for (Node node : nodes.values()) {
            distanceToGoal.put(node.getStop().getId(), node.getStop().distanceTo(end.getStop()));
        }

        HashMap<Node, NodeEntry> cheapestCosts = new HashMap<>();
        
        NodeEntry startEntry = new NodeEntry(start, 0);
        cheapestCosts.put(start, startEntry);

        // to allow updates of priority of elements in the queue we always compare QueueEntry.cost to cheapestCosts
        // if they differ we skip the element as the actual cheapest is already in the queue or has already been processed
        // inspired by https://stackoverflow.com/questions/1871253/updating-java-priorityqueue-when-its-elements-change-priority
        // cheaper then updating priority of elements in the queue
        PriorityQueue<QueueEntry> queue = new PriorityQueue<>();
        queue.add(new QueueEntry(startEntry, 0, 0));

        while (!queue.isEmpty()) {
            QueueEntry polled = queue.poll();
            if (polled.cost != cheapestCosts.get(polled.entry.node).cost) {
                continue;
            }
            NodeEntry current = polled.entry;
            if (current.node.equals(end)) {
                LinkedList<Edge> path = new LinkedList<>();
                NodeEntry currentEntry = current;
                while (currentEntry.previous != null) {
                    path.addFirst(currentEntry.previousEdge);
                    currentEntry = currentEntry.previous;
                }
                return path;
            }

            for (Node destination : current.node.getDestinations()) {
                int goalDistance = distanceToGoal.get(destination.getStop().getId());
                Time currentTime = new Time(departureTime.getMinutes() + current.cost);

                for (Edge edge : current.node.getAllEdgesTo(destination)) {
                    if (edge.getDeparture().getDepartureTime().getMinutes() < currentTime.getMinutes()) {
                        continue;
                    }
                    if (current.previousEdge != null && current.previousEdge.getArrival().getTripId() != edge.getDeparture().getTripId()) {
                        if (edge.getDeparture().getDepartureTime().getDifference(currentTime) < 5) {
                            continue;
                        }
                    }
                    int newCost = current.cost + edge.getDeparture().getDepartureTime().getDifference(currentTime) + edge.getTravelTime();
                    if (!cheapestCosts.containsKey(destination) || newCost < cheapestCosts.get(destination).cost) {
                        NodeEntry newEntry = new NodeEntry(destination, newCost, current, edge);
                        cheapestCosts.put(destination, newEntry);
                        int estimatedMinutesToGoal = goalDistance / MAX_SPEED;
                        int priority = newCost + estimatedMinutesToGoal;
                        queue.add(new QueueEntry(newEntry, newCost, priority));
                    }
                    break;
                }
            }
        }

        return null;
    }
}
