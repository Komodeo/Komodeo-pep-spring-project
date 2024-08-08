package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.*;
import com.example.service.*;

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
    // map services
    @Autowired
    AccountService accountService;
    @Autowired
    MessageService messageService;

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
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        if (account.getUsername() == "" || account.getPassword().length() < 4) {
            return ResponseEntity.status(400).body(account);
        }
        if (accountService.findByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(409).body(account);
        }
        return ResponseEntity.status(200).body(accountService.save(account));
    }

    /*
     * ## 2: Our API should be able to process User logins.
     * 
     * As a user, I should be able to verify my login on the endpoint POST
     * localhost:8080/login. The request body will contain a JSON representation of
     * an Account.
     * 
     * - The login will be successful if and only if the username and password
     * provided in the request body JSON match a real account existing on the
     * database. If successful, the response body should contain a JSON of the
     * account in the response body, including its accountId. The response status
     * should be 200 OK, which is the default.
     * - If the login is not successful, the response status should be 401.
     * (Unauthorized)
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account) {
        try {
            Account loggedAccount = accountService.findByUsername(account.getUsername());
            if (account.getUsername() != null && loggedAccount.getUsername().equals(account.getUsername())
                    && loggedAccount.getPassword().equals(account.getPassword())) {
                return ResponseEntity.status(200).body(loggedAccount);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return ResponseEntity.status(401).body(account);
    }

    /*
     * ## 3: Our API should be able to process the creation of new messages.
     * 
     * As a user, I should be able to submit a new post on the endpoint POST
     * localhost:8080/messages. The request body will contain a JSON representation
     * of a message, which should be persisted to the database, but will not contain
     * a messageId.
     * 
     * - The creation of the message will be successful if and only if the
     * messageText is not blank, is not over 255 characters, and postedBy refers to
     * a real, existing user. If successful, the response body should contain a JSON
     * of the message, including its messageId. The response status should be 200,
     * which is the default. The new message should be persisted to the database.
     * - If the creation of the message is not successful, the response status
     * should be 400. (Client error)
     */
    @PostMapping(value = "/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            Account postingAccount = accountService.findByAccountId(message.getPostedBy());
            if (postingAccount != null && message.getMessageText() != "" && message.getMessageText().length() <= 255) {
                return ResponseEntity.status(200).body(messageService.save(message));
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return ResponseEntity.status(400).body(message);
    }
}
