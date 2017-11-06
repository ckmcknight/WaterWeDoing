package com.wwd.model.server;


import com.wwd.model.login.InvalidCredentialsException;
import com.wwd.model.login.LoginCredentials;
import com.wwd.model.login.UnauthorizedOperationException;
import com.wwd.model.login.User;
import com.wwd.model.login.UserType;
import com.wwd.model.report.Location;
import com.wwd.model.report.OverallCondition;
import com.wwd.model.report.PurityReport;
import com.wwd.model.report.WaterCondition;
import com.wwd.model.report.WaterManager;
import com.wwd.model.report.WaterReport;
import com.wwd.model.report.WaterSpot;
import com.wwd.model.report.WaterType;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server implementation of the WaterManager.
 */
@SuppressWarnings("TryWithIdenticalCatches")
public class ServerWaterManager implements WaterManager {

    private final Map<Location, WaterSpot> waterSpotMap;
    private final ServerLogin serverLogin;

    private final SqlQuerier querier;

    /**
     * The java logger for this class
     */
    private static final Logger LOGGER =
            Logger.getLogger("ServerWaterManager");

    private static final String TABLE_SPOTS = "water_spots";
    private static final String TABLE_SOURCE = "water_reports";
    private static final String TABLE_PURITY = "purity_reports";

    private static String buildGetPurityReportsQuery(float latitude,
                                                     float longitude) {
        return "SELECT * FROM " + TABLE_PURITY
                + " WHERE spot_latitude='" + latitude + "'"
                + " AND spot_longitude='" + longitude + "';";
    }

    private static String buildGetWaterReportsQuery(float latitude,
                                                    float longitude) {
        return "SELECT * FROM " + TABLE_SOURCE
                + " WHERE spot_latitude='" + latitude + "'"
                + " AND spot_longitude='" + longitude + "';";
    }

    private static String buildGetWaterSpotsQuery() {
        return "SELECT * FROM " + TABLE_SPOTS + ";";
    }

    private static String buildAddWaterSpotQuery(Location loc) {
        return "INSERT INTO " + TABLE_SPOTS
                + " SET latitude='" + loc.getLatitude() + "',"
                + " longitude='" + loc.getLongitude() + "';";
    }

    private static String buildAddPurityReportQuery(PurityReport report) {
        return "INSERT INTO " + TABLE_PURITY
                + " SET date='"
                + report.getDate().format(DateTimeFormatter.RFC_1123_DATE_TIME)
                + "',"
                + " reporter_name='" + report.getReporterName() + "',"
                + " spot_latitude='" + report.getLocation().getLatitude() + "',"
                + " spot_longitude='" + report.getLocation().getLongitude()
                + "'," + " water_condition='" + report.getCondition() + "',"
                + " virus_ppm=" + report.getVirusPPM() + ","
                + " contaminant_ppm=" + report.getContaminantPPM() + ";";
    }

    private static String buildAddWaterReportQuery(WaterReport report) {
        return "INSERT INTO " + TABLE_SOURCE
                + " SET date='"
                + report.getDate().format(DateTimeFormatter.RFC_1123_DATE_TIME)
                + "',"
                + " reporter_name='" + report.getReporterName() + "',"
                + " spot_latitude='" + report.getLocation().getLatitude() + "',"
                + " spot_longitude='" + report.getLocation().getLongitude()
                + "'," + " water_type='" + report.getType() + "',"
                + " water_condition='" + report.getCondition() + "';";
    }

    /**
     * Constructs a ServerWaterManager given a ServerLogin.
     * @param serverLogin the server login instance to be injected
     */
    public ServerWaterManager(ServerLogin serverLogin) {
        waterSpotMap = new HashMap<>();
        this.serverLogin = serverLogin;
        querier = new SqlQuerier();
        updateWaterSpots();
    }

