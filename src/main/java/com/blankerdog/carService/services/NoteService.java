package com.blankerdog.carService.services;

import com.blankerdog.carService.model.Note;

import java.util.List;

public interface NoteService {
    Note create(Note note);
    Note readById(long id);
    Note update(Note note, long id);
    void delete(long id);
    List<Note> getAll();
}
