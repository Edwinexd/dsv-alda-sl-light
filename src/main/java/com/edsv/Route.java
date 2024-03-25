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

public class Route {
    // route_id,agency_id,route_short_name,route_long_name,route_type,route_url
    private long id;
    private String agencyId;
    private String shortName;
    private String longName;
    private int type;
    private String url;

    public Route(long id, String agencyId, String shortName, String longName, int type, String url) {
        this.id = id;
        this.agencyId = agencyId;
        this.shortName = shortName;
        this.longName = longName;
        this.type = type;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Route route = (Route) obj;
        return route.getId() == this.id;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", agencyId='" + agencyId + '\'' +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                '}';
    }
}
