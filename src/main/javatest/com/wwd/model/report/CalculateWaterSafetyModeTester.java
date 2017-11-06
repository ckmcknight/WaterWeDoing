package com.wwd.model.report;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.*;

/**
 * Tests calculateWaterSafetyMode method in waterSpot {@link WaterSpot}
 * @author Michael
 */
public class CalculateWaterSafetyModeTester {

    /**
     * Test to ensure that when all null values are entered --- will be returned
     */
    @Test
    public void allNullValues() {
        // Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        String answer = water.calculateWaterSafetyMode();

        // Test
        assertEquals("---", answer);
    }

    /**
     * Test to ensure that when two water conditions tie for the max number
     * of conditions in the WaterSpot that only one is selected.
     */
    @Test
    public void inCaseOfTie() {

        //Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        ZonedDateTime now = ZonedDateTime.now();

        PurityReport rep1 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.SAFE, 0,0);
        PurityReport rep2 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.SAFE, 0,0);
        PurityReport rep3 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.SAFE, 0,0);
        PurityReport rep4 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.TREATABLE, 0,0);
        PurityReport rep5 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.TREATABLE, 0,0);
        PurityReport rep6 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.TREATABLE, 0,0);
        PurityReport rep7 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.UNSAFE, 0,0);

        water.addPurityReport(rep1);
        water.addPurityReport(rep2);
        water.addPurityReport(rep3);
        water.addPurityReport(rep4);
        water.addPurityReport(rep5);
        water.addPurityReport(rep6);
        water.addPurityReport(rep7);

        String answer = water.calculateWaterSafetyMode();

        //Test
        assertTrue(answer.equals("SAFE") || answer.equals("TREATABLE"));
    }

    /**
     * Test to ensure that the most occurring condition is successfully returned
     */
    @Test
    public void mostOccurringReturned() {

        //Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        ZonedDateTime now = ZonedDateTime.now();

        PurityReport rep1 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.SAFE, 0,0);
        PurityReport rep2 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.SAFE, 0,0);
        PurityReport rep3 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.UNSAFE, 0,0);
        PurityReport rep4 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.TREATABLE, 0,0);
        PurityReport rep5 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.TREATABLE, 0,0);
        PurityReport rep6 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.UNSAFE, 0,0);
        PurityReport rep7 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.TREATABLE, 0,0);

        water.addPurityReport(rep1);
        water.addPurityReport(rep2);
        water.addPurityReport(rep3);
        water.addPurityReport(rep4);
        water.addPurityReport(rep5);
        water.addPurityReport(rep6);
        water.addPurityReport(rep7);

        String answer = water.calculateWaterSafetyMode();

        //Test
        assertEquals("TREATABLE", answer);
    }

    /**
     * Test to ensure that the correct entry is return when only one value is
     * given
     */
    @Test
    public void singleValue() {

        // Setup
        Location loc1 = new Location(2, 2);
        WaterSpot water = new WaterSpot(loc1);
        ZonedDateTime now = ZonedDateTime.now();
        PurityReport rep1 = new PurityReport(now, 0, "Mike", loc1,
                OverallCondition.SAFE, 0,0);
        water.addPurityReport(rep1);
        String answer = water.calculateWaterSafetyMode();

        // Test
        assertEquals("SAFE", answer);
    }



}
