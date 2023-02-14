package com.blankerdog.carService.controller;


import com.blankerdog.carService.exception.TokenRefreshException;
import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.RefreshToken;
import com.blankerdog.carService.payload.request.LoginRequest;
import com.blankerdog.carService.payload.request.SignupRequest;
import com.blankerdog.carService.payload.request.TokenRefreshRequest;
import com.blankerdog.carService.payload.response.JwtResponse;
import com.blankerdog.carService.payload.response.MessageResponse;
import com.blankerdog.carService.payload.response.TokenRefreshResponse;
import com.blankerdog.carService.security.jwt.JwtUtils;
import com.blankerdog.carService.services.AccountService;
import com.blankerdog.carService.services.RefreshTokenService;
import com.blankerdog.carService.services.RoleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/auth")
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
    @Autowired
    RefreshTokenService refreshTokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(userToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Account account = (Account) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(account);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(account.getId());

        List<String> roles = account.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), account.getId(),
                account.getUsername(), account.getEmail(), roles));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getAccount)
                .map(account -> {
                    String token = jwtUtils.generateTokenFromUsername(account.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
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
                signUpRequest.getEmail(),
                null, null, null, null, null,
                roleService.readByName("USER"));
        accountService.create(account);
        logger.info("Account registered successfully!");
        return new ResponseEntity<>(new MessageResponse("Account registered successfully!"), HttpStatus.CREATED);
    }

}
