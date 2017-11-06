package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;

/**
 * Interface with the property of being dependent on the MainFXApplication.
 * Should allow consumers to inject the MainFXApplication.
 */
public interface MainDependent {
    /**
     * Set the main application for this object.
     */
    void setMainApp(MainFXApplication mainApp);
}
