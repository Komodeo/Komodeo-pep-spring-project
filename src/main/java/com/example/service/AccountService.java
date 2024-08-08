package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    
    // return account by username
    public Account findAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }
}
