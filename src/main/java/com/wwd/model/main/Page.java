package com.wwd.model.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * A page consisting of an FXML view + Controller combination.
 * @param <T> the Controller type that belongs to this page
 */
public class Page<T> {
    private final FXMLLoader loader;

    private Pane viewPane;

    private T controller;

    /**
     * Create a page based on the resource URL.
     * @param location the resource location
     * @throws IOException attempting to load the resource fails
     */
    public Page(URL location) throws IOException {
        loader = new FXMLLoader();
        loader.setLocation(location);
        viewPane = loader.load();
        controller = loader.getController();
    }

    /**
     * Get the FXMLLoader that provided the view pane and controller.
     * @return the FXMLLoader for this resource
     */
    @SuppressWarnings("unused")
    public FXMLLoader getLoader() {
        return loader;
    }

    /**
     * Get the view pane for this resource.
     * @return the Pane for this resource
     */
    public Pane getViewPane() {
        return viewPane;
    }

    /**
     * Get the controller for this resource.
     * @return the controller of type T for this resource
     */
    public T getController() {
        return controller;
    }
}
