package com.wwd.model.report;

import java.time.ZonedDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the calculateWaterTypeMode method in WaterSpot {@link WaterSpot}
 * @author Chad
 */
public class WaterTypeModeTester {

    /**
     * Test to ensure a WaterSpot with no WaterReports returns 0
     */
    @Test
    public void testNoReports() {
        // Setup
        Location location = new Location(12, 34);
        WaterSpot aSpot1 = new WaterSpot(location);

        // Test
        String mode = aSpot1.calculateWaterTypeMode();
        assertEquals(mode, "---");
    }

    /**
     * Test to ensure WaterType count is right with only one WaterReport
     */
    @Test
    public void testOneReport() {
        // Setup
        Location location1 = new Location(56, 78);
        ZonedDateTime currentTime = ZonedDateTime.now();
        WaterSpot aSpot2 = new WaterSpot(location1);
        WaterReport report = new WaterReport(currentTime, 1, "Bob", location1, WaterType.BOTTLED, WaterCondition.POTABLE);
        aSpot2.addWaterReport(report);

        // Test
        String mode1 = aSpot2.calculateWaterTypeMode();
        assertEquals(mode1, "BOTTLED");
    }

    /**
     * Test to ensure WaterType count is not null
     */
    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void testModeNotNull() {
        // Setup
        Location location4 = new Location(56, 78);
        WaterSpot aSpot5 = new WaterSpot(location4);
        ZonedDateTime currentTime = ZonedDateTime.now();
        WaterReport report = new WaterReport(currentTime, 1, "Bob", location4, WaterType.BOTTLED, WaterCondition.POTABLE);
        aSpot5.addWaterReport(report);

        // Test
        String mode4 = aSpot5.calculateWaterTypeMode();
        assertFalse(mode4.equals(null));
    }

    /**
     * Test to ensure that when there exists equal number of WaterTypes
     * the first one is outputted
     */
    @Test
    public void testEqualNumberOfTypes() {
        // Setup
        Location location2 = new Location(18, 22);
        WaterSpot aSpot3 = new WaterSpot(location2);
        ZonedDateTime currentTime = ZonedDateTime.now();
        WaterReport report1 = new WaterReport(currentTime, 1, "Bob", location2, WaterType.STREAM, WaterCondition.POTABLE);
        WaterReport report2 = new WaterReport(currentTime, 2, "Bob", location2, WaterType.BOTTLED, WaterCondition.POTABLE);
        WaterReport report3 = new WaterReport(currentTime, 3, "Bob", location2, WaterType.WELL, WaterCondition.POTABLE);
        WaterReport report4 = new WaterReport(currentTime, 4, "Bob", location2, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report5 = new WaterReport(currentTime, 5, "Bob", location2, WaterType.SPRING, WaterCondition.POTABLE);
        aSpot3.addWaterReport(report1);
        aSpot3.addWaterReport(report2);
        aSpot3.addWaterReport(report3);
        aSpot3.addWaterReport(report4);
        aSpot3.addWaterReport(report5);

        // Test
        String mode2 = aSpot3.calculateWaterTypeMode();
        assertEquals(mode2, "STREAM");
    }

    /**
     * Test to ensure that when there exists unequal number of WaterTypes
     * the correct one is outputted
     */
    @Test
    public void testLargeUnequalNumberOfTypes() {
        // Setup
        Location location3 = new Location(13, 56);
        WaterSpot aSpot4 = new WaterSpot(location3);
        ZonedDateTime currentTime = ZonedDateTime.now();
        WaterReport report1 = new WaterReport(currentTime, 1, "Bob", location3, WaterType.OTHER, WaterCondition.POTABLE);
        WaterReport report2 = new WaterReport(currentTime, 2, "Bob", location3, WaterType.BOTTLED, WaterCondition.POTABLE);
        WaterReport report3 = new WaterReport(currentTime, 3, "Bob", location3, WaterType.WELL, WaterCondition.POTABLE);
        WaterReport report4 = new WaterReport(currentTime, 4, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report5 = new WaterReport(currentTime, 5, "Bob", location3, WaterType.SPRING, WaterCondition.POTABLE);
        WaterReport report6 = new WaterReport(currentTime, 6, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report7 = new WaterReport(currentTime, 7, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report8 = new WaterReport(currentTime, 8, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report9 = new WaterReport(currentTime, 9, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report10 = new WaterReport(currentTime, 10, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report11 = new WaterReport(currentTime, 11, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report12 = new WaterReport(currentTime, 12, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report13 = new WaterReport(currentTime, 13, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);
        WaterReport report14 = new WaterReport(currentTime, 14, "Bob", location3, WaterType.LAKE, WaterCondition.POTABLE);

        aSpot4.addWaterReport(report1);
        aSpot4.addWaterReport(report2);
        aSpot4.addWaterReport(report3);
        aSpot4.addWaterReport(report4);
        aSpot4.addWaterReport(report5);
        aSpot4.addWaterReport(report6);
        aSpot4.addWaterReport(report7);
        aSpot4.addWaterReport(report8);
        aSpot4.addWaterReport(report9);
        aSpot4.addWaterReport(report10);
        aSpot4.addWaterReport(report11);
        aSpot4.addWaterReport(report12);
        aSpot4.addWaterReport(report13);
        aSpot4.addWaterReport(report14);

        // Test
        String mode3 = aSpot4.calculateWaterTypeMode();
        assertEquals(mode3, "LAKE");
    }
}
