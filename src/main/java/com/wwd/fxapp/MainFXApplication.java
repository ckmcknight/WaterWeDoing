package com.wwd.fxapp;


import com.wwd.controller.GMapsController;
import com.wwd.controller.LoginScreenController;
import com.wwd.controller.MainDependent;
import com.wwd.controller.MainScreenController;
import com.wwd.controller.ManagePurityReportsController;
import com.wwd.controller.ManageReportsController;
import com.wwd.controller.ManageUsersController;
import com.wwd.controller.ProfileViewController;
import com.wwd.controller.PurityReportController;
import com.wwd.controller.Unaware;
import com.wwd.controller.ViewHistoricalController;
import com.wwd.controller.WaterReportController;
import com.wwd.model.login.LoginCredentials;
import com.wwd.model.login.LoginInterface;
import com.wwd.model.login.LoginManager;
import com.wwd.model.login.User;
import com.wwd.model.main.Page;
import com.wwd.model.main.PageType;
import com.wwd.model.report.WaterFinder;
import com.wwd.model.report.WaterManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class.  Some code reused from
 * http://code.makery.ch/library/javafx-8-tutorial/
 *
 * This class handles all the scene switching to reuse the main stage.
 *
 */
public class MainFXApplication extends Application {
    @SuppressWarnings("WeakerAccess")
    public static final boolean IS_LOCAL = false;

    /**  the java logger for this class */
    private static final Logger LOGGER = Logger.getLogger("MainFXApplication");

    /** the main container for the application window */
    private static Stage mainScreen;

    /** the login manager to use for this entire application **/
    private LoginInterface loginInterface;

    /** the main layout for the main window */
    private BorderPane rootLayout;

    /** the map of main pages **/
    private final Map<PageType, Page> pageMap = new HashMap<>();

    //private final WaterFinder waterFinder = new WaterFinder();

    private WaterManager waterManager;

    private LoginCredentials credentials;

