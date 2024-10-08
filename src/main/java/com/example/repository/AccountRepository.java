package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // find account by account id
    Account findByAccountId(int accountId);

    // find account by username
    Account findByUsername(String username);
}
