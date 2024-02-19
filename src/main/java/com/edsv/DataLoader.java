package com.edsv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

// TODO: Clean up this class
public class DataLoader {
    private HashMap<Long, Node> nodes = new HashMap<>();
    private TreeMap<Long, Stop> stops = new TreeMap<>();
    private TreeMap<Long, Trip> trips = new TreeMap<>();
    private TreeMap<Trip, List<StopTime>> stopTimes = new TreeMap<>();

    public void load() {
        try {
            loadStops();
            loadTrips();
            loadStopTimes();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        createNodes();
    }

    private void loadStops() throws IOException {
        // Load stops from resources/sl_stops.csv
        // TODO: Try with resources 
        InputStreamReader isr = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("sl_stops.csv"));
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            Long id = Long.parseLong(parts[0]);
            stops.put(id, new Stop(id, parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
        }
        System.out.println(stops.size());
    }
    
    private void loadTrips() throws IOException {
        // Load trips from resources/sl_trips.csv
        InputStreamReader isr = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("sl_trips.csv"));
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            long routeId = Long.parseLong(parts[0]);
            long serviceId = Long.parseLong(parts[1]);
            long tripId = Long.parseLong(parts[2]);
            String tripHeadsign = parts[3];
            String tripShortName = parts[4];
            trips.put(tripId, new Trip(routeId, serviceId, tripId, tripHeadsign, tripShortName, new LinkedList<>()));
        }
        System.out.println(trips.size());
    }

    private void loadStopTimes() throws IOException {
        // Load stop times from resources/sl_stop_times.csv
        InputStreamReader isr = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("sl_stop_times.csv"));
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            long tripId = Long.parseLong(parts[0]);
            long stopId = Long.parseLong(parts[3]);
            int stopSequence = Integer.parseInt(parts[4]);
            String[] arrivalTimeParts = parts[1].split(":");
            Time arrivalTime = new Time(Integer.parseInt(arrivalTimeParts[0]), Integer.parseInt(arrivalTimeParts[1]));
            String[] departureTimeParts = parts[2].split(":");
            Time departureTime = new Time(Integer.parseInt(departureTimeParts[0]), Integer.parseInt(departureTimeParts[1]));
            int pickupType = Integer.parseInt(parts[5]);
            int dropOffType = Integer.parseInt(parts[6]);
            Trip trip = trips.get(tripId);
            List<StopTime> stopTimes = this.stopTimes.get(trip);
            if (stopTimes == null) {
                stopTimes = new LinkedList<>();
                this.stopTimes.put(trip, stopTimes);
            }
            stopTimes.add(new StopTime(tripId, arrivalTime, departureTime, stopId, stopSequence, pickupType, dropOffType));
        }
        System.out.println(this.stopTimes.size());
        // get random key
        // System.out.println(this.stopTimes.firstEntry().getValue());
    }

    private void createNodes() {
        for (Stop stop : stops.values()) {
            nodes.put(stop.getId(), new Node(stop));
        }

        for (Entry<Trip, List<StopTime>> entry : stopTimes.entrySet()) {
            Trip trip = entry.getKey();
            List<StopTime> stopTimes = entry.getValue();
            StopTime previous = null;
            for (StopTime stopTime : stopTimes) {
                if (stopTime.getDropOffType() == 1) { // first of line
                    previous = stopTime;
                    continue;
                }
                Stop previousStop = stops.get(previous.getStopId());
                Stop currentStop = stops.get(stopTime.getStopId());

                // add edge to node
                // get previous stop node
                Node previousNode = nodes.get(previousStop.getId());
                // get current stop node
                Node currentNode = nodes.get(currentStop.getId());
                // create edge
                previousNode.addEdge(new Edge(currentNode, previous, stopTime));

                previous = stopTime;
            }
        }
        System.out.println();
        // print first node and outgoing edges
        Node firstNode = nodes.values().iterator().next();
        System.out.println(firstNode.getStop());
        System.out.println(firstNode.getDestinations().stream().map(n -> firstNode.getEdgesTo(n).next()).toList());
        // print trip name
        // System.out.println(trips.get(firstNode.getEdges().next().getDeparture().getTripId()).getTripHeadsign());
        // System.out.println(firstNode.getEdges().next().getTo().getStop());

        System.out.println();
        Node nextNode = firstNode;
        for (int i = 0; i < 5; i++) {
            // nextNode = nextNode.getEdges().next().getTo();
            // System.out.println(nextNode.getStop());
            // System.out.println(firstNode.getEdges().next());
            // System.out.println(nextNode.getEdges().get(0).getStopTime());
            // System.out.println(nextNode.getEdges().get(0).getTo().getStop());
            System.out.println();
        }
        // edge iterator to flat list
        List<Edge> edges = new ArrayList<>();
        EdgeIterator ei = firstNode.getEdgesTo(firstNode.getDestinations().iterator().next(), new Time(12, 0));
        while (ei.hasNext()) {
            edges.add(ei.next());
        }

        List<Time> x = edges.stream().map(e -> e.getDeparture().getDepartureTime()).toList();
        // x.sort((a,b) -> a.compareTo(b));
        System.out.println(x);

        // print haversine between first and second node
        System.out.println(firstNode.getStop().distanceTo(firstNode.getDestinations().iterator().next().getStop()));


        EdgeIterator emptyEi = new EdgeIterator(new TreeSet<>(), new Time(12, 0));

        while (emptyEi.hasNext()) {
            System.out.println(emptyEi.next().getDeparture().getDepartureTime());
        }

        EdgeIterator eiWithOne = new EdgeIterator(new TreeSet<>(List.of(new Edge(null, new StopTime(0, new Time(12, 0), new Time(12, 1), 0, 0, 0, 0), new StopTime(0, new Time(12, 5), new Time(12, 6), 0, 0, 0, 0)))), new Time(12, 2));

        while (eiWithOne.hasNext()) {
            System.out.println(eiWithOne.next().getDeparture().getDepartureTime());
        }

        System.out.println();
        System.out.println();
        System.out.println();

        Node hornstull = nodes.get(740021658L);
        System.out.println(hornstull.toString());
        System.out.println(hornstull.getDestinations());
        for (Node n : hornstull.getDestinations()) {
            System.out.println(n.getStop().getName() + ": " +
                n.getStop().distanceTo(hornstull.getStop()));
            if (n.getStop().getName().equals("Liljeholmen T-bana")) {
                EdgeIterator ei4 = hornstull.getEdgesTo(n, new Time(12, 0));
                while (ei4.hasNext()) {
                    Edge e = ei4.next();
                    System.out.println(e.getDeparture().getDepartureTime() + " -> " + e.getArrival().getArrivalTime());
                }
            }
        }
    }
}
