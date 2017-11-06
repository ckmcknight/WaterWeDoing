package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.report.PurityReport;
import com.wwd.model.report.WaterSpot;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller for the profile viewer.
 */
public class ManagePurityReportsController implements MainDependent, Unaware {

    /**The main application dependency**/
    private MainFXApplication mainApp;

    private static final Logger LOGGER =
            Logger.getLogger("ManagePurityReportsController");

    private List<PurityReport> purityReports;
    private int maxSheets = 1;
    private int currentPuritySheet = 1;

    private Label[][] reportPurityLabels;

    private Comparator<PurityReport> comparator
            = Comparator.comparing(PurityReport::getDate).reversed();

    @FXML
    GridPane sheetPane;

    @FXML
    TextField sheetNumberField;

    @FXML
    Label maxSheetLabel;

    @SuppressWarnings("unused")
    @FXML
    private void initialize() {
        initLabels();
        sheetNumberField.setOnKeyPressed((ev) -> {
                if (KeyCode.ENTER == ev.getCode()) {
                    try {
                        int newPuritySheet
                            = Integer.parseInt(sheetNumberField.getText());
                        setCurrentSheet(newPuritySheet);
                    } catch (NumberFormatException e) {
                        // Bad format
                        setCurrentSheet(currentPuritySheet);
                    }
                }
            });
    }

    /**
     * Sets the main application
     * @param mainApp   the application to be set up
     * */
    @Override
    public void setMainApp(MainFXApplication mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void receiveFocus() {
        reload();
    }

    @FXML
    private void handlePrev() {
        setCurrentSheet(currentPuritySheet - 1);
    }

    @FXML
    private void handleNext() {
        setCurrentSheet(currentPuritySheet + 1);
    }

    private void reload() {
        purityReports = new ArrayList<>();

        try {
            List<WaterSpot> waterSpots = mainApp.getWaterManager()
                    .getWaterSpots(mainApp.getLoginCredentials());
            for (WaterSpot w : waterSpots) {
                purityReports.addAll(w.getPurityReportList());
            }
            purityReports.sort(comparator);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Could not transact with server!");
        }

        maxSheets = purityReports.size() / 10;
        if (purityReports.size() % 10 != 0) {
            maxSheets += 1;
        }
        if (maxSheets <= 0) {
            maxSheets = 1;
        }
        maxSheetLabel.setText(" of " + maxSheets);
        setPurityCurrentSheet(1, true);
    }

    private void setCurrentSheet(int newSheet) {
        setPurityCurrentSheet(newSheet, false);
    }

    private void setPurityCurrentSheet(int newSheet, boolean forceReload) {
        if (newSheet < 1) {
            newSheet = 1;
        } else if (newSheet > maxSheets) {
            newSheet = maxSheets;
        }

        if (newSheet == currentPuritySheet) {
            sheetNumberField.setText(currentPuritySheet + "");
            if (forceReload) {
                loadPurityReport(currentPuritySheet);
            }
        } else {
            currentPuritySheet = newSheet;
            sheetNumberField.setText(currentPuritySheet + "");
            loadPurityReport(currentPuritySheet);
        }
    }

    private void loadPurityReport(int sheetNumber) {
        int start = ((sheetNumber - 1) * 10);

        for (int i = 0; i < 10; i++) {
            // Build the values to display for this row
            String[] values = new String[8];

            if (start + i < purityReports.size()) {
                PurityReport thisReport = purityReports.get(start + i);
                values[0] = thisReport.getID() + "";
                values[1] = thisReport.getDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE);
                values[2] = thisReport.getLocation().getLatitude() + "";
                values[3] = thisReport.getLocation().getLongitude() + "";
                values[4] = thisReport.getCondition().toString();
                values[5] = thisReport.getReporterName();
                values[6] = String.valueOf(thisReport.getVirusPPM());
                values[7] = String.valueOf(thisReport.getContaminantPPM());
            }

            for (int n = 0; n < values.length; n++) {
                reportPurityLabels[i][n].setText(values[n]);
            }
        }
    }

    private void initLabels() {
        reportPurityLabels = new Label[10][8];
        for (int row = 0; row < reportPurityLabels.length; row++) {
            for (int col = 0; col < reportPurityLabels[row].length; col++) {
                Label newPurityLabel = new Label("");
                newPurityLabel.setMaxHeight(Double.MAX_VALUE);
                newPurityLabel.setMaxWidth(Double.MAX_VALUE);
                newPurityLabel.setAlignment(Pos.CENTER);
                newPurityLabel.setTextAlignment(TextAlignment.CENTER);
                reportPurityLabels[row][col] = newPurityLabel;
                sheetPane.add(newPurityLabel, col, row + 1);
            }
        }
    }
}