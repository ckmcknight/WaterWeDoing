package com.wwd.controller;

import com.wwd.fxapp.MainFXApplication;
import com.wwd.model.login.InvalidCredentialsException;
import com.wwd.model.login.UnauthorizedOperationException;
import com.wwd.model.login.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller for the profile viewer.
 */
public class ManageUsersController implements MainDependent, Unaware {

    /**The main application dependency**/
    private MainFXApplication mainApp;

    private static final Logger LOGGER =
            Logger.getLogger("ManageUsersController");

    private List<User> users;
    private int maxSheets = 1;
    private int currentSheet = 1;

    private Label[][] itemLabels;
    private Button[] itemButtons;

    private Comparator<User> comparator
            = Comparator.comparing(User::getUserType)
            .thenComparing(User::getUserId);

    @FXML
    GridPane sheetPane;

    @FXML
    TextField sheetNumberField;

    @FXML
    Label maxSheetLabel;

    @FXML
    private void initialize() {
        initLabels();
        initButtons();
        sheetNumberField.setOnKeyPressed((ev) -> {
                if (ev.getCode() == KeyCode.ENTER) {
                    try {
                        int newSheet
                                = Integer.parseInt(sheetNumberField.getText());
                        setCurrentSheet(newSheet);
                    } catch (NumberFormatException e) {
                        // Bad format
                        setCurrentSheet(currentSheet);
                    }
                }
            });
    }


    /**
     * Sets the main application
     * @param mainApp   the application to be set up
     * */
    @Override
    public void setMainApp(MainFXApplication mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void receiveFocus() {
        reload();
    }

    @FXML
    private void handlePrev() {
        setCurrentSheet(currentSheet - 1);
    }

    @FXML
    private void handleNext() {
        setCurrentSheet(currentSheet + 1);
    }

    private void reload() {
        users = new LinkedList<>();
        try {
            users = mainApp.getLoginInterface()
                    .getAllUsers(mainApp.getLoginCredentials());
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Could not transact with server!\n"
                    + e.getMessage());
        } catch (UnauthorizedOperationException e) {
            LOGGER.log(Level.WARNING, "Unauthorized Op:\n" + e.getMessage());
        }
        users.sort(comparator);

        maxSheets = users.size() / 10;
        if (users.size() % 10 != 0) {
            maxSheets += 1;
        }
        if (maxSheets <= 0) {
            maxSheets = 1;
        }
        maxSheetLabel.setText(" of " + maxSheets);
        setCurrentSheet(1, true);
    }

    private void setCurrentSheet(int newSheet) {
        setCurrentSheet(newSheet, false);
    }

    private void setCurrentSheet(int newSheet, boolean forceReload) {
        if (newSheet < 1) {
            newSheet = 1;
        } else if (newSheet > maxSheets) {
            newSheet = maxSheets;
        }

        if (newSheet == currentSheet) {
            sheetNumberField.setText(currentSheet + "");
            if (forceReload) {
                loadReport(currentSheet);
            }
        } else {
            currentSheet = newSheet;
            sheetNumberField.setText(currentSheet + "");
            loadReport(currentSheet);
        }
    }

    private User getUser(int sheetNumber, int row) {
        int start = ((sheetNumber - 1) * 10);
        if (start + row < users.size()) {
            return users.get(start + row);
        }
        return null;
    }

    private void loadReport(int sheetNumber) {


        for (int i = 0; i < 10; i++) {
            // Build the values to display for this row
            String[] values = new String[5];
            itemButtons[i].setVisible(false);

            User u = getUser(sheetNumber, i);
            if (u != null) {
                values[0] = u.getUserId();
                values[1] = u.getUserType() + "";
                values[2] = u.getName();
                values[3] = u.getEmailAddress();
                values[4] = u.getPhoneNumber();
                itemButtons[i].setVisible(true);
            }

            for (int n = 0; n < values.length; n++) {
                itemLabels[i][n].setText(values[n]);
            }
        }
    }

    private void initLabels() {
        itemLabels = new Label[10][5];
        for (int row = 0; row < itemLabels.length; row++) {
            for (int col = 0; col < itemLabels[row].length; col++) {
                Label newLabel = new Label("");
                newLabel.setMaxWidth(Double.MAX_VALUE);
                newLabel.setMaxHeight(Double.MAX_VALUE);
                newLabel.setAlignment(Pos.CENTER);
                newLabel.setTextAlignment(TextAlignment.CENTER);
                itemLabels[row][col] = newLabel;
                sheetPane.add(newLabel, col + 1, row + 1);
            }
        }
    }

    private void initButtons() {
        itemButtons = new Button[10];
        for (int row = 0; row < itemButtons.length; row++) {
            Button newButton = new Button("X");
            newButton.setMaxWidth(Double.MAX_VALUE);
            newButton.setMaxHeight(Double.MAX_VALUE);
            newButton.setAlignment(Pos.CENTER);
            newButton.setTextAlignment(TextAlignment.CENTER);

            final int thisRow = row;
            newButton.setOnAction((event) -> deleteUser(thisRow));

            itemButtons[row] = newButton;
            sheetPane.add(newButton, 0, row + 1);
        }
    }

    private void deleteUser(int row) {
        User user = getUser(currentSheet, row);

        boolean shouldReload = false;
        if (user != null) {
            try {
                shouldReload = mainApp.getLoginInterface().deleteUser(
                        mainApp.getLoginCredentials(),
                        user
                );
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, "Could not transact with server!\n"
                        + e.getMessage());
            } catch (InvalidCredentialsException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            } catch (UnauthorizedOperationException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }

        if (shouldReload) {
            reload();
        }
    }
}
