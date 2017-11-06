package com.wwd.model.login;

import java.io.Serializable;

/**
 * Exception to be thrown when registration error occurs
 */
public class RegistrationException extends RuntimeException
        implements Serializable {
    /**
     * Constructor for RegistrationException
     *
     */
    public RegistrationException() { }

    /**
     * Checks if user is not null and passes user to login method
     * @param msg the message to be displayed with the Exception
     */
    @SuppressWarnings("unused")
    public RegistrationException(@SuppressWarnings("SameParameterValue")
                                         String msg) {

        super(msg);
    }
}
