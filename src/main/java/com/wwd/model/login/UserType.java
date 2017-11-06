package com.wwd.model.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Enum that contains the types of users
 *
 */
public enum UserType {
    USER,
    WORKER,
    MANAGER,
    ADMIN;

    /**
     * Returns a list of values of the Users.
     * @return a list of values of the Users
     */
    public static ObservableList<UserType> valuesAsList() {
        ObservableList<UserType> list = FXCollections.observableArrayList();
        list.addAll(UserType.values());
        return list;
    }
}