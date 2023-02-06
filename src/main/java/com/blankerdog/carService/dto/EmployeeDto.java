package com.blankerdog.carService.dto;

import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Order;
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
@Relation(itemRelation = "Employee", collectionRelation = "Employees")
public class EmployeeDto {
    private Long id;

    @NotBlank(message = "'firstName' can't be empty")
    private String firstName;

    private String lastName;

    private String phoneNumber;

    @Email
    private String email;

    @NotBlank(message = "'position' can't be empty")
    private String position;

    @NotNull
    private Long accountId;

    private List<Long> ordersIds;
}
