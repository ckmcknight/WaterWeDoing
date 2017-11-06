package com.wwd.model.login;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface LoginInterface extends Remote {

    void registerUser(LoginCredentials credentials, UserType userType,
                      String userName) throws RemoteException;

    User loginUser(LoginCredentials loginCredentials) throws RemoteException;

    void updateUser(LoginCredentials loginCredentials, User user)
            throws RemoteException;

    boolean requestReset(String userId) throws RemoteException;

    boolean validateReset(String validationCode,
                          LoginCredentials newLoginCredentials)
            throws RemoteException;

    boolean deleteUser(LoginCredentials loginCredentials, User user)
            throws InvalidCredentialsException, UnauthorizedOperationException,
            RemoteException;

    LinkedList<User> getAllUsers(LoginCredentials loginCredentials)
            throws InvalidCredentialsException, UnauthorizedOperationException,
            RemoteException;
}
