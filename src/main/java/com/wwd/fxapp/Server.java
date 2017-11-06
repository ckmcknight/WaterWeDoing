package com.wwd.fxapp;

import com.wwd.model.login.LoginInterface;
import com.wwd.model.report.WaterManager;
import com.wwd.model.server.ServerLogin;
import com.wwd.model.server.ServerWaterManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("SpringFacetCodeInspection")
@SpringBootApplication
public class Server {

    private static LoginInterface mLoginInterface;

    private static WaterManager mWaterManager;

    @Bean
    public LoginInterface loginInterface() {
        return mLoginInterface;
    }

    @Bean
    public WaterManager waterManager() {
        return mWaterManager;
    }

    @SuppressWarnings("WeakerAccess")
    public static final int PORT = 1099;

    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "www.gttx.org");
            mLoginInterface = new ServerLogin();
            mWaterManager = new ServerWaterManager((ServerLogin)
                    mLoginInterface);
            LoginInterface loginStub = (LoginInterface) UnicastRemoteObject
                    .exportObject(mLoginInterface, 0);
            WaterManager waterManagerStub = (WaterManager) UnicastRemoteObject
                    .exportObject(mWaterManager, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind("wwd_login", loginStub);
            registry.bind("wwd_water_manager", waterManagerStub);

            System.out.println("Booting spring web service");
            SpringApplication.run(Server.class, args);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
