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

public class StopTime {
    private long tripId;
    private Trip trip;
    private Time arrivalTime;
    private Time departureTime;
    private long stopId;
    private int stopSequence;
    private int pickupType;
    private int dropOffType;

    public StopTime(long tripId, Trip trip, Time arrivalTime, Time departureTime, long stopId, int stopSequence, int pickupType, int dropOffType) {
        this.tripId = tripId;
        this.trip = trip;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.pickupType = pickupType;
        this.dropOffType = dropOffType;
    }

    public long getTripId() {
        return tripId;
    }

    public Trip getTrip() {
        return trip;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public long getStopId() {
        return stopId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public int getPickupType() {
        return pickupType;
    }

    public int getDropOffType() {
        return dropOffType;
    }

    @Override
    public String toString() {
        return "StopTime{" +
                "tripId=" + tripId +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", stopId=" + stopId +
                ", stopSequence=" + stopSequence +
                ", pickupType=" + pickupType +
                ", dropOffType=" + dropOffType +
                '}';
    }
}
