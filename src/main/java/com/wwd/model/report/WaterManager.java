package com.wwd.model.report;

import com.wwd.model.login.InvalidCredentialsException;
import com.wwd.model.login.LoginCredentials;
import com.wwd.model.login.UnauthorizedOperationException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Water Manager remote interface to handle water spots and respective reports.
 */
public interface WaterManager extends Remote {
    /**
     * Adds a purity report to the correct water spot.
     * @param credentials the LoginCredentials for validation
     * @param location the Location of the report
     * @param condition the OverallCondition of the water spot
     * @param virusPPM the virus parts per million for the water
     * @param contaminantPPM the contaminant parts per million for the water
     * @throws RemoteException  when the server throws an exception
     * @throws InvalidCredentialsException when invalid credentials
     *                                     are entered
     * @throws UnauthorizedOperationException   thrown when user is null
     */
    void addPurityReport(LoginCredentials credentials,
                         Location location,
                         OverallCondition condition,
                         int virusPPM,
                         int contaminantPPM)
            throws RemoteException,
            InvalidCredentialsException,
            UnauthorizedOperationException;

    /**
     * Adds a water source report to the correct water spot.
     * @param credentials the LoginCredentials for validation
     * @param location the Location of the report
     * @param condition the WaterCondition of the water spot
     * @param type the WaterType of the water spot
     * @throws RemoteException  when the server throws an exception
     * @throws InvalidCredentialsException  when invalid credentials
     *                                     are entered
     * @throws UnauthorizedOperationException   thrown when user is null
     */
    void addSourceReport(LoginCredentials credentials,
                         Location location,
                         WaterCondition condition,
                         WaterType type)
            throws RemoteException,
            InvalidCredentialsException,
            UnauthorizedOperationException;

    /**
     * Returns all the water spots.
     * @param credentials the LoginCredentials for validation
     * @return a serializable ArrayList of all the water spots
     * @throws RemoteException  when the server throws an exception
     * @throws InvalidCredentialsException  when invalid credentials
     *                                     are entered
     * @throws UnauthorizedOperationException   thrown when user is null
     */
    ArrayList<WaterSpot> getWaterSpots(LoginCredentials credentials)
            throws RemoteException,
            InvalidCredentialsException,
            UnauthorizedOperationException;
}
