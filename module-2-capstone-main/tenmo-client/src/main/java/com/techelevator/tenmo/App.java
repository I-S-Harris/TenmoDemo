package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);




    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    private void printUsers (User[] users) {
        if (users != null) {
            System.out.println(users);
            }
        else {
            System.out.println("Error obtaining user list.");
        }

    }
    private void viewCurrentBalance() {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        try {
            System.out.println("Your account balance is: $" +accountService.getBalance());
        } catch (NullPointerException e){
            System.out.println("No Funds");
        }
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        try {
            System.out.println("Your Transfer History is:" );
            transferService.getAllTransfers();
        } catch (NullPointerException e) {
            System.out.println("No Transfer History available.");
        }

    }

    private void sendBucks() {
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        transferService.sendBucks();
    }


    private void viewPendingRequests() {}
        /*
        // TODO Auto-generated method stub
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        try {
            System.out.println("Transfers currently pending: ");
            transferService.getPendingRequests();
            } catch (NullPointerException e) {
            System.out.println("There are no requests pending.");
        }
        }
        */


    private void requestBucks() {
        // TODO Auto-generated method stub

    }



}
