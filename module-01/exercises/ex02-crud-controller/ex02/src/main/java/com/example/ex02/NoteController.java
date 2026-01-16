package com.example.ex02;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/notes")
public class NoteController {
    private final List<Note> notes = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping()
    public List<Note> getAll() {
        return this.notes;
    };

    @GetMapping("/{id}")
    public Note getNote(@PathVariable Long id) {
        return this.notes.stream()
                .filter(note -> note.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Note not found" + id));

    };

    @PostMapping()
    public List<Note> createNote(@RequestBody Note note) {
        Note newNote = new Note(
                idCounter.getAndIncrement(),
                note.title,
                note.content);
        this.notes.add(newNote);
        return this.notes;
    };

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
        return this.notes.stream()
                .filter(oldNote -> oldNote.id.equals(id))
                .findFirst()
                .map(oldNote -> {
                    Note newNote = new Note(
                            id,
                            note.title,
                            note.content);
                    int index = notes.indexOf(oldNote);
                    notes.set(index, newNote);
                    return newNote;
                }).orElseThrow(() -> new NoSuchElementException("Note not found " + id));
    };

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        boolean removed = this.notes.removeIf(note -> note.id.equals(id));
        if (!removed) {
            throw new NoSuchElementException("Note not found " + id);
        }
    }

}
