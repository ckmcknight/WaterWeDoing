package com.wwd.model.report;

import java.io.Serializable;

/**
 * a class handling the location of water reports
 */
public class Location implements Serializable {

    private final float latitude;
    private final float longitude;

    /**
     * Creates a location at (0.0, 0.0)
     */
    public Location() {
        latitude = 0.0f;
        longitude = 0.0f;
    }

    /**
     * creates a location with longitude and latitude coordinates
     *
     * @param longitude1 the longitude of the location
     * @param latitude1 the latitude of the location
     */
    public Location(float latitude1, float longitude1) {
        this.latitude = latitude1;
        this.longitude = longitude1;
    }

    /**
     * a getter for the latitude of the current location
     *
     * @return the latitude
     */
    public final float getLatitude() {
        return latitude;
    }

    /**
     * a getter for the longitude of the current location
     *
     * @return the longitude
     */
    public final float getLongitude() {
        return longitude;
    }

    /**
     * Checks if one Location is the same as another
     * @param o a Location to be checked
     * @return true if the Locations are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        Location test = (Location) o;
        return this.getLatitude() == test.getLatitude()
                && this.getLongitude() == test.getLongitude();
    }

    /**
     * Calculates a hashcode based on the latitude and longitude
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Float.hashCode(latitude) * 7 + Float.hashCode(longitude) * 11;
    }

    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }
}
