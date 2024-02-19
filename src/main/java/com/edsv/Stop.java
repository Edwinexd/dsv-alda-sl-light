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
