package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.report.HistoricReport;
import com.wwd.model.report.ImpurityType;
import com.wwd.model.report.WaterSpot;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewHistoricalController implements MainDependent {

    private static final Logger LOGGER =
            Logger.getLogger("ViewHistoricalController");

    @FXML
    private TextField latField;

    @FXML
    private TextField longField;

    @FXML
    private TextField yearField;

    @FXML
    private ComboBox<ImpurityType> impurityType;

    @FXML
    private Label errorLabel;

    @FXML
    private LineChart<String, Number> historicalGraph;

    private MainFXApplication mainApplication;

    @FXML
    private void initialize() {
        impurityType.setItems(ImpurityType.valuesAsList());
        errorLabel.setOpacity(0);
    }

    /**
     * allow for calling back to the main application code if necessary
     * @param main   the reference to the FX Application instance
     * */
    public void setMainApp(MainFXApplication main) {
        mainApplication = main;
    }

    private void createGraph(float latEntry, float longEntry, int yearEntry,
                             ImpurityType impurity, MainFXApplication mainApp) {
        historicalGraph.getData().clear();

        try {
            List<WaterSpot> waterSpotList = mainApp.getWaterManager()
                    .getWaterSpots(mainApp.getLoginCredentials());
            XYChart.Series<String, Number> dataSeries = HistoricReport
                    .getSeries(latEntry, longEntry, yearEntry, impurity,
                            waterSpotList);

            historicalGraph.getXAxis().setLabel("Months");
            historicalGraph.getYAxis().setLabel("PPM");
            historicalGraph.setTitle("Historical Report");
            dataSeries.setName("Data Set 1");
            historicalGraph.getData().add(dataSeries);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

    /**
     * Searches for the text field entries in latitude, longitude,
     * and year.
     * Converts location sequence to double and year to int.
     */
    public void searchPressed() {
        errorLabel.setVisible(false);
        String latitude = latField.getCharacters().toString();
        String longitude = longField.getCharacters().toString();
        String year = yearField.getCharacters().toString();
        ImpurityType impurity = impurityType.getValue();
        try {
            float latEntry = Float.valueOf(latitude);
            float longEntry = Float.valueOf(longitude);
            int yearEntry = Integer.parseInt(year);
            int latEntryAsInt = (int) latEntry;
            int longEntryAsInt = (int) longEntry;
            if (-90 > latEntryAsInt || latEntryAsInt > 90) {
                throw new NumberFormatException();
            } else if (-180 >  longEntryAsInt
                    || longEntryAsInt > 180) {
                throw new NumberFormatException();
            } else if (1900 > yearEntry || yearEntry > 2100) {
                throw new NumberFormatException();
            }
            createGraph(latEntry, longEntry, yearEntry, impurity,
                    mainApplication);
        } catch (NumberFormatException e) {
            errorLabel.setVisible(true);
        }
    }

    /**
     * Pressing enter from yearField calls searchPressed()
     * @param event any key press in the yearField.
     */
    public void handleKeyPressed(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchPressed();
        }
    }
}
