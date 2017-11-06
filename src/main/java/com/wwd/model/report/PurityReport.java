package com.wwd.model.report;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * class to handle the instantiation of water reports
 */
public class PurityReport implements Serializable {

    private final ZonedDateTime date;
    private final int reportID;
    private final String reporterName;
    private final Location location;
    private final OverallCondition condition;
    private final int virusPPM;
    private final int contaminantPPM;

    /**
     * creates a water report and sets values
     *
     * @param date time of the report
     * @param reportID the ID number of the report
     * @param reporterName the name of the user passing in the report
     * @param location the location of the water
     * @param condition the condition/purity of the water
     * @param virusPPM  the virus PPM of the water
     * @param contaminantPPM  the contaminant PPM of the water
     */
    public PurityReport(ZonedDateTime date, int reportID, String reporterName,
                        Location location, OverallCondition condition,
                        int virusPPM, int contaminantPPM) {
        this.date = date;
        this.reportID = reportID;
        this.reporterName = reporterName;
        this.location = location;
        this.condition = condition;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
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
     * a getter for the Enum condition of the water
     *
     * @return the water condition
     */
    public OverallCondition getCondition() {
        return condition;
    }

    /**
     * a getter for the virus PPM of the water
     *
     * @return the virus ppm
     */
    public int getVirusPPM() {
        return virusPPM;
    }

    /**
     * a getter for the contaminant PPM of the water
     *
     * @return the contaminant ppm
     */
    public int getContaminantPPM() {
        return contaminantPPM;
    }

    @Override
    public String toString() {
        String out = "";

        out += date.toString() + "\n";
        out += reportID + "\n";
        out += reporterName + "\n";
        out += location.toString() + "\n";
        out += condition + "\n";
        out += virusPPM + "\n";
        out += contaminantPPM + "\n";

        return out;
    }
}
