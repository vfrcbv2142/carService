package com.blankerdog.carService.dto;


import com.blankerdog.carService.model.Note;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Relation(itemRelation = "order", collectionRelation = "orders")
public class OrderDto {
    private Long id;

    @NotBlank(message = "Order name can't be empty")
    private String name;

    @NotNull
    private Long accountId;

    @NotNull
    private Long clientId;

    @NotNull
    private LocalDate creationDate;

    private List<Long> employeesIds;

    private List<Long> itemsIds;

    private List<Long> notesIds;
}
