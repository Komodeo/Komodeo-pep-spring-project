package com.example.controller;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import com.example.entity.*;
import com.example.repository.AccountRepository;
import com.example.service.*;

import javafx.application.Application;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    /*
     * ## 1: Our API should be able to process new User registrations.
     * 
     * As a user, I should be able to create a new Account on the endpoint POST
     * localhost:8080/register. The body will contain a representation of a JSON
     * Account, but will not contain an accountId.
     * 
     * - The registration will be successful if and only if the username is not
     * blank, the password is at least 4 characters long, and an Account with that
     * username does not already exist. If all these conditions are met, the
     * response body should contain a JSON of the Account, including its accountId.
     * The response status should be 200 OK, which is the default. The new account
     * should be persisted to the database.
     * - If the registration is not successful due to a duplicate username, the
     * response status should be 409. (Conflict)
     * - If the registration is not successful for some other reason, the response
     * status should be 400. (Client error)
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Account> postAccount(@RequestBody Account account) {
        if (account.getUsername() == "" || account.getPassword().length() < 4) {
            return ResponseEntity.status(400).body(null);
        }
        if (accountService.findByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(409).body(null);
        }
        return ResponseEntity.status(200).body(accountService.save(account));
    }
}
