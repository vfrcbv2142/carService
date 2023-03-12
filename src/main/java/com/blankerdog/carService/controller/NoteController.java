package com.blankerdog.carService.controller;

import com.blankerdog.carService.dto.NoteDto;
import com.blankerdog.carService.dto.NoteTransformer;
import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Note;
import com.blankerdog.carService.services.NoteService;
import com.blankerdog.carService.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    @Autowired
    NoteService noteService;
    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<NoteDto>> getById(@PathVariable long id){
        return new ResponseEntity<>(toModel(NoteTransformer.convertToDto(noteService.readById(id))), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @orderController.canAccessOrder(#orderId)")
    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<NoteDto>>> getAllByOrderId(@RequestParam long orderId){
        List<EntityModel<NoteDto>> notesDto = noteService.findAllByOrderId(orderId).stream()
                .map(x -> NoteTransformer.convertToDto(x))
                .map(x -> toModel(x))
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(notesDto), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<EntityModel<NoteDto>> postNote(@RequestBody NoteDto noteDto){
        Note note = NoteTransformer.convertToEntity(
                noteDto,
                orderService.readById(noteDto.getOrderId())
        );
        return new ResponseEntity<>(toModel(NoteTransformer.convertToDto(noteService.create(note))), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @noteController.canAccessNote(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<NoteDto>> putNote(@RequestBody NoteDto noteDto, @PathVariable long id){
        Note note = NoteTransformer.convertToEntity(
                noteDto,
                null
        );
        return new ResponseEntity<>(toModel(NoteTransformer.convertToDto(noteService.update(note, id))), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @noteController.canAccessNote(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable long id){
        noteService.delete(id);
        return new ResponseEntity<>("Note successfully deleted", HttpStatus.OK);
    }

    private static EntityModel<NoteDto> toModel(NoteDto noteDto){
        return EntityModel.of(noteDto,
                linkTo(methodOn(NoteController.class).deleteNote(noteDto.getId())).withSelfRel());
    }

    public boolean canAccessNote(long noteId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account =  (Account) authentication.getPrincipal();

        List<Long> notesId = orderService.findAllByIds(orderService.findAllByAccountId(account.getId()).stream()
                        .map(x -> x.getId()).collect(Collectors.toList())).stream()
                .flatMap(y -> y.getNotes().stream()
                        .map(z -> z.getId())
                ).collect(Collectors.toList());
        return notesId.stream().anyMatch(x -> x.equals(noteId));
    }

}
