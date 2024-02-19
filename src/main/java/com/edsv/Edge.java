package com.edsv;

public class Edge implements Comparable<Edge> {
    private Node to;
    private StopTime departure;
    private StopTime arrival;

    // TODO: One current problem with this class is that its impossible to calculate how expensive taking this edge is
    // we will need the StopTime departuretime of the from Node to calculate this

    public Edge(Node to, StopTime departure, StopTime arrival) {
        this.to = to;
        this.departure = departure;
        this.arrival = arrival;
    }

    public Node getTo() {
        return to;
    }

    public StopTime getDeparture() {
        return departure;
    }

    public StopTime getArrival() {
        return arrival;
    }

    public int getTravelTime() {
        return arrival.getArrivalTime().getDifference(departure.getDepartureTime());
    }

    @Override
    public int compareTo(Edge o) {
        return this.departure.getDepartureTime().compareTo(o.departure.getDepartureTime());
    }

    @Override
    public String toString() {
        return "Edge{" +
                "to=" + to +
                ", departure=" + departure +
                ", arrival=" + arrival +
                ", travelTime=" + getTravelTime() +
                '}';
    }
}
