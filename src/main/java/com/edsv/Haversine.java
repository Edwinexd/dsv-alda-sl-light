package com.edsv;

// Based on https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
// and https://en.wikipedia.org/wiki/Haversine_formula
public class Haversine {
    private static final double EARTH_RADIUS_KM = 6371.0;

    public static int distanceBetween(double latitude1, double longitude1, double latitude2, double longitude2) {

        double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        double deltaLongitude = Math.toRadians(longitude2 - longitude2);

        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);

        double a = haversine(deltaLatitude) + Math.cos(latitude1) * Math.cos(latitude2) * haversine(deltaLongitude);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (EARTH_RADIUS_KM * c * 1000);
    }

    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
