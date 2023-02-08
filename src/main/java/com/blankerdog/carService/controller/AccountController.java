package com.blankerdog.carService.controller;

import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CollectionModel<EntityModel<Account>>> getAll(){
        List<EntityModel<Account>> accounts = accountService.getAll().stream()
                .map(x -> toModel(x))
                .toList();
        return new ResponseEntity<>(CollectionModel.of(accounts), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.principal.id == #id")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Account>> getById(@PathVariable long id){
        return new ResponseEntity<>(toModel(accountService.readById(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<EntityModel<Account>> postAccount(@RequestBody Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return new ResponseEntity<>(toModel(accountService.create(account)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.principal.id == #id")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Account>> putById(@RequestBody Account account, @PathVariable long id){
        return new ResponseEntity<>(toModel(accountService.update(account, id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.principal.id == #id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id){
        accountService.delete(id);
        return new ResponseEntity<String>("Account deleted successfully", HttpStatus.OK);
    }

    private static EntityModel<Account> toModel(Account account){
        return EntityModel.of(account,
                linkTo(methodOn(AccountController.class).getById(account.getId())).withSelfRel(),
                linkTo(methodOn(AccountController.class).getAll()).withRel("accounts"));
    }
}
