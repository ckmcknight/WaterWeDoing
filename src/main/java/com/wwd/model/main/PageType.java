package com.wwd.model.main;

/**
 * The enumeration of page types in the main view.
 */
public enum PageType {
    PROFILE_VIEW("Profile"),
    FIND_WATER("Find Water"),
    LOG_LOCATION_REPORT("Log Water Location"),
    LOG_PURITY_REPORT("Log Water Purity"),
    VIEW_HISTORICAL("Historical Report"),
    MANAGE_REPORTS("Manage Reports"),
    MANAGE_PURITY_REPORTS("Purity Reports"),
    MANAGE_USERS("Manage Users"),
    VIEW_SECURITY("Security Log");

    private final String name;

    PageType(String name) {
        this.name = name;
    }

    /**
     * Converts name to string
     * @return the name as a string
     */
    @Override
    public String toString() {
        return name;
    }
}
