package com.wwd.model.login;

import java.io.Serializable;

/**
 * Exception to be thrown if username or password is incorrect
 *
 */
public class InvalidCredentialsException extends RuntimeException
        implements Serializable {

    /**
     * Constructor for InvalidCredentials
     *
     */
    public InvalidCredentialsException() { }

    /**
     * Checks if user is not null and passes user to login method
     * @param msg the message to be displayed with the Exception
     */
    public InvalidCredentialsException(@SuppressWarnings("SameParameterValue")
                                               String msg) {
        super(msg);
    }
}
