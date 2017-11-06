package com.wwd.model.server;


import com.wwd.model.login.InvalidCredentialsException;
import com.wwd.model.login.LoginCredentials;
import com.wwd.model.login.LoginInterface;
import com.wwd.model.login.LoginType;
import com.wwd.model.login.PasswordRecovery;
import com.wwd.model.login.RegistrationException;
import com.wwd.model.login.UnauthorizedOperationException;
import com.wwd.model.login.User;
import com.wwd.model.login.UserType;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for logging in users remotely
 */
public class ServerLogin implements LoginInterface {

    private final SqlQuerier querier = new SqlQuerier();

    private static final String TABLE = "users";
    private Map<String, String> validationCodeMap = new HashMap<>();

    /**
     * The java logger for this class
     */
    private static final Logger LOGGER =
            Logger.getLogger("ServerLogin");

    @Override
    public void registerUser(LoginCredentials credentials, UserType userType,
                             String userName) throws RemoteException {
        String registerQuery = "INSERT INTO " +  TABLE
                + " SET user_id='" + credentials.getUid() + "', "
                + "password_hash=" + credentials.getPassword() + ", "
                + "login_type='" + LoginType.INTERNAL + "', "
                + "user_type='" + userType.toString() + "', "
                + "name='" + userName + "';";

        if (querier.executeUpdate(registerQuery) == -1) {
            throw new RegistrationException();
        }
    }

    public User loginUser(LoginCredentials loginCredentials)
            throws InvalidCredentialsException {
        String query = "SELECT * FROM " + TABLE + " WHERE user_id='"
                + loginCredentials.getUid()
                + "' AND password_hash=" + loginCredentials.getPassword();
        ResultSet result = querier.query(query);
        System.out.println(querier);
        try {
            if (result.next()) {
                return constructUser(result);
            } else {
                throw new InvalidCredentialsException();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot connect to Login Service");
        }
        return null;
    }

    public void updateUser(LoginCredentials loginCredentials, User user)
            throws RemoteException {
        String updateStatement = "UPDATE " + TABLE
                + " SET email_address='" + user.getEmailAddress() + "', "
                + "home_address='" + user.getHomeAddress() + "', "
                + "user_type='" + user.getUserType().toString() + "', "
                + "phone_number='" + user.getPhoneNumber() + "' "
                + "WHERE user_id='" + loginCredentials.getUid() + "' "
                + "AND password_hash='" + loginCredentials.getPassword() + "';";
        if (querier.executeUpdate(updateStatement) == -1) {
            LOGGER.log(Level.SEVERE, "User update failed");
        }

    }

    public boolean requestReset(String userId)
            throws RemoteException {
        String query = "SELECT * FROM " + TABLE + " WHERE user_id='"
                + userId + "'";
        ResultSet result = querier.query(query);
        System.out.println(querier);
        try {
            if (result.next()) {
                User user = constructUser(result);
                String userPhone = user.getPhoneNumber();
                if (userPhone == null || userPhone.isEmpty()) {
                    // Needs a new account
                    return false;
                } else {
                    String secureCode = PasswordRecovery
                            .sendSecureCode(userPhone);
                    validationCodeMap.put(userId, secureCode);
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        // User not found in the table
        return false;
    }

    public boolean validateReset(String validationCode,
                                 LoginCredentials newLoginCredentials)
            throws RemoteException {
        String userId = newLoginCredentials.getUid();
        if (validationCodeMap.containsKey(userId)) {
            if (validationCode.equals(validationCodeMap.get(userId))) {
                String updatePasswordQuery = "UPDATE " + TABLE
                        + " SET password_hash='" + newLoginCredentials
                        .getPassword() + "'" + " WHERE user_id='" + userId
                        + "'";
                querier.executeUpdate(updatePasswordQuery);
                validationCodeMap.remove(userId);
                return true;
            }
            return false;
        }
        return false;
    }

    private User constructUser(ResultSet rs) {
        try {
            String uid = rs.getString("user_id");
            UserType userType = UserType.valueOf(rs.getString("user_type"));
            LoginType loginType = LoginType.valueOf(rs.getString("login_type"));
            String name = rs.getString("name");
            String emailAddress = rs.getString("email_address");
            String homeAddress = rs.getString("home_address");
            return new User(uid, userType, loginType, name, emailAddress,
                    homeAddress, rs.getString("phone_number"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
        throw new InvalidCredentialsException("Invalid credentials!");
    }

    @Override
    public LinkedList<User> getAllUsers(LoginCredentials loginCredentials) {
        LinkedList<User> users = new LinkedList<>();
        CompletableFuture<User> userFuture = CompletableFuture
                .supplyAsync(() -> loginUser(loginCredentials));
        try {
            User user = userFuture.get();
            if (!user.getUserType().equals(UserType.ADMIN)) {
                throw new UnauthorizedOperationException("Users of type <"
                        + user.getUserType() + "> does not have access.");
            }

            String getUsersQuery = "SELECT * FROM " + TABLE + ";";
            ResultSet rs = querier.query(getUsersQuery);
            try {
                while (rs.next()) {
                    users.add(constructUser(rs));
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "SQL ERROR:\n" + e.getMessage());
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        return users;
    }

    public boolean deleteUser(LoginCredentials loginCredentials, User user) {
        boolean deleted = false;

        CompletableFuture<User> userFuture = CompletableFuture
                .supplyAsync(() -> loginUser(loginCredentials));
        try {
            User requestingUser = userFuture.get();
            if (requestingUser == null) {
                throw new InvalidCredentialsException("Invalid credentials!");
            }
            if (!requestingUser.getUserType().equals(UserType.ADMIN)) {
                throw new UnauthorizedOperationException("User does not have "
                        + "access to this functionality.");
            }
            if (user.getUserId().equals(requestingUser.getUserId())) {
                throw new UnauthorizedOperationException("User cannot "
                        + "delete itself.");
            }

            String deleteUserQuery = "DELETE FROM " + TABLE
                    + " WHERE user_id='" + user.getUserId() + "';";
            if (querier.executeUpdate(deleteUserQuery) != -1) {
                deleted = true;
            }

        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        return deleted;
    }
}
