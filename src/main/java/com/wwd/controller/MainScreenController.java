package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.login.User;
import com.wwd.model.login.UserType;
import com.wwd.model.main.PageType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * The controller for the root/main window
 *
 */
public class MainScreenController {

    /** reference back to mainApplication if needed */
    private MainFXApplication mainApplication;

    @FXML
    private Label welcomeLabel;

    @FXML
    private VBox tabBox;

    /**
     * allow for calling back to the main application code if necessary
     * @param main   the reference to the FX Application instance
     * */
    public void setMainApp(MainFXApplication main) {
        mainApplication = main;
        User currentUser = mainApplication.getCurrentUser();
        ObservableList<Node> tabChildren = tabBox.getChildren();
        if (currentUser != null) {
            UserType uType = currentUser.getUserType();
            for (PageType pageType : PageType.values()) {
                if (userCanAccess(uType, pageType)) {
                    if (mainApplication.getAvailablePages()
                            .contains(pageType)) {
                        tabChildren.add(tabChildren.size() - 1,
                                createTab(pageType.toString(), pageType));
                    }
                }
            }
        }
    }

    /**
     * Updates the info of the user that is passed in
     * @param user   the user whose info is being updated
     * */
    public void updateUserInfo(User user) {
        if (user != null && !user.getName().isEmpty()) {
            welcomeLabel.setText("Welcome, " + user.getName() + ".");
        }
    }

    /**
     * Handles logging the user out
     */
    @FXML
    private void handleLogOut() {
        mainApplication.logoutUser();
    }

    private Button createTab(String text, PageType pageType) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(45);
        b.setMinHeight(45);
        b.setAlignment(Pos.CENTER);
        b.setTextAlignment(TextAlignment.CENTER);
        b.setOnAction((actionEvent) -> mainApplication.goToPage(pageType));
        return b;
    }

    private static boolean userCanAccess(UserType userType, PageType pageType) {
        if (userType == UserType.ADMIN) {

            return !(pageType == PageType.MANAGE_REPORTS
                    || pageType == PageType.MANAGE_PURITY_REPORTS
                    || pageType == PageType.VIEW_HISTORICAL
                    || pageType == PageType.LOG_PURITY_REPORT
                    || pageType == PageType.FIND_WATER
                    || pageType == PageType.LOG_LOCATION_REPORT);

        } else if (userType == UserType.MANAGER) {

            return !(pageType == PageType.VIEW_SECURITY
                    || pageType == PageType.MANAGE_USERS);

        } else if (userType == UserType.WORKER) {

            return !(pageType == PageType.VIEW_SECURITY
                    || pageType == PageType.MANAGE_USERS
                    || pageType == PageType.MANAGE_REPORTS
                    || pageType == PageType.MANAGE_PURITY_REPORTS
                    || pageType == PageType.VIEW_HISTORICAL);

        } else if (userType == UserType.USER) {

            return !(pageType == PageType.VIEW_SECURITY
                    || pageType == PageType.MANAGE_USERS
                    || pageType == PageType.MANAGE_REPORTS
                    || pageType == PageType.MANAGE_PURITY_REPORTS
                    || pageType == PageType.VIEW_HISTORICAL
                    || pageType == PageType.LOG_PURITY_REPORT);
        }

        return false;
    }
}
