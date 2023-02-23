package com.blankerdog.carService.dto;

import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Order;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Relation(itemRelation = "client", collectionRelation = "clients")
public class ClientDto {
    private Long id;

    @NotBlank(message = "'firstName' can't be empty")
    private String firstName;

    private String lastName;

    private String phoneNumber;

    @Email
    private String email;

    @NotNull
    private Long accountId;

    private List<Long> ordersIds;
}
