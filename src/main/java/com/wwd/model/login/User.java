package com.wwd.model.login;

import java.io.Serializable;

/**
 * Class to handle the user
 *
 */
public class User implements Serializable, Cloneable {
    private final String userId;
    private UserType userType;
    private final LoginType loginType;
    private String name;
    private String emailAddress;
    private String homeAddress;
    private String phoneNumber;

    /**
     * Initialize a user with an empty userID. Do not use explicitly.
     */
    public User() {
        this("");
    }

    /**
     * Initialize a user with the given userId.
     * @param userId the userId of this user.
     */
    public User(String userId) {
        this(userId, UserType.USER, LoginType.INTERNAL, "Anonymous");
    }

    /**
     * Initialize a user with userID, userType, loginType, and name
     * @param userId   the userId of this user.
     * @param userType the userType of this user.
     * @param loginType the loginType of this user.
     * @param name     the name of this user.
     */
    public User(String userId, UserType userType,
                LoginType loginType, String name) {
        this.userId = userId;
        this.userType = userType;
        this.loginType = loginType;
        this.name = name;
        this.homeAddress = ",,,,";
        this.emailAddress = "";
        this.phoneNumber = "";
    }

    public User(String userID, UserType userType, LoginType loginType,
                String name, String emailAddress, String homeAddress,
                String phoneNumber) {
        this(userID, userType, loginType, name);
        this.emailAddress = emailAddress;
        this.homeAddress = homeAddress;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Updates the User.
     * @param user   the user to be updated.
     */
    public void update(User user) {
        this.userType = user.userType;
        this.homeAddress = user.homeAddress;
        this.emailAddress = user.emailAddress;
        this.phoneNumber = user.phoneNumber;
    }

    /**
     * Get the userId.
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Get the UserType.
     * @return the UserType
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Set the UserType
     * @param userType the access type of this User
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Get the LoginType.
     * @return the LoginType
     */
    public LoginType getLoginType() {
        return loginType;
    }

    /**
     * Get the name of this user.
     * @return the name of this user
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this user.
     * @param name the name of this user
     */
    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the email address of this user.
     * @return the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the email address of this user.
     * @param emailAddress fully qualified email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Get the home address of this user.
     * @return the home address as a comma delimited string
     */
    public String getHomeAddress() {
        return homeAddress;
    }

    /**
     * Set the home address of this user.
     * @param homeAddress comma delimited string of home address
     */
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    /**
     * Get the phone number of this user.
     * @return the phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the home address of this user.
     * @param phoneNumber comma delimited string of home address
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
