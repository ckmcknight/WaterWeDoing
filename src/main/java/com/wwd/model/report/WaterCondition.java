package com.wwd.model.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Enum of available Water Conditions
 */
public enum WaterCondition {
    WASTE,
    TREATABLE_MUDDY,
    TREATABLE_CLEAR,
    POTABLE;


    /**
     * returns the conditions of water as a list
     *
     * @return ObservableList of all conditions of water
     */
    public static ObservableList<WaterCondition> valuesAsList() {
        ObservableList<WaterCondition> list
                = FXCollections.observableArrayList();
        list.addAll(WaterCondition.values());
        return list;
    }
}
