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
