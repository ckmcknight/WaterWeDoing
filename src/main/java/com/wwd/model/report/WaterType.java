package com.wwd.model.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Enum of available Water Types
 */
public enum WaterType {
    BOTTLED,
    WELL,
    STREAM,
    LAKE,
    SPRING,
    OTHER;


    /**
     * returns the types of water as a list
     *
     * @return ObservableList of all types of water
     */
    public static ObservableList<WaterType> valuesAsList() {
        ObservableList<WaterType> list = FXCollections.observableArrayList();
        list.addAll(WaterType.values());
        return list;
    }
}
