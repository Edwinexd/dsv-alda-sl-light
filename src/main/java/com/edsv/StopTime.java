package com.edsv;

public class StopTime {
    private long tripId;
    private Time arrivalTime;
    private Time departureTime;
    private long stopId;
    private int stopSequence;
    private int pickupType;
    private int dropOffType;

    public StopTime(long tripId, Time arrivalTime, Time departureTime, long stopId, int stopSequence, int pickupType, int dropOffType) {
        this.tripId = tripId;
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
