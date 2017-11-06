package com.wwd.fxapp;

import com.wwd.model.login.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SuppressWarnings("SpringFacetCodeInspection")
@SpringBootApplication
public class SpringTester {
    private static LoginInterface mLoginInterface;

    @Bean
    public LoginInterface loginInterface() {
        return mLoginInterface;
    }

    public static void main(String[] args) {
        mLoginInterface = new LoginManager();
        /*try {
            mLoginInterface.registerUser(new LoginCredentials(LoginType.INTERNAL, "user", "pass"), UserType.USER, "Default User");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }*/
        SpringApplication.run(SpringTester.class, args);
    }
}
