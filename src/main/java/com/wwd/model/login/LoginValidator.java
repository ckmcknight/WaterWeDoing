package com.wwd.model.login;

/**
 * Interface that validates login
 *
 */
interface LoginValidator {

    /**
     * Calls the validateLogin method
     * @param credentials the user's name and password
     * @return whether the user's credentials were valid
     *
     */
    boolean validateLogin(LoginCredentials credentials);
}
