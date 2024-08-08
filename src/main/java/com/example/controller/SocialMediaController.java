package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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

    /*
     * ## 4: Our API should be able to retrieve all messages.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET
     * localhost:8080/messages.
     * 
     * - The response body should contain a JSON representation of a list containing
     * all messages retrieved from the database. It is expected for the list to
     * simply be empty if there are no messages. The response status should always
     * be 200, which is the default.
     */
    @GetMapping(value = "/messages")
    public List<Message> findAllMessages() {
        return messageService.findAll();
    }

    /*
     * ## 5: Our API should be able to retrieve a message by its ID.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET
     * localhost:8080/messages/{messageId}.
     * 
     * - The response body should contain a JSON representation of the message
     * identified by the messageId. It is expected for the response body to simply
     * be empty if there is no such message. The response status should always be
     * 200, which is the default.
     */
    @GetMapping(value = "messages/{messageId}")
    public Message findMessageById(@PathVariable int messageId) {
        return messageService.findByMessageId(messageId);
    }

    /*
     * ## 6: Our API should be able to delete a message identified by a message ID.
     * 
     * As a User, I should be able to submit a DELETE request on the endpoint DELETE
     * localhost:8080/messages/{messageId}.
     * 
     * - The deletion of an existing message should remove an existing message from
     * the database. If the message existed, the response body should contain the
     * number of rows updated (1). The response status should be 200, which is the
     * default.
     * - If the message did not exist, the response status should be 200, but the
     * response body should be empty. This is because the DELETE verb is intended to
     * be idempotent, ie, multiple calls to the DELETE endpoint should respond with
     * the same type of response.
     */
    @Transactional
    @DeleteMapping(value = "messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        try {
            Message messageToDelete = messageService.findByMessageId(messageId);
            if (messageToDelete != null) {
                return ResponseEntity.status(200).body(messageService.deleteByMessageId(messageId));
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return ResponseEntity.status(200).body(null);
    }
}
