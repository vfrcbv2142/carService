package com.blankerdog.carService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Relation(itemRelation = "Note", collectionRelation = "Notes")
public class NoteDto {
    private Long id;

    @NotBlank(message = "Note can't be empty")
    private String text;

    @NotNull
    private Long orderId;
}
