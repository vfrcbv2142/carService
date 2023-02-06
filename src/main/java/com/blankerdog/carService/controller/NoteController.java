package com.blankerdog.carService.controller;

import com.blankerdog.carService.dto.NoteDto;
import com.blankerdog.carService.dto.NoteTransformer;
import com.blankerdog.carService.model.Note;
import com.blankerdog.carService.services.NoteService;
import com.blankerdog.carService.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    @Autowired
    NoteService noteService;
    @Autowired
    OrderService orderService;

    @PostMapping()
    public ResponseEntity<EntityModel<NoteDto>> postNote(@RequestBody NoteDto noteDto){
        Note note = NoteTransformer.convertToEntity(
                noteDto,
                orderService.readById(noteDto.getOrderId())
        );
        return new ResponseEntity<>(toModel(NoteTransformer.convertToDto(noteService.create(note))), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<NoteDto>> putNote(@RequestBody NoteDto noteDto, @PathVariable long id){
        Note note = NoteTransformer.convertToEntity(
                noteDto,
                null
        );
        return new ResponseEntity<>(toModel(NoteTransformer.convertToDto(noteService.update(note, id))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable long id){
        noteService.delete(id);
        return new ResponseEntity<>("Note successfully deleted", HttpStatus.OK);
    }

    private static EntityModel<NoteDto> toModel(NoteDto noteDto){
        return EntityModel.of(noteDto,
                linkTo(methodOn(NoteController.class).deleteNote(noteDto.getId())).withSelfRel());
    }
}
