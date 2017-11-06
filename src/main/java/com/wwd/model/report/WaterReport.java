package com.wwd.model.report;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * class to handle the instantiation of water reports
 */
public class WaterReport implements Serializable {

    private final ZonedDateTime date;
    private final int reportID;
    private final String reporterName;
    private final Location location;
    private final WaterType type;
    private final WaterCondition condition;

    /**
     * creates a water report and sets values
     *
     * @param date time of the report
     * @param reportID the ID number of the report
     * @param reporterName the name of the user passing in the report
     * @param location the location of the water
     * @param type the type of water
     * @param condition the condition/purity of the water
     */
    public WaterReport(ZonedDateTime date, int reportID, String reporterName,
                       Location location, WaterType type,
                       WaterCondition condition) {
        this.date = date;
        this.reportID = reportID;
        this.reporterName = reporterName;
        this.location = location;
        this.type = type;
        this.condition = condition;
    }

    /**
     * a getter for the date of the report
     *
     * @return the date of the report
     */
    public ZonedDateTime getDate() {
        return date;
    }

    /**
     * a getter for the ID# of the report
     *
     * @return the ID#
     */
    public int getID() {
        return reportID;
    }

    /**
     * a getter for the name of the user who submitted the report
     *
     * @return the user name
     */
    public String getReporterName() {
        return reporterName;
    }

    /**
     * a getter for the location the water report is referring to
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * a getter for the Enum type of the water
     *
     * @return the water type
     */
    public WaterType getType() {
        return type;
    }

    /**
     * a getter for the Enum condition of the water
     *
     * @return the water condition
     */
    public WaterCondition getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        String out = "";

        out += date.toString() + "\n";
        out += reportID + "\n";
        out += reporterName + "\n";
        out += location.toString() + "\n";
        out += type + "\n";
        out += condition + "\n";

        return out;
    }
}
