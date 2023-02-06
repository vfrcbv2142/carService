package com.blankerdog.carService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Relation(itemRelation = "Price", collectionRelation = "Prices")
public class PriceDto {
    private Long id;

    private Integer painting;

    private Integer preparingAluminum;

    private Integer preparingPlastic;

    private Integer preparingIron;

    private Integer soldering;

    private Integer disassembling;

    private Integer straightening;

    private Long accountId;
}