    /**
     * Method to start the application.
     *@param primaryStage the main stage of the application
     *
     */
    @Override
    public void start(Stage primaryStage) {
        if (IS_LOCAL) {
            loginInterface = new LoginManager();
            waterManager = new WaterFinder();
        } else {
            try {
                Registry registry = LocateRegistry.getRegistry("www.gttx.org",
                        Server.PORT);
                loginInterface = (LoginInterface) registry.lookup("wwd_login");

                waterManager = (WaterManager) registry
                        .lookup("wwd_water_manager");
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
        LOGGER.log(Level.INFO, "LOCAL: " + IS_LOCAL);

        mainScreen = primaryStage;
        goToLoginScreen(mainScreen);
    }

    private void initMainPages() {
        String fxmlToLoad = "n/a";
        try {
            // Attempt to load the profile view page
            fxmlToLoad = "ProfileViewScreen.fxml";
            pageMap.put(PageType.PROFILE_VIEW,
                    new Page<ProfileViewController>(MainFXApplication.class
                            .getResource("/com/wwd/view/" + fxmlToLoad)));

            fxmlToLoad = "WaterReportScreen.fxml";
            pageMap.put(PageType.LOG_LOCATION_REPORT,
                    new Page<WaterReportController>(MainFXApplication
                            .class.getResource("/com/wwd/view/" + fxmlToLoad)));

            fxmlToLoad = "PurityReportScreen.fxml";
            pageMap.put(PageType.LOG_PURITY_REPORT,
                    new Page<PurityReportController>(MainFXApplication
                            .class.getResource("/com/wwd/view/" + fxmlToLoad)));

            fxmlToLoad = "ManageReportsScreen.fxml";
            pageMap.put(PageType.MANAGE_REPORTS,
                    new Page<ManageReportsController>(MainFXApplication
                            .class.getResource("/com/wwd/view/" + fxmlToLoad)));

            fxmlToLoad = "GMapsScreen.fxml";
            pageMap.put(PageType.FIND_WATER,
                    new Page<GMapsController>(MainFXApplication
                            .class.getResource("/com/wwd/view/" + fxmlToLoad)));

            fxmlToLoad = "ManagePurityReportsScreen.fxml";
            pageMap.put(PageType.MANAGE_PURITY_REPORTS,
                    new Page<ManagePurityReportsController>(MainFXApplication
                            .class.getResource("/com/wwd/view/" + fxmlToLoad)));

            fxmlToLoad = "ViewHistoricalReport.fxml";
            pageMap.put(PageType.VIEW_HISTORICAL,
                    new Page<ViewHistoricalController>(MainFXApplication
                            .class.getResource("/com/wwd/view/" + fxmlToLoad)));

            fxmlToLoad = "ManageUsersScreen.fxml";
            pageMap.put(PageType.MANAGE_USERS,
                    new Page<ManageUsersController>(MainFXApplication
                            .class.getResource("/com/wwd/view/" + fxmlToLoad)));

            // Set the main app for those who need it
            //noinspection Convert2streamapi
            for (Page p : pageMap.values()) {
                if (p.getController() instanceof MainDependent) {
                    MainDependent m = (MainDependent) (p.getController());
                    m.setMainApp(this);
                }
            }

        } catch (IOException e) {
            //error on load, so log it
            LOGGER.log(Level.SEVERE,
                    "Failed to find the fxml file for " + fxmlToLoad + "!!");
            e.printStackTrace();
        } catch (Exception e) {
            //unexpected error
            LOGGER.log(Level.SEVERE, "Unexpected error!");
            e.printStackTrace();
        }

    }

    /**
     * get the login manager for this application
     * @return the LoginManager
     */
    public LoginInterface getLoginInterface() {
        return loginInterface;
    }

    /**
     * Get the water manager.
     * @return the waterManager
     */
    public WaterManager getWaterManager() {
        return waterManager;
    }

    /**
     * Gets the available pages
     * @return the keySet of available pages
     * */
    public Set<PageType> getAvailablePages() {
        return pageMap.keySet();
    }

    public void setCredentials(LoginCredentials newCredentials) {
        this.credentials = newCredentials;
    }

    public LoginCredentials getLoginCredentials() {
        return credentials;
    }


    /**
     * Initialize the main screen for the application.
     * Most other views will be shown in this screen.
     *
     * @param mainScreen  the main Stage window of the application
     */
    private void initRootLayout(Stage mainScreen) {
        try {
            // Initialize the main pages
            initMainPages();

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainFXApplication
                    .class.getResource("/com/wwd/view/MainScreen.fxml"));
            rootLayout = loader.load();

            // Give the controller access to the main app.
            MainScreenController controller = loader.getController();
            controller.setMainApp(this);
            controller.updateUserInfo(realUser);

            setPage(PageType.PROFILE_VIEW);

            // Set the Main App title
            mainScreen.setTitle("Hello Water!");

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            mainScreen.setScene(scene);
            mainScreen.setResizable(true);
            mainScreen.show();

        } catch (IOException e) {
            //error on load, so log it
            LOGGER.log(Level.SEVERE,
                    "Failed to find the fxml file for MainScreen!!");
            e.printStackTrace();
        } catch (Exception e) {
            //unexpected error
            LOGGER.log(Level.SEVERE, "Unexpected error!");
            e.printStackTrace();
        }
    }

    /**
     * Switch to the login screen for the application.
     *
     * @param mainScreen  the main Stage window of the application
     */
    private void goToLoginScreen(Stage mainScreen) {
        try {
            // Load login layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainFXApplication
                    .class.getResource("/com/wwd/view/loginScreen.fxml"));
            /* the layout used for the login screen */
            AnchorPane loginLayout = loader.load();

            // Give the controller access to the main app.
            LoginScreenController controller = loader.getController();
            controller.setMainApp(this);

            // Set the Login App title
            mainScreen.setTitle("Please log in.");

            // Show the scene containing the login layout.
            Scene scene = new Scene(loginLayout);
            mainScreen.setScene(scene);
            mainScreen.setResizable(false);
            mainScreen.show();


        } catch (IOException e) {
            //error on load, so log it
            LOGGER.log(Level.SEVERE,
                    "Failed to find the fxml file for LoginScreen!!");
            e.printStackTrace();
        }
    }

    private void setPage(PageType pageType) {
        Page found;
        if (pageMap.containsKey(pageType) && rootLayout != null) {
            found = pageMap.get(pageType);
            if (found.getController() instanceof Unaware) {
                Unaware un = (Unaware) (found.getController());
                un.receiveFocus();
            }
            rootLayout.setCenter(found.getViewPane());
        }
    }

    private User realUser = new User();
    private static final User dummyUser = new User();

    /**
     * Logs the user in
     * @param user The user to be logged in
     */
    public void loginUser(User user) {
        realUser = user;
        Platform.runLater(() -> initRootLayout(mainScreen));
    }

    /**
     * Logs the user out and returns to the login screen
     */
    public void logoutUser() {
        realUser = dummyUser;
        Platform.runLater(() -> goToLoginScreen(mainScreen));
    }

    /**
     * Goes to the next page of the application
     * @param pageType   the page to be displayed next
     */
    public void goToPage(PageType pageType) {
        setPage(pageType);
    }

    /**
     * Gets the current user
     * @return the current User
     */
    public User getCurrentUser() {
        return realUser;
    }

    /**
     * Updates the current user
     * @param newUser   the user that is to become the currentUser
     */
    public void updateCurrentUser(User newUser) {
        if (newUser != null) {
            realUser.update(newUser);
            try {
                loginInterface.updateUser(getLoginCredentials(), newUser);
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, e.toString());
            }
        }
    }

    /**
     * Main method to launch application
     */
    public static void main(String[] args) {
        launch(MainFXApplication.class, args);
    }
}
