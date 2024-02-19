package com.edsv;

/**
 * Oversimplified time class, representing a time of day.
 */
public class Time implements Comparable<Time> {
    private int minutes;

    public Time(int minutes) {
        this.minutes = minutes % (24 * 60); // some data in our dataset contains departures at 24:XX for some reason
    }

    public Time(int hours, int minutes) {
        this(hours * 60 + minutes);
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return minutes / 60;
    }

    public int getMinutesOfHour() {
        return minutes % 60;
    }

    public int getDifference(Time departureTime) {
        return this.minutes - departureTime.minutes;
    }

    @Override
    public int compareTo(Time o) {
        return Integer.compare(this.minutes, o.minutes);
    }

    @Override
    public int hashCode() {
        return minutes * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Time))
            return false;
        Time time = (Time) obj;
        return time.minutes == this.minutes;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", getHours(), getMinutesOfHour());
    }

    
}
