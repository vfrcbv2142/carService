package com.blankerdog.carService.services;


import com.blankerdog.carService.model.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends UserDetailsService {

    Account create(Account account);
    Account readById(long id);
    Account update(Account account, long id);
    void delete(long id);
    List<Account> getAll();
    Account getByLogin(String login);
    boolean existsByLogin(String login);
}
