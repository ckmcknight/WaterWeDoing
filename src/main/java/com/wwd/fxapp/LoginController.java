package com.wwd.fxapp;


import com.wwd.model.login.LoginCredentials;
import com.wwd.model.login.LoginInterface;
import com.wwd.model.login.LoginType;
import com.wwd.model.login.User;
import com.wwd.model.login.UserType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.rmi.RemoteException;

@RestController
public class LoginController {
    @Autowired
    private LoginInterface loginInterface;

    @CrossOrigin
    @RequestMapping("/wwd/login/register")
    public void registerUser(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.getString("userName"),
                    Long.parseLong(loginObj.get("password").toString())
            );
            UserType userType = UserType.valueOf(bodyObj.getString("userType")
                    .toUpperCase());
            String userName = bodyObj.getString("userName");

            loginInterface.registerUser(loginCredentials, userType, userName);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping("/wwd/login")
    public User loginUser(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.get("userName").toString(),
                    Long.parseLong(loginObj.get("password").toString())
            );

            return loginInterface.loginUser(loginCredentials);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    @CrossOrigin
    @RequestMapping("/wwd/login/update")
    public void updateUser(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");
        JSONObject userObj = bodyObj.getJSONObject("User");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.get("userName").toString(),
                    Long.parseLong(loginObj.get("password").toString())
            );

            User newUser = new User(
                    userObj.getString("userId"),
                    UserType.valueOf(userObj.getString("userType")
                            .toUpperCase()),
                    LoginType.INTERNAL,
                    userObj.getString("name")
            );
            newUser.setHomeAddress(userObj.getString("homeAddress"));
            newUser.setPhoneNumber(userObj.getString("phoneNumber"));
            newUser.setEmailAddress(userObj.getString("emailAddress"));

            loginInterface.updateUser(loginCredentials, newUser);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping("/wwd/login/requestReset")
    public boolean requestReset(@RequestParam(value = "userId",
            defaultValue = "") String userId) {
        try {
            return loginInterface.requestReset(userId);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @CrossOrigin
    @RequestMapping("/wwd/login/validateReset")
    public boolean validateReset(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.get("userName").toString(),
                    Long.parseLong(loginObj.get("password").toString())
            );

            return loginInterface.validateReset(
                    bodyObj.getString("validationCode"),
                    loginCredentials
            );
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @CrossOrigin
    @RequestMapping("/wwd/login/delete")
    public boolean deleteUser(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");
        JSONObject userObj = bodyObj.getJSONObject("User");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.get("userName").toString(),
                    Long.parseLong(loginObj.get("password").toString())
            );

            User dUser = new User(
                    userObj.getString("userId"),
                    UserType.valueOf(userObj.getString("userType")
                            .toUpperCase()),
                    LoginType.valueOf(userObj.getString("loginType")
                            .toUpperCase()),
                    userObj.getString("name")
            );

            return loginInterface.deleteUser(loginCredentials, dUser);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @CrossOrigin
    @RequestMapping("/wwd/login/allUsers")
    public User[] getAllUsers(@RequestBody String body) {
        JSONObject bodyObj = new JSONObject(body);
        JSONObject loginObj = bodyObj.getJSONObject("LoginCredentials");

        try {
            LoginCredentials loginCredentials = new LoginCredentials(
                    LoginType.INTERNAL,
                    loginObj.get("userName").toString(),
                    Long.parseLong(loginObj.get("password").toString())
            );

            return loginInterface.getAllUsers(loginCredentials)
                    .toArray(new User[0]);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
}
