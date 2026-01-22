package com.example.ex03;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    public NoteController() {
        // Add first note
        notes.add(new Note(
                idCounter.getAndIncrement(),
                "Shopping list",
                "Milk, bread, eggs, butter",
                "Jan",
                new ArrayList<>(List.of(new Comment(1L, "Comment 1"), new Comment(2L, "Comment 2"))) // Empty comment list
        ));

        // Add second note
        notes.add(new Note(
                idCounter.getAndIncrement(),
                "Learning Spring",
                "Master @RestController and @RequestParam",
                "Anna",
                new ArrayList<>()));

        // Add third note
        notes.add(new Note(
                idCounter.getAndIncrement(),
                "Workout plan",
                "Monday: chest, Wednesday: back, Friday: legs",
                "Piotr",
                new ArrayList<>()));
    }

    @GetMapping()
    public List<Note> getAll(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Note> filderedNotes = this.notes.stream()
                .filter(note -> author == null || note.author.equalsIgnoreCase(author))
                .filter(note -> search == null || note.title.toLowerCase().contains(search.toLowerCase()))
                .toList();
        int start = page * size;
        int end = Math.min(start + size, filderedNotes.size());

        return filderedNotes.subList(start, end);
    };

    @GetMapping("/{id}")
    public Note getNote(@PathVariable Long id) {
        return this.notes.stream()
                .filter(note -> note.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Note not found" + id));

    };

    @GetMapping("/{id}/comments/{cid}")
    public Comment getComment(@PathVariable Long id, @PathVariable Long cid) {
        return this.getNote(id).getComments().stream()
                .filter(comment -> comment.id.equals(cid))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Note not found" + id));

    };

    @PostMapping()
    public List<Note> createNote(@RequestBody Note note) {
        Note newNote = new Note(
                idCounter.getAndIncrement(),
                note.title,
                note.content,
                note.author, note.comments);
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
                            note.content,
                            note.author,
                            note.comments);
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
