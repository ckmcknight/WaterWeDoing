package com.wwd.model.login;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage login
 *
 */
public class LoginManager implements LoginInterface {

    /**  the java logger for this class */
    private static final Logger LOGGER = Logger.getLogger("LoginManager");

    private Map<LoginType, LoginValidator> loginValidators = new HashMap<>();
    private Map<String, User> userMap;
    private Map<String, String> validationCodeMap;



    public LoginManager() {
        initMaps();
        validationCodeMap = new HashMap<>();
    }

    /**
     * Initializes the maps so that validators can be correctly found
     * and users can be located by their UID.
     */
    private void initMaps() {
        loginValidators = new HashMap<>();
        InternalLoginValidator internalLoginValidator
                = new InternalLoginValidator();
        loginValidators.put(LoginType.INTERNAL, internalLoginValidator);

        userMap = new HashMap<>();

        // Hardcode "user" and "pass"
        internalLoginValidator.putValidCredentials(new
                LoginCredentials(LoginType.INTERNAL, "user", "pass"));
        userMap.put("0user", new User("0user"));
    }

    /**
     * Attempt to register the user.
     * @param credentials credentials to register
     * @param userType user type requested
     * @param userName user's name
     * @throws IllegalArgumentException for existing users
     */
    public void registerUser(LoginCredentials credentials,
                             UserType userType, String userName) {
        User newUser = new User(credentials.getUid(), userType,
                credentials.getLoginType(), userName);
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (credentials.getPassword() == null
                || credentials.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Choose a password");
        }
        if (userMap.containsKey(credentials.getUid())) {
            throw new IllegalArgumentException("User already exists");
        }
        userMap.put(newUser.getUserId(), newUser);
        if (credentials.getLoginType() == LoginType.INTERNAL) {
            InternalLoginValidator validator
                    = (InternalLoginValidator) (loginValidators
                    .get(LoginType.INTERNAL));
            validator.putValidCredentials(credentials);
        }
    }

    public User loginUser(LoginCredentials credentials)
            throws InvalidCredentialsException {
        LoginValidator validator
                = loginValidators.get(credentials.getLoginType());
        if (validator.validateLogin(credentials)) {
            return userMap.get(credentials.getUid());
        }
        throw new InvalidCredentialsException("Invalid Username or Password");
    }

    @Override
    public boolean requestReset(String userId) throws RemoteException {

        if (userMap.containsKey(userId)) {
            User user = userMap.get(userId);
            String userPhone = user.getPhoneNumber();
            if (userPhone == null) {
                // Needs a new account
                return false;
            } else {
                String secureCode = PasswordRecovery.sendSecureCode(userPhone);
                validationCodeMap.put(userId, secureCode);
                return true;
            }
        }

        // User doesn't exist on server
        return false;
    }

    @Override
    public boolean validateReset(String validationCode,
                                 LoginCredentials newLoginCredentials) {
        String userId = newLoginCredentials.getUid();
        if (validationCodeMap.containsKey(userId)) {
            if (validationCode.equals(validationCodeMap.get(userId))) {
                InternalLoginValidator internalLoginValidator
                        = (InternalLoginValidator) loginValidators
                        .get(LoginType.INTERNAL);
                internalLoginValidator.putValidCredentials(newLoginCredentials);
                validationCodeMap.remove(userId);
                return true;
            }
            return false;
        }
        return false;
    }

    public void updateUser(LoginCredentials loginCredentials, User user)
            throws RemoteException {

    }

    @Override
    public LinkedList<User> getAllUsers(LoginCredentials loginCredentials) {
        LinkedList<User> users = new LinkedList<>();
        CompletableFuture<User> userFuture = CompletableFuture
                .supplyAsync(() -> loginUser(loginCredentials));
        userFuture.thenAccept((user) -> {
                if (!user.getUserType().equals(UserType.ADMIN)) {
                    throw new UnauthorizedOperationException("Users of type <"
                            + user.getUserType() + "> does not have access.");
                }
                users.addAll(userMap.values());
            });
        return users;
    }

    @Override
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
                throw new UnauthorizedOperationException("User does not "
                        + "have access to this functionality.");
            }
            if (user.getUserId().equals(requestingUser.getUserId())) {
                throw new UnauthorizedOperationException("User cannot delete "
                        + "itself.");
            }

            if (userMap.containsKey(user.getUserId())) {
                deleted = true;
                userMap.remove(user.getUserId());
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        return deleted;
    }
}
