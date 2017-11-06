package com.wwd.model.report;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Location {@link Location}
 * @author charlie
 */
public class LocationTester {

    /**
     * Test asserts false when null.
     */
    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void testEqualsNull() {
        // Setup
        Location realLocation = new Location(1, 1);

        //Test
        assertFalse(realLocation.equals(null));
    }


    /**
     * Test asserts false when wrong type.
     */
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void testEqualsWrongType() {
        // Setup
        Location realLocation = new Location(1, 1);
        Integer num = 1;

        // Test
        assertFalse(realLocation.equals(num));
    }

    /**
     * Test asserts false when wrong latitude.
     */
    @Test
    public void testEqualsWrongLatitude() {
        // Setup
        Location realLocation = new Location(1.1f, 1.1f);
        Location testLocation = new Location(1.1f, 0f);

        // Test
        assertFalse(realLocation.equals(testLocation));
    }

    /**
     * Test asserts false when wrong longitude.
     */
    @Test
    public void testEqualsWrongLongitude() {
        // Setup
        Location realLocation = new Location(1.1f, 1.1f);
        Location testLocation = new Location(0f, 1.1f);

        // Test
        assertFalse(realLocation.equals(testLocation));
    }

    /**
     * Test asserts true when equal.
     */
    @Test
    public void testEqualsCorrect() {
        // Setup
        Location realLocation = new Location(1.1f, 1.1f);
        Location testLocation = new Location(1.1f, 1.1f);

        // Test
        assertTrue(realLocation.equals(testLocation));
    }
}