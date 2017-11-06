package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.report.Location;
import com.wwd.model.report.WaterCondition;
import com.wwd.model.report.WaterManager;
import com.wwd.model.report.WaterType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * Controller for the Water Report Page
 */
public class WaterReportController implements MainDependent {

    /**The main application dependency**/
    private MainFXApplication mainApp;

    private static final Logger LOGGER =
            Logger.getLogger("WaterReportController");

    @FXML
    private TextField latitudeTextField;

    @FXML
    private TextField longitudeTextField;

    @FXML
    private ComboBox<WaterType> waterTypeCombo;

    @FXML
    private ComboBox<WaterCondition> waterConditionCombo;

    @FXML
    private Label submitErrorText;

    /**
     * Sets up the main application
     * @param mainApp   the application to be set up
     * */
    @Override
    public void setMainApp(MainFXApplication mainApp) {
        //boolean isFirst = (this.mainApp == null);
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        waterConditionCombo.setItems(WaterCondition.valuesAsList());
        waterTypeCombo.setItems(WaterType.valuesAsList());
        submitErrorText.setOpacity(0);
    }

    @FXML
    private void handleClearPressed() {
        latitudeTextField.clear();
        longitudeTextField.clear();
        waterTypeCombo.getSelectionModel().clearSelection();
        waterConditionCombo.getSelectionModel().clearSelection();

    }

    @FXML
    private void handleSubmitReportPressed() {
        try {
            float latitude = Float.parseFloat(latitudeTextField.getText());
            float longitude = Float.parseFloat((longitudeTextField.getText()));
            Location location = new Location(latitude, longitude);
            /*the WaterManager for the class*/
            WaterManager waterManager = mainApp.getWaterManager();
            if (waterConditionCombo.getSelectionModel().isEmpty()
                    || waterTypeCombo.getSelectionModel().isEmpty()) {
                submitErrorText.setText("Condition and Type Must Be Set!");
                submitErrorText.setOpacity(1);
            } else {
                waterManager.addSourceReport(mainApp.getLoginCredentials(),
                        location, waterConditionCombo.getValue(),
                        waterTypeCombo.getValue());
                submitErrorText.setOpacity(0);
                handleClearPressed();
            }
        } catch (NumberFormatException e) {
            submitErrorText.setText("Latitude and Longitude Must Be Numbers!");
            submitErrorText.setOpacity(1);
        } catch (RemoteException e) {
            submitErrorText.setText("Could not connect to the server!");
            submitErrorText.setOpacity(1);
        }
    }
}
