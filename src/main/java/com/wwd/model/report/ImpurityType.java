package com.wwd.model.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Enum of available Impurity Types
 */
public enum ImpurityType {
    VIRUS,
    CONTAMINANT;


    /**
     * returns the types of impurities as a list
     *
     * @return ObservableList of all types of water
     */
    public static ObservableList<ImpurityType> valuesAsList() {
        ObservableList<ImpurityType> list = FXCollections.observableArrayList();
        list.addAll(ImpurityType.values());
        return list;
    }
}
