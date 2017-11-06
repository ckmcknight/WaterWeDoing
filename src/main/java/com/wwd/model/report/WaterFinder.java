package com.wwd.model.report;
import com.wwd.model.login.LoginCredentials;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Class to handle location and water spot relationships
 */
public class WaterFinder implements WaterManager {

    private final Map<Location, WaterSpot> waterSpotMap;
    private int nextReportId = 1;

    /**
     * Attaches water reports to a location
     */
    public WaterFinder() {
        waterSpotMap = new HashMap<>();
    }

    /**
     * Adds a report to the appropriate water spot.
     * If the water spot does not exist it creates the spot.
     *
     * @param credentials the credentials of the reporting user
     * @param location The location of the report
     * @param condition The condition of the water
     * @param type The type of water present at the location
     */
    public void addSourceReport(LoginCredentials credentials, Location location,
                                WaterCondition condition, WaterType type) {
        ZonedDateTime date = ZonedDateTime.now();
        WaterReport report = new WaterReport(date, nextReportId,
                credentials.getUid(), location, type, condition);
        nextReportId++;
        if (!waterSpotMap.containsKey(location)) {
            WaterSpot newSpot = new WaterSpot(location);
            waterSpotMap.put(location, newSpot);
        }
        WaterSpot spot = waterSpotMap.get(location);
        spot.addWaterReport(report);
    }

    /**
     * Adds a purity report to the appropriate water spot.
     * If the water spot does not exist it creates the spot.
     *
     * @param credentials the credentials of the reporting user
     * @param location The location of the report
     * @param condition The condition of the water
     * @param virusPPM a quantity for viruses
     * @param contaminantPPM a quantity for contaminants
     */
    public void addPurityReport(LoginCredentials credentials, Location
                                location, OverallCondition condition, int
                                virusPPM, int contaminantPPM) {
        ZonedDateTime date = ZonedDateTime.now();
        PurityReport report = new PurityReport(date, nextReportId,
                credentials.getUid(), location, condition, virusPPM,
                contaminantPPM);
        nextReportId++;
        if (!waterSpotMap.containsKey(location)) {
            WaterSpot newSpot = new WaterSpot(location);
            waterSpotMap.put(location, newSpot);
        }
        WaterSpot spot = waterSpotMap.get(location);
        spot.addPurityReport(report);
    }

    /**
     * gets the water spot at the location
     *
     * @param credentials the location where a water spot
     *                 is expected to be retrieved
     * @return the water spot at the location, returns null if none exists.
     */
    public ArrayList<WaterSpot> getWaterSpots(LoginCredentials credentials) {
        return new ArrayList<>(waterSpotMap.values());
    }
}
