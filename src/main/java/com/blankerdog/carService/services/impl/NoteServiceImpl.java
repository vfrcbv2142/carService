package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.model.Note;
import com.blankerdog.carService.model.Order;
import com.blankerdog.carService.repository.NoteRepository;
import com.blankerdog.carService.services.NoteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteRepository noteRepository;


    @Override
    public Note create(Note note) {
        if (note != null){
            return noteRepository.save(note);
        }
        throw new NullPointerException("Note cannot be 'null'");
    }

    @Override
    public Note readById(long id) {
        return noteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Note with id " + id + " not found"));
    }

    @Override
    public Note update(Note note, long id) {
        Note existingNote = readById(id);
        existingNote.setText(note.getText());
        return noteRepository.save(existingNote);
    }

    @Override
    public void delete(long id) {
        noteRepository.delete(readById(id));
    }

    @Override
    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    @Override
    public List<Note> findAllByOrderId(long orderId){
        return noteRepository.findAllByOrderId(orderId);
    }

}
