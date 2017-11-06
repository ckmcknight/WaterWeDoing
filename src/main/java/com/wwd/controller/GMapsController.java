package com.wwd.controller;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.report.WaterSpot;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import netscape.javascript.JSObject;

/**
 * The controller for the map view window
 * and handles putting water report locations onto the map
 *
 */
public class GMapsController implements Initializable,
        MapComponentInitializedListener, MainDependent, Unaware {

    @FXML
    private GoogleMapView mapView;

    @FXML
    private TextField latField;

    @FXML
    private TextField longField;

    @FXML
    private Label errorLabel;

    private GoogleMap map;

    private boolean gmapsReady = false;

    // Default is Tech Tower, Atlanta, GA
    private LatLong location;

    /** reference back to mainApplication if needed */
    private MainFXApplication mainApplication;

    private static final Logger LOGGER =
            Logger.getLogger("GMapsController");

    /**
     * Method for notifying view that it is active
     *
     */
    public void receiveFocus() {
        initMap();
    }

    /**
     * searches for the text field entries in latitude and longitude.
     * converts character sequence to double.
     *
     */
    public void searchPressed() {
        errorLabel.setVisible(false);
        String latitude = latField.getCharacters().toString();
        String longitude = longField.getCharacters().toString();
        try {
            Double latEntry = Double.parseDouble(latitude);
            Double longEntry = Double.parseDouble(longitude);
            if (-90 >  latEntry.intValue() || latEntry.intValue() > 90) {
                throw new NumberFormatException();
            } else if (-180 >  longEntry.intValue()
                    || longEntry.intValue() > 180) {
                throw new NumberFormatException();
            }
            setCurrentLocation(new LatLong(latEntry, longEntry));
            mapView.setCenter(latEntry, longEntry);
        } catch (NumberFormatException e) {
            errorLabel.setVisible(true);
        }
    }

    /**
     * pressing enter from longField calls searchPressed()
     * @param event any key press in the longField.
     */
    public void handleKeyPressed(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchPressed();
        }
    }

    /**
     * allow for calling back to the main application code if necessary
     * @param main   the reference to the FX Application instance
     * */
    public void setMainApp(MainFXApplication main) {
        mainApplication = main;
    }

    private void setCurrentLocation(LatLong currentLocation) {
        location = currentLocation;
    }


    /**
     * Initializes the map
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
    }

    /**
     * Gets all the reports and put their locations on the map
     */
    @Override
    public void mapInitialized() {
        if (!gmapsReady) {
            initMap();
        }
        gmapsReady = true;
    }

    private void initMap() {
        setCurrentLocation(new LatLong(34.2, -84.8));
        List<WaterSpot> allSpots = new ArrayList<>();

        try {
            allSpots = mainApplication.getWaterManager()
                    .getWaterSpots(mainApplication.getLoginCredentials());
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Could not transact with server!");
        }
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(location)
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(9);

        if (allSpots == null) {
            map = mapView.createMap(mapOptions);
        } else {
            map = mapView.createMap(mapOptions);
            for (WaterSpot i : allSpots) {
                MarkerOptions markerOption = new MarkerOptions();
                LatLong newLocation = new LatLong(i.getLocation().getLatitude(),
                        i.getLocation().getLongitude());
                markerOption.position(newLocation)
                        .visible(Boolean.TRUE);
                Marker marker = new Marker(markerOption);
                String waterCondition = i.calculateWaterConditionMode();
                String waterType = i.calculateWaterTypeMode();
                String waterSafety = i.calculateWaterSafetyMode();
                String virusPPM = i.calculateMedianVirusPPM();
                String contaminantPPM = i.calculateMedianContaminantPPM();
                map.addUIEventHandler(marker, UIEventType.click,
                        (JSObject obj) -> {
                        InfoWindowOptions infoWindowOptions
                                = new InfoWindowOptions();
                        infoWindowOptions.content("Condition: "
                                + waterCondition + "<br>" + "Water Type: "
                                + waterType + "<br>" + "Safety: "
                                + waterSafety + "<br>" + "Median Virus PPM: "
                                + virusPPM + "<br>" + "Median Contaminant PPM: "
                                + contaminantPPM);
                        InfoWindow window = new InfoWindow(infoWindowOptions);
                        window.open(map, marker);
                    });
                map.addMarker(marker);
            }
        }
    }
}
