package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.repository.AccountRepository;
import com.blankerdog.carService.services.AccountService;
import com.blankerdog.carService.services.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Account create(Account account) {
        if (account != null){
            account.setRole(roleService.readByName("USER"));
            return accountRepository.save(account);
        }
        throw new NullPointerException("Account cannot be 'null'");
    }

    @Override
    public Account readById(long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Account with id " + id + " not found"));
    }

    @Override
    public Account update(Account account, long id) {
        Account existingAccount = readById(id);
        existingAccount.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(existingAccount);
    }

    @Override
    public void delete(long id) {
        accountRepository.delete(readById(id));
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account getByLogin(String login) {
        return accountRepository.getAccountByLogin(login).orElseThrow(
                () -> new UsernameNotFoundException(String.format("Account %s is not found", login)));
    }

    @Override
    public boolean existsByLogin(String login) {
        return accountRepository.existsAccountByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return getByLogin(login);
    }
}
