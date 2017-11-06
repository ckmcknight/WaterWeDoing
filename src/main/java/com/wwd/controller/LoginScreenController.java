package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.login.LoginCredentials;
import com.wwd.model.login.LoginType;
import com.wwd.model.login.User;
import com.wwd.model.login.UserType;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller for the login window
 */
public class LoginScreenController implements Consumer<User> {

    /**
     * the java logger for this class
     */
    private static final Logger LOGGER =
            Logger.getLogger("LoginScreenController");
    private static final String LOGIN_ERROR_MSG = "Enter a valid login";
    private static final String REGISTER_ERROR_MSG = "User already exists";
    private static final String FORGOT_PASSWORD_MSG = "Enter your username";
    private static final String NONEXISTENT_USER = "You need a new account";
    private static final String BAD_INVALIDATION_CODE
            = "That is not a valid code";

    @FXML
    private TextField userField;

    @FXML
    private TextField passField;

    @FXML
    private TextField nameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Pane registerTypePane;

    @FXML
    private Pane forgotPasswordPane;

    @FXML
    private RadioButton workerRb;

    @FXML
    private RadioButton managerRb;

    @FXML
    private RadioButton adminRb;

    @FXML
    private ToggleGroup group;

    @FXML
    private TextField validator;

    @FXML
    private TextField newPassword;


    /**
     * reference back to mainApplication if needed
     */
    private MainFXApplication mainApplication;

    /**
     * allow for calling back to the main application code if necessary
     *
     * @param main the reference to the FX Application instance
     */
    public void setMainApp(MainFXApplication main) {
        mainApplication = main;
    }

    /*
    Stores values in user fields and checks them for validity
     */
    public void handleLoginPressed() {
        LoginCredentials userCredentials
                = new LoginCredentials(LoginType.INTERNAL, userField.getText(),
                passField.getText());
        mainApplication.setCredentials(userCredentials);
        attemptLoginUser(userCredentials);
    }

    /**
     * Checks if user is not null and passes user to login method
     *
     * @param user the User to be logged in
     */
    @Override
    public void accept(User user) {
        if (user != null) {
            mainApplication.loginUser(user);
        }
    }

    /**
     * Returns the Consumer after operation has been performed
     *
     * @param after the current Consumer
     * @return the Consumer
     */
    @Override
    public Consumer<User> andThen(Consumer<? super User> after) {
        return this;
    }

    /*
     * clears all text values when pressed.
     */
    public void handleCancelPressed() {
        errorLabel.setVisible(false);
        userField.clear();
        passField.clear();
        userField.setPromptText("Username");
        passField.setPromptText("Password");
    }

    /*
     * takes values and sets up a new user
     */
    public void handleRegisterPressed() {
        if (userField.getText().isEmpty()) {
            Platform.runLater(() -> {
                    errorLabel.setText(REGISTER_ERROR_MSG);
                    errorLabel.setVisible(true);
                });
            return;
        }
        //TODO: Check if user already exists before going to register pane
        registerTypePane.setVisible(true);
    }

    /*
     * Gives the user privileges corresponding to their level of access
     */
    public void handleFinishPressed() {
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        UserType userType = UserType.USER;
        if (selected == workerRb) {
            userType = UserType.WORKER;
        } else if (selected == managerRb) {
            userType = UserType.MANAGER;
        } else if (selected == adminRb) {
            userType = UserType.ADMIN;
        }

        LoginCredentials registerCredentials
                = new LoginCredentials(LoginType.INTERNAL, userField.getText(),
                passField.getText());
        mainApplication.setCredentials(registerCredentials);
        try {
            mainApplication.getLoginInterface().registerUser(
                registerCredentials, userType, nameField.getText());
            registerTypePane.setVisible(false);
            attemptLoginUser(registerCredentials);
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
            registerTypePane.setVisible(false);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Cannot connect to Login Service");
        }
    }

    /*
     * Allows "Enter" to be pressed instead of the register button
     */
    public void handleKeyInput(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLoginPressed();
        }
    }

    private void attemptLoginUser(LoginCredentials attemptLoginCredentials) {
        CompletableFuture<User> userFuture
                = CompletableFuture.supplyAsync(() -> {
                        try {
                            return mainApplication.getLoginInterface()
                                    .loginUser(attemptLoginCredentials);
                        } catch (RemoteException e) {
                            LOGGER.log(Level.SEVERE,
                                    "Cannot connect to Login Service:\n"
                                            + e.getMessage());
                        }
                        return null;
                    });
        userFuture.exceptionally(ex -> {
                Platform.runLater(() -> {
                        errorLabel.setText(LOGIN_ERROR_MSG);
                        errorLabel.setVisible(true);
                        passField.clear();
                        passField.setPromptText("Password");
                    });
                return null;
            }).thenAccept(this);
    }

    /*
     * Handles if a user forgot their password
     */
    public void handleForgotPasswordPressed() {
        if (userField.getText().isEmpty()) {
            Platform.runLater(() -> {
                    errorLabel.setText(FORGOT_PASSWORD_MSG);
                    errorLabel.setVisible(true);
                });
            return;
        }

        try {
            //Server sends user a validation code
            // if userID exists and user has a phone number
            if (mainApplication.getLoginInterface()
                    .requestReset("0" + userField.getText())) {
                forgotPasswordPane.setVisible(true);
            } else {
                errorLabel.setText(NONEXISTENT_USER);
                errorLabel.setVisible(true);
            }

        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

    /*
     * Resets user's password and logs them in
     */
    public void handleFinishPressedNewPassword() {
        LoginCredentials registerCredentials
                = new LoginCredentials(LoginType.INTERNAL, userField.getText(),
                newPassword.getText());
        try {
            if (mainApplication.getLoginInterface()
                    .validateReset(validator.getText(), registerCredentials)) {
                forgotPasswordPane.setVisible(false);
                attemptLoginUser(registerCredentials);
            } else {
                forgotPasswordPane.setVisible(false);
                errorLabel.setText(BAD_INVALIDATION_CODE);
                errorLabel.setVisible(true);
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}