package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private RestTemplate restTemplate = new RestTemplate();
    private String API_BASE_URL;
    private AuthenticatedUser currentUser;

    public AccountService(String API_BASE_URL, AuthenticatedUser currentUser){
        this.API_BASE_URL = API_BASE_URL;
        this.currentUser = currentUser;
    }

    //Authenticated user can retrieve their own balance and is limited to only viewing their own
    //balance information.
    public BigDecimal getBalance(){
        BigDecimal balance = new BigDecimal(0);
        try {
            balance = restTemplate.exchange(API_BASE_URL + "balance/" +
            currentUser.getUser().getId(), HttpMethod.GET, authEnt(), BigDecimal.class).getBody();
        } catch (RestClientException e) {
            System.out.println("Unable to retrieve balance.");
        }
        return balance;
    }
    private HttpEntity<Void> authEnt(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