    /**
     * Updates the water spots from the database.
     */
    private void updateWaterSpots() {
        waterSpotMap.clear();

        String getWaterSpotsQuery = buildGetWaterSpotsQuery();
        ResultSet resultSet = querier.query(getWaterSpotsQuery);
        try {
            while (resultSet.next()) {
                WaterSpot spot = constructWaterSpot(resultSet);
                if (spot != null) {
                    waterSpotMap.put(spot.getLocation(), spot);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Constructs a water spot given a query result set
     * @param rs the query result set
     * @return the water spot, null if any errors occur
     */
    private WaterSpot constructWaterSpot(ResultSet rs) {
        WaterSpot waterSpot = null;
        try {
            String latitude = rs.getString("latitude");
            String longitude = rs.getString("longitude");
            Location loc = new Location(
                    Float.parseFloat(latitude),
                    Float.parseFloat(longitude)
            );
            waterSpot = new WaterSpot(loc);

            String getPurityReportsQuery
                    = buildGetPurityReportsQuery(loc.getLatitude(),
                    loc.getLongitude());
            ResultSet purityRs = querier.query(getPurityReportsQuery);

            while (purityRs.next()) {
                PurityReport purityReport = constructPurityReport(purityRs);
                if (purityReport != null) {
                    waterSpot.addPurityReport(purityReport);
                }
            }

            String getWaterReportsQuery
                    = buildGetWaterReportsQuery(loc.getLatitude(),
                    loc.getLongitude());
            ResultSet sourceRs = querier.query(getWaterReportsQuery);

            while (sourceRs.next()) {
                WaterReport waterReport = constructWaterReport(sourceRs);
                if (waterReport != null) {
                    waterSpot.addWaterReport(waterReport);
                }
            }


        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return waterSpot;
    }

    /**
     * Constructs a purity report given a query result set
     * @param rs the query result set
     * @return the purity report, null if any errors occur
     */
    private PurityReport constructPurityReport(ResultSet rs) {
        PurityReport purityReport = null;

        try {
            ZonedDateTime dateTime = ZonedDateTime.parse(rs.getString("date"),
                    DateTimeFormatter.RFC_1123_DATE_TIME);
            int reportId = rs.getInt("report_id");
            String reporterName = rs.getString("reporter_name");
            String latitude = rs.getString("spot_latitude");
            String longitude = rs.getString("spot_longitude");
            String condition = rs.getString("water_condition");
            int virusPPM = rs.getInt("virus_ppm");
            int contaminantPPM = rs.getInt("contaminant_ppm");

            OverallCondition overallCondition
                    = OverallCondition.valueOf(condition);

            purityReport = new PurityReport(
                    dateTime,
                    reportId,
                    reporterName,
                    new Location(
                            Float.parseFloat(latitude),
                            Float.parseFloat(longitude)
                    ),
                    overallCondition,
                    virusPPM,
                    contaminantPPM
            );

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return purityReport;
    }

    /**
     * Constructs a water report given a query result set
     * @param rs the query result set
     * @return the water report, null if any errors occur
     */
    private WaterReport constructWaterReport(ResultSet rs) {
        WaterReport waterReport = null;

        try {
            ZonedDateTime dateTime = ZonedDateTime.parse(rs.getString("date"),
                    DateTimeFormatter.RFC_1123_DATE_TIME);
            int reportId = rs.getInt("report_id");
            String reporterName = rs.getString("reporter_name");
            String latitude = rs.getString("spot_latitude");
            String longitude = rs.getString("spot_longitude");
            String type = rs.getString("water_type");
            String condition = rs.getString("water_condition");

            WaterType waterType = WaterType.valueOf(type);
            WaterCondition waterCondition = WaterCondition.valueOf(condition);

            waterReport = new WaterReport(
                    dateTime,
                    reportId,
                    reporterName,
                    new Location(
                            Float.parseFloat(latitude),
                            Float.parseFloat(longitude)
                    ),
                    waterType,
                    waterCondition
            );

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return waterReport;
    }

    @Override
    public void addPurityReport(LoginCredentials credentials,
                                Location location,
                                OverallCondition condition,
                                int virusPPM,
                                int contaminantPPM)
            throws RemoteException,
            InvalidCredentialsException,
            UnauthorizedOperationException {

        CompletableFuture<User> userFuture = CompletableFuture
                .supplyAsync(() -> serverLogin.loginUser(credentials));
        userFuture.thenAccept((user) -> {
                if (user == null) {
                    throw new InvalidCredentialsException("Bad user "
                            + "credentials!");
                }
                UserType userType = user.getUserType();
                if (!userType.equals(UserType.WORKER) && !userType
                    .equals(UserType.MANAGER)) {
                    throw new UnauthorizedOperationException(
                        "User is unauthorized to perform this operation.");
                }

                List<String> addPurityReportQuery = new ArrayList<>();
                boolean isNewSpot = !waterSpotMap.containsKey(location);
                if (isNewSpot) {
                    addPurityReportQuery.add(buildAddWaterSpotQuery(location));
                }

                ZonedDateTime date = ZonedDateTime.now();
                PurityReport report = new PurityReport(date, -1,
                    user.getUserId(), location, condition,
                    virusPPM, contaminantPPM);

                addPurityReportQuery.add(buildAddPurityReportQuery(report));

                int[] status = querier.executeUpdate(addPurityReportQuery);
                if (status.length == 0) {
                    return;
                }
                try {
                    String getReportIdQuery = "SELECT LAST_INSERT_ID();";
                    ResultSet reportIdRs = querier.query(getReportIdQuery);
                    int reportId = -1;
                    if (reportIdRs.next()) {
                        reportId = reportIdRs.getInt(1);
                    }

                    if (reportId >= 0) {
                        report = new PurityReport(
                            report.getDate(),
                            reportId,
                            report.getReporterName(),
                            report.getLocation(),
                            report.getCondition(),
                            report.getVirusPPM(),
                            report.getContaminantPPM());

                        WaterSpot spot;
                        if (isNewSpot) {
                            spot = new WaterSpot(location);
                            waterSpotMap.put(location, spot);
                        } else {
                            spot = waterSpotMap.get(location);
                        }
                        spot.addPurityReport(report);
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
            });
    }

    @Override
    public void addSourceReport(LoginCredentials credentials,
                                Location location,
                                WaterCondition condition,
                                WaterType type)
            throws RemoteException,
            InvalidCredentialsException,
            UnauthorizedOperationException {

        CompletableFuture<User> userFuture = CompletableFuture
                .supplyAsync(() -> serverLogin.loginUser(credentials));
        userFuture.thenAccept((user) -> {
                if (user == null) {
                    throw new InvalidCredentialsException("Bad user "
                            + "credentials!");
                }

                List<String> addSourceReportQuery = new ArrayList<>();
                boolean isNewSpot = !waterSpotMap.containsKey(location);
                if (isNewSpot) {
                    addSourceReportQuery.add(buildAddWaterSpotQuery(location));
                }

                ZonedDateTime date = ZonedDateTime.now();
                WaterReport report = new WaterReport(date, -1,
                    user.getUserId(), location, type, condition);

                addSourceReportQuery.add(buildAddWaterReportQuery(report));

                int[] status = querier.executeUpdate(addSourceReportQuery);
                if (status.length == 0) {
                    return;
                }
                try {
                    String getReportIdQuery = "SELECT LAST_INSERT_ID();";
                    ResultSet reportIdRs = querier.query(getReportIdQuery);
                    int reportId = -1;
                    if (reportIdRs.next()) {
                        reportId = reportIdRs.getInt(1);
                    }

                    if (reportId >= 0) {
                        report = new WaterReport(
                            report.getDate(),
                            reportId,
                            report.getReporterName(),
                            report.getLocation(),
                            report.getType(),
                            report.getCondition()
                        );

                        WaterSpot spot;
                        if (isNewSpot) {
                            spot = new WaterSpot(location);
                            waterSpotMap.put(location, spot);
                        } else {
                            spot = waterSpotMap.get(location);
                        }
                        spot.addWaterReport(report);
                    }

                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
            });
    }

    @Override
    public ArrayList<WaterSpot> getWaterSpots(LoginCredentials credentials)
            throws RemoteException,
            InvalidCredentialsException,
            UnauthorizedOperationException {
        // Assuming every transaction has been kept up to date, use cache:
        CompletableFuture<User> userFuture = CompletableFuture
                .supplyAsync(() -> serverLogin.loginUser(credentials));
        try {
            User user = userFuture.get();
            if (user == null) {
                throw new InvalidCredentialsException("Bad user credentials!");
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        return new ArrayList<>(waterSpotMap.values());
    }
}
