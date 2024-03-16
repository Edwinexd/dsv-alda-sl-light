package com.edsv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class DataLoader {
    public static HashMap<Long, Node> load() {
        HashMap<Long, Stop> stops = loadStops();
        HashMap<Long, Route> routes = loadRoutes();
        HashMap<Long, Trip> trips = loadTrips(routes);
        HashMap<Trip, LinkedList<StopTime>> stopTimes = loadStopTimes(trips);
        for (Entry<Trip, LinkedList<StopTime>> entry : stopTimes.entrySet()) {
            entry.getKey().addStopTimes(entry.getValue());
        }
        HashMap<Long, Node> nodes = buildNodes(stops.values(), stopTimes.values());
        System.out.println("Max speed: " + getMaxSpeed(nodes.values()) + " meters per minute (excluding 0 minute travel times)");
        return nodes;
    }

    private static HashMap<Long, Stop> loadStops() {
        HashMap<Long, Stop> stops = new HashMap<>();
        // Load stops from resources/sl_stops.csv
        try (InputStreamReader isr = new InputStreamReader(DataLoader.class.getClassLoader().getResourceAsStream("sl_stops.csv"));
            BufferedReader br = new BufferedReader(isr)) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
               String[] parts = line.split(",");
               Long id = Long.parseLong(parts[0]);
               stops.put(id, new Stop(id, parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stops;
    }
    
    private static HashMap<Long, Trip> loadTrips(HashMap<Long, Route> routes) {
        HashMap<Long, Trip> trips = new HashMap<>();
        // Load trips from resources/sl_trips.csv
        try (InputStreamReader isr = new InputStreamReader(DataLoader.class.getClassLoader().getResourceAsStream("sl_trips.csv"));
            BufferedReader br = new BufferedReader(isr)) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
               String[] parts = line.split(",");
               long routeId = Long.parseLong(parts[0]);
               long serviceId = Long.parseLong(parts[1]);
               long tripId = Long.parseLong(parts[2]);
               String tripHeadsign = parts[3];
               String tripShortName = parts[4];
               Route route = routes.get(routeId);
               // Circular references, Trip.stopTimes are added later
               trips.put(tripId, new Trip(routeId, route, serviceId, tripId, tripHeadsign, tripShortName));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return trips;
    }

    private static HashMap<Trip, LinkedList<StopTime>> loadStopTimes(HashMap<Long, Trip> trips) {
        HashMap<Trip, LinkedList<StopTime>> stopTimes = new HashMap<>();
        // Load stop times from resources/sl_stop_times.csv
        try (InputStreamReader isr = new InputStreamReader(DataLoader.class.getClassLoader().getResourceAsStream("sl_stop_times.csv"));
            BufferedReader br = new BufferedReader(isr)) {
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
               LinkedList<StopTime> stopTimesSequence = stopTimes.get(trip);
               if (stopTimesSequence == null) {
                  stopTimesSequence = new LinkedList<>();
                  stopTimes.put(trip, stopTimesSequence);
               }
               stopTimesSequence.add(new StopTime(tripId, trip, arrivalTime, departureTime, stopId, stopSequence, pickupType, dropOffType));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stopTimes;
    }

    private static HashMap<Long, Route> loadRoutes() {
        HashMap<Long, Route> routes = new HashMap<>();
        // Load routes from resources/sl_routes.csv
        try (InputStreamReader isr = new InputStreamReader(DataLoader.class.getClassLoader().getResourceAsStream("sl_routes.csv"));
            BufferedReader br = new BufferedReader(isr)) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
               String[] parts = line.split(",");
               long id = Long.parseLong(parts[0]);
               String agencyId = parts[1];
               String shortName = parts[2];
               String longName = parts[3];
               int type = Integer.parseInt(parts[4]);
               String url = parts[5];
               routes.put(id, new Route(id, agencyId, shortName, longName, type, url));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return routes;
    }

    private static HashMap<Long, Node> buildNodes(Iterable<Stop> stops, Collection<LinkedList<StopTime>> collection) {
        HashMap<Long, Node> nodes = new HashMap<>();
        for (Stop stop : stops) {
            nodes.put(stop.getId(), new Node(stop));
        }

        for (List<StopTime> stopTimes : collection) {
            StopTime previous = null;
            for (StopTime stopTime : stopTimes) {
                if (previous != null) {
                    Node from = nodes.get(previous.getStopId());
                    Node to = nodes.get(stopTime.getStopId());
                    Edge edge = new Edge(to, previous, stopTime);
                    from.addEdge(edge);
                }
                previous = stopTime;
            }
        }
        return nodes;
    }

    private static int getMaxSpeed(Collection<Node> nodes) {
        // For each node, check all edges and check what the speed is
        // if it is the highest - update counter
        int maxSpeed = Integer.MIN_VALUE;
        for (Node node : nodes) {
            for (Node destination : node.getDestinations()) {
                for (Edge edge : node.getAllEdgesTo(destination)) {
                    int travelTime = edge.getTravelTime();
                    int metersTravelled = node.getStop().distanceTo(destination.getStop());
                    int speed = metersTravelled / Math.max(travelTime, 1);
                    if (speed > maxSpeed) {
                        maxSpeed = speed;
                    }
                }
            }
        }
        return maxSpeed;
    }
}
