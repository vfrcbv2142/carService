package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.exception.TokenRefreshException;
import com.blankerdog.carService.model.RefreshToken;
import com.blankerdog.carService.repository.AccountRepository;
import com.blankerdog.carService.repository.RefreshTokenRepository;
import com.blankerdog.carService.services.AccountService;
import com.blankerdog.carService.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AccountService accountService;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long accountId) {
        RefreshToken existingRefreshToken = accountService.readById(accountId).getRefreshToken();
        if (existingRefreshToken != null){
            try {
                return verifyExpiration(existingRefreshToken);
            } catch (TokenRefreshException ex){
                refreshTokenRepository.delete(existingRefreshToken);
            }
        }
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setAccount(accountService.readById(accountId));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByAccountId(Long id) {
        return refreshTokenRepository.deleteByAccount(accountService.readById(id));
    }
}
