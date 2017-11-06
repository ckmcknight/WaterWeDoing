package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.login.UnauthorizedOperationException;
import com.wwd.model.report.Location;
import com.wwd.model.report.OverallCondition;
import com.wwd.model.report.WaterManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * Controller for the Submit Purity Report Page
 */
public class PurityReportController implements MainDependent {

    /**The main application dependency**/
    private MainFXApplication mainApp;

    private static final Logger LOGGER =
            Logger.getLogger("PurityReportController");

    @FXML
    private TextField latitudeTextField;

    @FXML
    private TextField longitudeTextField;

    @FXML
    private ComboBox<OverallCondition> overallConditionCombo;

    @FXML
    private TextField virusPPMTextField;

    @FXML
    private TextField contaminantPPMTextField;

    @FXML
    private Label submitErrorText;

    /**
     * Sets up the main application
     * @param mainApp   the application to be set up
     * */
    @Override
    public void setMainApp(MainFXApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        overallConditionCombo.setItems(OverallCondition.valuesAsList());
        submitErrorText.setOpacity(0);
    }

    @FXML
    private void handleClearPressed() {
        latitudeTextField.clear();
        longitudeTextField.clear();
        overallConditionCombo.getSelectionModel().clearSelection();
        virusPPMTextField.clear();
        contaminantPPMTextField.clear();
    }

    @FXML
    private void handleSubmitReportPressed() {
        try {
            float latitude = Float.parseFloat(latitudeTextField.getText());
            float longitude = Float.parseFloat(longitudeTextField.getText());
            int virusPPM = Integer.parseInt(virusPPMTextField.getText());
            int contaminantPPM = Integer.parseInt(contaminantPPMTextField
                    .getText());
            Location location = new Location(latitude, longitude);
            /*the WaterManager for the class*/
            WaterManager waterManager = mainApp.getWaterManager();
            if (overallConditionCombo.getSelectionModel().isEmpty()) {
                submitErrorText.setText("Overall Condition Must Be Set!");
                submitErrorText.setOpacity(1);
            } else if (virusPPM < 0 || contaminantPPM < 0) {
                submitErrorText.setText("PPM  Values Must Be Non-negative!");
                submitErrorText.setOpacity(1);
            } else {
                waterManager.addPurityReport(mainApp.getLoginCredentials(),
                        location, overallConditionCombo.getValue(), virusPPM,
                        contaminantPPM);
                submitErrorText.setOpacity(0);
                handleClearPressed();
            }
        } catch (RemoteException e) {
            submitErrorText.setText("Could not connect to remote server!");
            submitErrorText.setOpacity(1);
        } catch (NumberFormatException e) {
            submitErrorText.setText("All Text Fields Must Be Numbers!");
            submitErrorText.setOpacity(1);
        } catch (UnauthorizedOperationException e) {
            System.out.println("Unauthorized access:\n" + e.getMessage());
        } catch (Exception e) {
            submitErrorText.setText("Unknown error has occurred. See log.");
            submitErrorText.setOpacity(1);
        }
    }
}
