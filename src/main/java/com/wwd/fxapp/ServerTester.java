package com.wwd.fxapp;

import com.wwd.model.login.*;
import com.wwd.model.report.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Tester for Server
 */
public class ServerTester {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("www.gttx.org", Server.PORT);
            LoginInterface login = (LoginInterface) registry
                    .lookup("wwd_login");
            WaterManager waterManager = (WaterManager) registry
                    .lookup("wwd_water_manager");
            LoginCredentials creds = new LoginCredentials(
                    LoginType.INTERNAL, "user4", "pass");

            try {
                LoginCredentials newLoginCredential
                        = new LoginCredentials(LoginType.INTERNAL, "user4",
                        "pass");
                login.registerUser(newLoginCredential, UserType.ADMIN, "user");
            } catch (RegistrationException e) {
                System.out.println(e.getMessage());
            }

            User user = login.loginUser(creds);
            System.out.println(user.getEmailAddress());

            waterManager.addSourceReport(creds,
                    new Location(10.0f, 3.0f),
                    WaterCondition.POTABLE,
                    WaterType.SPRING);
            List<WaterSpot> waterSpotList = waterManager.getWaterSpots(creds);
            System.out.println(waterSpotList.size() + " water spots found:");
            for (WaterSpot spot : waterSpotList) {
                System.out.println(spot.toString() + "\n");
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
