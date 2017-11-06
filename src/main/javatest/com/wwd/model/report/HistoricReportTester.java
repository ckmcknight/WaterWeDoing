package com.wwd.model.report;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Created by Josh on 11/9/2016.
 * Tests the calculateAverage function of the HistoricReport class.
 */
public class HistoricReportTester {

    /**
     * Tests the average of a null list.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void calculateAverageNullTest() {
        // Setup
        ArrayList<Integer> values = null;

        float expected = 0.0f;
        float result = HistoricReport.calculateAverage(values);

        assertEquals(expected, result, 0.0);
    }

    /**
     * Tests the average of an empty list.
     */
    @Test
    public void calculateAverageEmptyTest() {
        // Setup
        ArrayList<Integer> values = new ArrayList<>();

        float expected = 0.0f;
        float result = HistoricReport.calculateAverage(values);

        assertEquals(expected, result, 0.0);
    }

    /**
     * Tests the average of a single positive value.
     */
    @Test
    public void calculateAverageSinglePositiveTest() {
        // Setup
        ArrayList<Integer> values = new ArrayList<>();
        values.add(110);

        float expected = 110.0f;
        float result = HistoricReport.calculateAverage(values);

        assertEquals(expected, result, 0.0);
    }

    /**
     * Tests the average of a single negative value.
     */
    @Test
    public void calculateAverageSingleNegativeTest() {
        // Setup
        ArrayList<Integer> values = new ArrayList<>();
        values.add(-10);

        float expected = -10.0f;
        float result = HistoricReport.calculateAverage(values);

        assertEquals(expected, result, 0.0);
    }

    // Tests the average of multiple values, negative and positive.
    @Test
    public void calculateAverageMultiTest() {
        // Setup
        ArrayList<Integer> values = new ArrayList<>();
        values.add(-10);
        values.add(100);
        values.add(-55);
        values.add(65);

        float expected = 25.0f;
        float result = HistoricReport.calculateAverage(values);

        assertEquals(expected, result, 0.0);
    }
}
