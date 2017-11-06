package com.wwd.model.login;

import java.io.Serializable;

/**
 * Class to handle user credentials
 *
 */
public class LoginCredentials implements Serializable {

    private final LoginType loginType;
    private final String userName;
    private final String password;
    private static final Long MOD_SPACE = ((long) 1 << 53);

    /**
     * Gets the user credentials
     * @param loginType the type of login method:
     *                  username/password, Facebook, Google, etc.
     * @param userName the user's username
     * @param password User's password
     */
    public LoginCredentials(@SuppressWarnings("SameParameterValue")
                            LoginType loginType, String userName,
                            String password) {
        this(loginType, userName, hashString(password));
    }

    /**
     * Gets the user credentials
     * @param loginType the type of login method:
     *                  username/password, Facebook, Google, etc.
     * @param userName the user's username
     * @param hashCode hash of the user's password
     */
    public LoginCredentials(LoginType loginType, String userName,
                            long hashCode) {
        this.loginType = loginType;
        this.userName = userName;
        this.password = hashCode + "";
    }

    /**
     * Gets the user id
     * @return the user's id
     */
    public String getUid() {
        return loginType.ordinal() + userName;
    }

    /**
     * Gets the login type
     * @return the login type
     */
    public LoginType getLoginType() {
        return loginType;
    }

    /**
     * Gets the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Converts the login type, username, and password to a string
     * @return a string with the login type, username, and password to a string
     */
    @Override
    public String toString() {
        return loginType.ordinal() + " " + userName + " " + password;
    }

    private static long hashString(String str) {
        long hash = 7;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash * 31 + str.charAt(i)) % MOD_SPACE;
        }
        return hash;
    }
}
