package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.report.WaterReport;
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
public class ManageReportsController implements MainDependent, Unaware {

    /**The main application dependency**/
    private MainFXApplication mainApp;

    private static final Logger LOGGER =
            Logger.getLogger("ManageReportsController");

    private List<WaterReport> waterReports;
    private int maxSheets = 1;
    private int currentSheet = 1;

    private Label[][] reportLabels;

    private Comparator<WaterReport> comparator
            = Comparator.comparing(WaterReport::getDate).reversed();

    @FXML
    GridPane sheetPane;

    @FXML
    TextField sheetNumberField;

    @FXML
    Label maxSheetLabel;

    @FXML
    private void initialize() {
        initLabels();
        sheetNumberField.setOnKeyPressed((ev) -> {
                if (ev.getCode() == KeyCode.ENTER) {
                    try {
                        int newSheet
                                = Integer.parseInt(sheetNumberField.getText());
                        setCurrentSheet(newSheet);
                    } catch (NumberFormatException e) {
                        // Bad format
                        setCurrentSheet(currentSheet);
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
        setCurrentSheet(currentSheet - 1);
    }

    @FXML
    private void handleNext() {
        setCurrentSheet(currentSheet + 1);
    }

    private void reload() {
        waterReports = new ArrayList<>();
        try {
            List<WaterSpot> waterSpots = mainApp.getWaterManager()
                    .getWaterSpots(mainApp.getLoginCredentials());
            for (WaterSpot w : waterSpots) {
                waterReports.addAll(w.getWaterReportList());
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Could not transact with server!\n"
                    + e.getMessage());
        }
        waterReports.sort(comparator);

        maxSheets = waterReports.size() / 10;
        if (waterReports.size() % 10 != 0) {
            maxSheets += 1;
        }
        if (maxSheets <= 0) {
            maxSheets = 1;
        }
        maxSheetLabel.setText(" of " + maxSheets);
        setCurrentSheet(1, true);
    }

    private void setCurrentSheet(int newSheet) {
        setCurrentSheet(newSheet, false);
    }

    private void setCurrentSheet(int newSheet, boolean forceReload) {
        if (newSheet < 1) {
            newSheet = 1;
        } else if (newSheet > maxSheets) {
            newSheet = maxSheets;
        }

        if (newSheet == currentSheet) {
            sheetNumberField.setText(currentSheet + "");
            if (forceReload) {
                loadReport(currentSheet);
            }
        } else {
            currentSheet = newSheet;
            sheetNumberField.setText(currentSheet + "");
            loadReport(currentSheet);
        }
    }

    private void loadReport(int sheetNumber) {
        int start = ((sheetNumber - 1) * 10);

        for (int i = 0; i < 10; i++) {
            // Build the values to display for this row
            String[] values = new String[7];

            if (start + i < waterReports.size()) {
                WaterReport thisReport = waterReports.get(start + i);
                values[0] = thisReport.getID() + "";
                values[1] = thisReport.getDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE);
                values[2] = thisReport.getLocation().getLatitude() + "";
                values[3] = thisReport.getLocation().getLongitude() + "";
                values[4] = thisReport.getType().toString();
                values[5] = thisReport.getCondition().toString();
                values[6] = thisReport.getReporterName();
            }

            for (int n = 0; n < values.length; n++) {
                reportLabels[i][n].setText(values[n]);
            }
        }
    }

    private void initLabels() {
        reportLabels = new Label[10][7];
        for (int row = 0; row < reportLabels.length; row++) {
            for (int col = 0; col < reportLabels[row].length; col++) {
                Label newLabel = new Label("");
                newLabel.setMaxWidth(Double.MAX_VALUE);
                newLabel.setMaxHeight(Double.MAX_VALUE);
                newLabel.setAlignment(Pos.CENTER);
                newLabel.setTextAlignment(TextAlignment.CENTER);
                reportLabels[row][col] = newLabel;
                sheetPane.add(newLabel, col, row + 1);
            }
        }
    }
}
