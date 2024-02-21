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
