package com.wwd.model.login;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to send text message using Twilio
 */
public class PasswordRecovery {

    //Twilio account data
    private static final String ACCOUNT_SID
            = "AC4fd06c08c4f7e9c1383e173f0b2a10b9";
    private static final String AUTH_TOKEN = "727d8e3a29e209afe681860e6d326bf4";
    private static final PhoneNumber TWILIO_NUM
            = new PhoneNumber("+14044765172");
    private static final Logger LOGGER = Logger.getLogger("PasswordRecovery");

    public static String sendSecureCode(String phoneNumber) {
        SecureRandom random = new SecureRandom();
        String secureCode = new BigInteger(13, random).toString(10);
        sendMessage(phoneNumber, "Enter this code to change your password: "
                + secureCode);
        return secureCode;
    }

    @SuppressWarnings("UnusedAssignment")
    private static void sendMessage(String phoneNumber, String msg) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message
                .creator(new PhoneNumber(phoneNumber), TWILIO_NUM,
                        msg).create();
        LOGGER.log(Level.SEVERE, "Validation code was sent to " + phoneNumber);
    }
}
