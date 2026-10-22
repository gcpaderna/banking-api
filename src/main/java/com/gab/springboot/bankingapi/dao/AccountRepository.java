package com.gab.springboot.bankingapi.dao;

import com.gab.springboot.bankingapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
