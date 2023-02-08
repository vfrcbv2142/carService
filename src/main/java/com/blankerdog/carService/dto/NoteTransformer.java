package com.blankerdog.carService.dto;

import com.blankerdog.carService.model.Note;
import com.blankerdog.carService.model.Order;

public class NoteTransformer {

    public static NoteDto convertToDto(Note note){
        return new NoteDto(
                note.getId(),
                note.getText(),
                note.getOrder().getId()
        );
    }

    public static Note convertToEntity(NoteDto noteDto, Order order){
        return new Note(
                noteDto.getId(),
                noteDto.getText(),
                order
        );
    }
}
