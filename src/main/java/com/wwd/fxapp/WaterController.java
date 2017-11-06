package com.wwd.fxapp;

import com.wwd.model.login.LoginCredentials;
import com.wwd.model.login.LoginType;
import com.wwd.model.report.Location;
import com.wwd.model.report.OverallCondition;
import com.wwd.model.report.PurityReport;
import com.wwd.model.report.WaterCondition;
import com.wwd.model.report.WaterManager;
import com.wwd.model.report.WaterReport;
import com.wwd.model.report.WaterSpot;
import com.wwd.model.report.WaterType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WaterController {
    @Autowired
    private WaterManager waterManager;

    @CrossOrigin
    @RequestMapping("/wwd/water/addPurity")
    public void addPurityReport(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");
        JSONObject locObj = bodyObj.getJSONObject("Location");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.getString("userName"),
                    Long.parseLong(loginObj.get("password").toString())
            );
            Location loc = new Location(
                    Float.parseFloat(locObj.get("latitude").toString()),
                    Float.parseFloat(locObj.get("longitude").toString())
            );
            waterManager.addPurityReport(
                    loginCredentials,
                    loc,
                    OverallCondition.valueOf(bodyObj.getString("condition")
                            .toUpperCase()),
                    bodyObj.getInt("virusPPM"),
                    bodyObj.getInt("contaminantPPM")
            );
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping("/wwd/water/addSource")
    public void addSourceReport(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");
        JSONObject locObj = bodyObj.getJSONObject("Location");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.getString("userName"),
                    Long.parseLong(loginObj.get("password").toString())
            );
            Location loc = new Location(
                    Float.parseFloat(locObj.get("latitude").toString()),
                    Float.parseFloat(locObj.get("longitude").toString())
            );
            waterManager.addSourceReport(
                    loginCredentials,
                    loc,
                    WaterCondition.valueOf(bodyObj.getString("condition")
                            .toUpperCase()),
                    WaterType.valueOf(bodyObj.getString("type").toUpperCase())
            );
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping("/wwd/water/allWaterSpots")
    public WaterSpotModel[] getWaterSpots(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.getString("userName"),
                    Long.parseLong(loginObj.get("password").toString())
            );

            ArrayList<WaterSpot> waterSpotArrayList
                    = waterManager.getWaterSpots(loginCredentials);
            WaterSpotModel[] waterSpotModels
                    = new WaterSpotModel[waterSpotArrayList.size()];
            for (int i = 0; i < waterSpotModels.length; i += 1) {
                waterSpotModels[i]
                        = new WaterSpotModel(waterSpotArrayList.get(i));
            }

            return waterSpotModels;
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return new WaterSpotModel[0];
    }

    private class WaterSpotModel implements Serializable {
        public Location location;
        public WaterReportModel[] waterReports;
        public PurityReportModel[] purityReports;

        public WaterSpotModel(WaterSpot spot) {
            location = spot.getLocation();

            List<PurityReport> purityReportList = spot.getPurityReportList();
            purityReports = new PurityReportModel[purityReportList.size()];
            for (int j = 0; j < purityReports.length; j += 1) {
                purityReports[j]
                        = new PurityReportModel(purityReportList.get(j));
            }

            List<WaterReport> waterReportList = spot.getWaterReportList();
            waterReports = new WaterReportModel[waterReportList.size()];
            for (int j = 0; j < waterReports.length; j += 1) {
                waterReports[j] = new WaterReportModel(waterReportList.get(j));
            }
        }
    }

    private class PurityReportModel implements Serializable {
        public String date;
        public int reportID;
        public String reporterName;
        public Location location;
        public OverallCondition condition;
        public int virusPPM;
        public int contaminantPPM;

        public PurityReportModel(PurityReport source) {
            date = source.getDate()
                    .format(DateTimeFormatter.RFC_1123_DATE_TIME);
            reportID = source.getID();
            reporterName = source.getReporterName();
            location = source.getLocation();
            condition = source.getCondition();
            virusPPM = source.getVirusPPM();
            contaminantPPM = source.getContaminantPPM();
        }
    }

    private class WaterReportModel implements Serializable {
        public String date;
        public int reportID;
        public String reporterName;
        public Location location;
        public WaterType type;
        public WaterCondition condition;

        public WaterReportModel(WaterReport source) {
            date = source.getDate()
                    .format(DateTimeFormatter.RFC_1123_DATE_TIME);
            reportID = source.getID();
            reporterName = source.getReporterName();
            location = source.getLocation();
            condition = source.getCondition();
            type = source.getType();
        }
    }
}
