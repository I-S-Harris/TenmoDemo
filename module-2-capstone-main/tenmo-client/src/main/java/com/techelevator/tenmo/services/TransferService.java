package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {
    private String API_BASE_URL;
    private AuthenticatedUser currentUser;

    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String API_BASE_URL, AuthenticatedUser currentUser) {
        this.API_BASE_URL = API_BASE_URL;
        this.currentUser = currentUser;
    }

    public TransferService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }

    //Authenticated user can view list of transfers sent/received.
    //Authenticated user can view transfer details by ID.
    //Transfer status shows as approved.
    public void getAllTransfers() {
        Transfer[] allTransfers = null;
        allTransfers = restTemplate.exchange(API_BASE_URL + "account/transfers/" + currentUser.getUser().getId(), HttpMethod.GET, authEnt(), Transfer[].class).getBody();

        for (Transfer i : allTransfers) {
//
            System.out.println("Transfer ID: " + i.getTransferId() + "\t\t" + "From: " + i.getUsernameFrom() + "\t\t$" + i.getAmount() + "\t\t" + " To: " + i.getUsernameTo());
        }
        System.out.print("-------------------------------------------\r\n" +
                "Please enter transfer ID to view details (0 to cancel): ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (Integer.parseInt(input) != 0) {
            boolean foundTransferId = false;
            for (Transfer i : allTransfers) {
                if (Integer.parseInt(input) == i.getTransferId()) {
                    Transfer temp = restTemplate.exchange(API_BASE_URL + "transfer/" + i.getTransferId(), HttpMethod.GET, authEnt(), Transfer.class).getBody();
                    foundTransferId = true;
                    System.out.println("--------------------------------------------\r\n" +
                            "Transfer Details\r\n" +
                            "--------------------------------------------\r\n" +
                            " Id: " + temp.getTransferId() + "\r\n" +
                            " From: " + temp.getUsernameFrom() + "\r\n" +
                            " To: " + temp.getUsernameTo() + "\r\n" +
                            " Status: " + temp.getTransferStatus() + "\r\n" +
                            " Amount: $" + temp.getAmount());
                }
            }
            if (!foundTransferId) {
                System.out.println("Not a valid transfer ID");
            }
        }
    }
    //Authenticated user can view pending requests.
    public Transfer[] getPendingRequests() {
        Transfer[] transfers = null;
        try {

            transfers = restTemplate.exchange(API_BASE_URL + "/transfers/user/" + currentUser.getUser().getId() + "/pending", HttpMethod.GET, authEnt(), Transfer[].class).getBody();

        } catch (RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }
        return transfers;
    }

    //Authenticated user can send transfer to another registered user.
    public void sendBucks() {
        User[] users = null;
                Transfer transfer = new Transfer();
        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, authEnt(),
            User[].class).getBody();
                System.out.println("-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "ID\t\tName\r\n" +
                    "-------------------------------------------");
            for (User i : users) {
                if (i.getId() != currentUser.getUser().getId()) {
                    System.out.println(i.getId() + "\t\t" + i.getUsername());
                }
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Enter the ID of the user you would like to send money (0 to cancel): ");
            transfer.setAccountTo(Integer.parseInt(scanner.nextLine()));

            transfer.setAccountFrom(currentUser.getUser().getId());
            System.out.println(transfer.getAccountTo() + transfer.getAccountFrom() + " will be your transfer ID.");
            if (transfer.getAccountTo() != 0) {
                System.out.print("Enter the amount you would like to send: ");
                try {
                    double amount = Double.parseDouble(scanner.nextLine());
                    BigDecimal prepAmount = new BigDecimal(amount);
                    transfer.setAmount(prepAmount);
                } catch (NumberFormatException e) {
                    System.out.println("Error when entering amount. Please enter a valid amount.");
                }
                String output = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST,
                makeTransferEntity(transfer), String.class).getBody();
                    System.out.println(output);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public Transfer getOneTransfer(long transferID) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/" + transferID, HttpMethod.GET, authEnt(), Transfer.class);
            transfer = response.getBody();
            if (transfer != null) {
                System.out.println("Transfer information:");
            }
            System.out.println("Id: " + transfer.getTransferId());
            System.out.println("From: " + transfer.getUsernameFrom());
            System.out.println("To: " + transfer.getUsernameTo());
            System.out.println("Type: " + transfer.getTransferType());
            System.out.println("Status: " + transfer.getTransferStatus());
            System.out.println("Amount: " + transfer.getAmount());
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Issue fetching transfer");
        }
        return transfer;
    }

    private HttpEntity<Void> authEnt() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }





}


