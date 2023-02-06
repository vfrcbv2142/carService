package com.blankerdog.carService.repository;

import com.blankerdog.carService.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
