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

public class Stop implements Comparable<Stop> {    
    private long id;
    private String name;
    // Not currently used:
    private double latitude;
    private double longitude;

    public Stop(long id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Uses the haversine formula to give an approximante distance between 2 stops based on their .latituide and .longitude values.
     * Can be used as a heurustic for the A* algorithm
     * @param other - the stop tp approximate distance to
     * @return approximate distance in meters
     */
    public int distanceTo(Stop other) {
        return Haversine.distanceBetween(this.latitude, this.longitude, other.latitude, other.longitude);
    }

    @Override
    public int compareTo(Stop o) {
        return Long.compare(this.id, o.id);
    }

    @Override
    public int hashCode() {
        return (int) (id * 31);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Stop))
            return false;
        Stop stop = (Stop) obj;
        return stop.id == this.id;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
