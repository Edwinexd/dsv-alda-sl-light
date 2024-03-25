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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Trip implements Comparable<Trip> {
    // route_id,service_id,trip_id,trip_headsign,trip_short_name
    private long routeId;
    private Route route;
    private long serviceId;
    private long tripId;
    private String tripHeadsign;
    private String tripShortName;

    private ArrayList<StopTime> stopTimes = new ArrayList<>();

    public Trip(long routeId, Route route, long serviceId, long tripId, String tripHeadsign, String tripShortName) {
        this.routeId = routeId;
        this.route = route;
        this.serviceId = serviceId;
        this.tripId = tripId;
        this.tripHeadsign = tripHeadsign;
        this.tripShortName = tripShortName;
    }

    public Trip(long routeId, Route route, long serviceId, long tripId, String tripHeadsign, String tripShortName, Collection<StopTime> stopTimes) {
        this(routeId, route, serviceId, tripId, tripHeadsign, tripShortName);
        this.stopTimes.addAll(stopTimes);
    }

    public long getRouteId() {
        return routeId;
    }

    public Route getRoute() {
        return route;
    }

    public long getServiceId() {
        return serviceId;
    }

    public long getTripId() {
        return tripId;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public String getTripShortName() {
        return tripShortName;
    }

    public List<StopTime> getStopTimes() {
        return Collections.unmodifiableList(stopTimes);
    }

    void addStopTime(StopTime stopTime) {
        stopTimes.add(stopTime);
    }

    void addStopTimes(List<StopTime> stopTimes) {
        this.stopTimes.addAll(stopTimes);
    }

    @Override
    public int compareTo(Trip o) {
        return Long.compare(this.tripId, o.tripId);
    }

    @Override
    public int hashCode() {
        return (int) (tripId * 31);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Trip))
            return false;
        Trip trip = (Trip) obj;
        return trip.tripId == this.tripId;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "routeId=" + routeId +
                ", serviceId=" + serviceId +
                ", tripId=" + tripId +
                ", tripHeadsign='" + tripHeadsign + '\'' +
                ", tripShortName='" + tripShortName + '\'' +
                ", stopTimes=" + stopTimes +
                '}';
    }

}
