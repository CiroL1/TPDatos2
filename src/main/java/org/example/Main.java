package org.example;
import Catalogo.*;
import Users.*;
import org.bson.Document;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Step 1: Connect user manager and create table
        UserManager userManager = new UserManager();
        userManager.connect();

        // Step 2: Create and insert a new user
        User newUser = new User();
        newUser.dni = 12345678;
        newUser.name = "John Doe";
        newUser.address = "123 Main St";
        newUser.sessionTime = 0;
        newUser.userType = "LOW";
        newUser.password = "password123";

        // Insert user into the DB (if not already there)
        if (userManager.getUserByDni(newUser.dni) == null) {
            userManager.insertUser(newUser);
            System.out.println("User created.");
        } else {
            System.out.println("User already exists.");
        }

        // Step 3: Manage session
        UserSessionManager sessionManager = new UserSessionManager(userManager);

        boolean loggedIn = sessionManager.login(newUser.dni, "password123");
        if (loggedIn) {
            try {
                // Simulate activity for ~5 minutes
                System.out.println("Simulating session for 1 minute...");
                Thread.sleep(1 * 60 * 1000); // 1 minutes in milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Step 4: Logout
            sessionManager.logout();
        }

        // Step 5: Close connection
        userManager.close();

    }

}
