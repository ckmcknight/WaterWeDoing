package com.wwd.model.login;


import java.util.HashMap;
import java.util.Map;

/**
 * Class to validate login credentials and verify existing user.
 *
 */
public class InternalLoginValidator implements LoginValidator {

    /**
     * The map of valid credentials
     */
    @SuppressWarnings("CanBeFinal")
    private Map<String, LoginCredentials> credentialsMap;

    /**
     * Build a login validator, initialize the map
     */
    public InternalLoginValidator() {
        credentialsMap = new HashMap<>();
    }

    /**
     * Add valid LoginCredentials to the map
     * @param credentials valid LoginCredentials
     */
    public final void putValidCredentials(LoginCredentials credentials) {
        credentialsMap.put(credentials.getUid(), credentials);
    }

    /**
     * Validates login credentials to verify existing user.
     * @param credentials the credentials of the user
     *
     * @return whether the user has entered valid login credentials
     *
     */
    @Override
    public final boolean validateLogin(LoginCredentials credentials) {
        if (credentialsMap.containsKey(credentials.getUid())) {
            LoginCredentials realCredentials
                    = credentialsMap.get(credentials.getUid());
            return realCredentials.getUid().equals(credentials.getUid())
                    && realCredentials.getPassword()
                    .equals(credentials.getPassword());
        }
        return false;
    }
}
