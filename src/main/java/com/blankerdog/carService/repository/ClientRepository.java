package com.blankerdog.carService.repository;

import com.blankerdog.carService.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> getClientsByAccountId(long accountId);
}
