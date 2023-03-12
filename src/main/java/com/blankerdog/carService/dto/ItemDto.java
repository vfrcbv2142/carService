package com.blankerdog.carService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Relation(itemRelation = "item", collectionRelation = "items")
public class ItemDto {
    private Long id;

    @NotBlank(message = "Item name can't be empty")
    private String name;

    @NotNull
    private Long orderId;

    @NotNull
    private Map<String, Float> itemPrice;
}
