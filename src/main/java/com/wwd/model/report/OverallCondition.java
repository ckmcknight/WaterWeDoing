package com.wwd.model.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Enum of available Water Conditions
 */
public enum OverallCondition {
    SAFE,
    TREATABLE,
    UNSAFE;


    /**
     * returns the conditions of water as a list
     *
     * @return ObservableList of all conditions of water
     */
    public static ObservableList<OverallCondition> valuesAsList() {
        ObservableList<OverallCondition> list
                = FXCollections.observableArrayList();
        list.addAll(OverallCondition.values());
        return list;
    }
}
