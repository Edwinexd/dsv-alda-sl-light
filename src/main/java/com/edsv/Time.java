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

    // TODO: Extremely unambiguous comparison!
    public int getDifference(Time departureTime) {
        int diff = this.minutes - departureTime.minutes;
        if (diff < 0) {
            // if we pass midnight we need to offset it
            diff += 24 * 60;
        }
        return diff;
    }

    @Override
    public int compareTo(Time o) {
        if (this.minutes < o.minutes) {
            return -1;
        } else if (this.minutes > o.minutes) {
            return 1;
        } else {
            return 0;
        }
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
