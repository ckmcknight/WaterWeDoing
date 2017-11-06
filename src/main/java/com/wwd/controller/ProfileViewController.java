package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.login.User;
import com.wwd.model.login.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

/**
 * The controller for the profile viewer.
 */
public class ProfileViewController implements MainDependent {

    /**The main application dependency**/
    private MainFXApplication mainApp;

    private static final Logger LOGGER =
            Logger.getLogger("ProfileViewController");

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField streetNumTextField;

    @FXML
    private TextField streetNameTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField stateTextField;

    @FXML
    private TextField zipCodeTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private ComboBox<UserType> userTypeCombo;

    @FXML
    private void initialize() {
        userTypeCombo.setItems(UserType.valuesAsList());
    }

    private static final int NUMBER_OF_FIELDS = 5;


    private String formatPhoneNumber(TextField phoneNumberTextField) {
        String phoneNumber = phoneNumberTextField.getText();
        String cleanPhoneNumber = phoneNumber.replaceAll("[^\\d]", "");
        char[] arr = cleanPhoneNumber.toCharArray();
        String validPhoneNumber;
        if (arr.length == 10) {
            validPhoneNumber = "+1" + (new String(arr));
            return validPhoneNumber;
        } else if ((arr.length == 11) && (arr[0] == '1')) {
            validPhoneNumber = "+" + (new String(arr));
            return validPhoneNumber;
        } else {
            return "";
        }
    }


    /**
     * Sets the main application
     * @param mainApp   the application to be set up
     * */
    @Override
    public void setMainApp(MainFXApplication mainApp) {
        boolean isFirst = (this.mainApp == null);
        this.mainApp = mainApp;
        if (isFirst) {
            loadUserData();
        }
    }

    @FXML
    private void handleSave() {
        if (mainApp == null) {
            return;
        }
        User newUser = mainApp.getCurrentUser();
        if (newUser == null) {
            newUser = new User();
        }

        String homeAddress = streetNumTextField.getText() + ","
                + streetNameTextField.getText() + ","
                + cityTextField.getText() + ","
                + stateTextField.getText() + ","
                + zipCodeTextField.getText();

        newUser.setHomeAddress(homeAddress);
        newUser.setPhoneNumber(formatPhoneNumber(phoneNumberTextField));
        newUser.setEmailAddress(emailTextField.getText());
        newUser.setUserType(userTypeCombo.getValue());

        mainApp.updateCurrentUser(newUser);
    }

    @FXML
    private void handleReset() {
        loadUserData();
    }

    private void loadUserData() {
        if (mainApp == null) {
            return;
        }
        User currentUser = mainApp.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        emailTextField.setText(currentUser.getEmailAddress());

        if (currentUser.getPhoneNumber() != null) {
            phoneNumberTextField.setText(currentUser.getPhoneNumber());
        } else {
            phoneNumberTextField.setText("");
        }

        String[] homeAddressValues = currentUser.getHomeAddress().split(",");
        String[] fieldValues = new String[NUMBER_OF_FIELDS];
        for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
            if (i < homeAddressValues.length) {
                fieldValues[i] = homeAddressValues[i];
            } else {
                fieldValues[i] = "";
            }
        }
        streetNumTextField.setText(fieldValues[0]);
        streetNameTextField.setText(fieldValues[1]);
        cityTextField.setText(fieldValues[2]);
        stateTextField.setText(fieldValues[3]);
        zipCodeTextField.setText(fieldValues[4]);

        userTypeCombo.getSelectionModel().select(currentUser.getUserType());
    }
}
