package com.wwd.model.report;

import java.time.ZonedDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the calculateMedianVirusPPM method in WaterSpot {@link WaterSpot}
 * @author Noah
 */
public class MedianVirusPPMTester {

    /**
     * Test to ensure a report without a virus PPM will
     * correctly return ---
     */
    @Test
    public void testNullReport() {
        // Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        String med = water.calculateMedianVirusPPM();

        // Test
        assertEquals(med, "---");
    }

    /**
     * Test to make sure median is correct
     * if there is only one value there
     */
    @Test
    public void testSingleNumber() {
        // Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        ZonedDateTime testTime = ZonedDateTime.now();
        PurityReport testReport = new PurityReport(testTime, 1, "Noah", loc1, OverallCondition.SAFE, 5, 0);
        water.addPurityReport(testReport);
        String med = water.calculateMedianVirusPPM();

        // Test
        assertEquals(med, "5");
    }

    /**
     * Tests to make sure method doesn't
     * return null somehow
     */
    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void testNotNull() {
        // Setup
        Location loc1 = new Location(5, 5);
        WaterSpot water = new WaterSpot(loc1);
        ZonedDateTime testTime = ZonedDateTime.now();
        PurityReport testReport = new PurityReport(testTime, 1, "Noah", loc1, OverallCondition.SAFE, 10, 0);
        water.addPurityReport(testReport);
        String med = water.calculateMedianVirusPPM();

        // Test
        assertFalse(med.equals(null));
    }

    /**
     * Test to ensure the median is correctly
     * calculated for two virus PPM that are
     * evenly spaced, like 3 and 5
     */
    @Test
    public void testEvenlySpacedNumbers() {
        // Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        ZonedDateTime testTime = ZonedDateTime.now();
        PurityReport testReport1 = new PurityReport(testTime, 1, "Noah", loc1, OverallCondition.SAFE, 5, 0);
        PurityReport testReport2 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.TREATABLE, 7, 0);
        water.addPurityReport(testReport1);
        water.addPurityReport(testReport2);
        String med = water.calculateMedianVirusPPM();

        // Test
        assertEquals(med, "6");
    }

    /**
     * Test to ensure the median is correctly
     * calculated for two virus PPM that are
     * oddly spaced, like 3 and 6
     */
    @Test
    public void testOddlySpacedNumbers() {
        // Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        ZonedDateTime testTime = ZonedDateTime.now();
        PurityReport testReport1 = new PurityReport(testTime, 1, "Noah", loc1, OverallCondition.SAFE, 4, 0);
        PurityReport testReport2 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.TREATABLE, 9, 0);
        water.addPurityReport(testReport1);
        water.addPurityReport(testReport2);
        String med = water.calculateMedianVirusPPM();

        // Test
        assertEquals(med, "6");
    }

    /**
     * Test to ensure that the existence of many
     * Virus PPM values will still result in a
     * correct calculation of the median
     */
    @Test
    public void testMultiplePPMs() {
        // Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        ZonedDateTime testTime = ZonedDateTime.now();

        // Create test purity reports
        PurityReport testReport1 = new PurityReport(testTime, 1, "Noah", loc1, OverallCondition.SAFE, 4, 0);
        PurityReport testReport2 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 9, 0);
        PurityReport testReport3 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 75, 0);
        PurityReport testReport4 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 1, 0);
        PurityReport testReport5 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 15, 0);
        PurityReport testReport6 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 2, 0);
        PurityReport testReport7 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 22, 0);
        PurityReport testReport8 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 0, 0);
        PurityReport testReport9 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 2000, 0);
        PurityReport testReport10 = new PurityReport(testTime, 2, "Noah", loc1, OverallCondition.SAFE, 523, 0);

        // Add test purity reports to WaterSpot
        water.addPurityReport(testReport1);
        water.addPurityReport(testReport2);
        water.addPurityReport(testReport3);
        water.addPurityReport(testReport4);
        water.addPurityReport(testReport5);
        water.addPurityReport(testReport6);
        water.addPurityReport(testReport7);
        water.addPurityReport(testReport8);
        water.addPurityReport(testReport9);
        water.addPurityReport(testReport10);

        String med = water.calculateMedianVirusPPM();

        // Test
        assertEquals(med, "12");
    }
}
