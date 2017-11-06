package com.wwd.model.login;

import java.io.Serializable;

public class UnauthorizedOperationException extends RuntimeException
        implements Serializable {
    /**
     * Constructor for UnauthorizedOperationException
     *
     */
    @SuppressWarnings("unused")
    public UnauthorizedOperationException() { }

    /**
     * Checks if user is not null and passes user to login method
     * @param msg the message to be displayed with the Exception
     */
    public UnauthorizedOperationException(String msg) {
        super(msg);
    }
}
