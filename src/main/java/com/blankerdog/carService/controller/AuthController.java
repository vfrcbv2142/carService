package com.blankerdog.carService.controller;


import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.payload.request.LoginRequest;
import com.blankerdog.carService.payload.request.SignupRequest;
import com.blankerdog.carService.payload.response.JwtResponse;
import com.blankerdog.carService.payload.response.MessageResponse;
import com.blankerdog.carService.security.jwt.JwtUtils;
import com.blankerdog.carService.services.AccountService;
import com.blankerdog.carService.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AccountService accountService;
    @Autowired
    RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(userToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Account account = (Account) authentication.getPrincipal();
        List<String> roles = account.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                account.getId(),
                account.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (accountService.existsByLogin(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Login is already taken!"));
        }

        Account account = new Account(null,
                signUpRequest.getLogin(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                null, null, null, null,
                roleService.readByName("USER"));
        accountService.create(account);

        return ResponseEntity.ok(new MessageResponse("Account registered successfully!"));
    }

}
