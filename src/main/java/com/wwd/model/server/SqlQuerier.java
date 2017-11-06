package com.wwd.model.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class SqlQuerier {
    private final Connection conn;

    /**
     * The java logger for this class
     */
    private static final Logger LOGGER =
            Logger.getLogger("SQLQuerier");

    public SqlQuerier() {
        System.out.println("Starting Sql Connection");
        this.conn = createConnection();
    }

    private Connection createConnection() {
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/water_we_doing"
                    + "?autoReconnect=true";
            Class.forName(myDriver);
            return DriverManager.getConnection(myUrl, "wwd", "bob<3");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    public ResultSet query(String query) {
        try {
            Statement st = conn.createStatement();
            return st.executeQuery(query);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    public int executeUpdate(String query) {
        try {
            Statement st = conn.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
        return -1;
    }

    public int[] executeUpdate(List<String> queryBatch) {
        try {
            Statement st = conn.createStatement();
            for (String query : queryBatch) {
                st.addBatch(query);
            }
            return st.executeBatch();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
        return new int[0];
    }
}
