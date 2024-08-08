package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    // return account by account id
    public Account findByAccountId(int accountId) {
        return accountRepository.findByAccountId(accountId);
    }
    
    // return account by username
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    // save and return account
    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
