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
