package com.example.ex04;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.ex04.note.NoteResponse;
import com.example.ex04.note.CreateNoteRequest;
import com.example.ex04.note.UpdateNoteRequest;
import com.example.ex04.comment.CommentResponse;

@RestController
@RequestMapping("api/notes")
public class NoteController {
    private final List<Note> notes = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public NoteController() {
        notes.add(new Note(
                idCounter.getAndIncrement(),
                "Shopping list",
                "Milk, bread, eggs, butter",
                "Jan",
                new ArrayList<>(List.of(new Comment(1L, "Comment 1"), new Comment(2L, "Comment 2")))));

        notes.add(new Note(
                idCounter.getAndIncrement(),
                "Learning Spring",
                "Master @RestController and @RequestParam",
                "Anna",
                new ArrayList<>()));

        notes.add(new Note(
                idCounter.getAndIncrement(),
                "Workout plan",
                "Monday: chest, Wednesday: back, Friday: legs",
                "Piotr",
                new ArrayList<>()));
    }

    @GetMapping()
    public List<NoteResponse> getAll(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Note> filteredNotes = this.notes.stream()
                .filter(note -> author == null || note.author().equalsIgnoreCase(author))
                .filter(note -> search == null || note.title().toLowerCase().contains(search.toLowerCase()))
                .toList();

        int start = Math.min(page * size, filteredNotes.size());
        int end = Math.min(start + size, filteredNotes.size());

        return filteredNotes.subList(start, end).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public NoteResponse getNote(@PathVariable Long id) {
        return mapToResponse(findNoteById(id));
    }

    @GetMapping("/{id}/comments/{cid}")
    public CommentResponse getComment(@PathVariable Long id, @PathVariable Long cid) {
        Note note = findNoteById(id); // Pobieramy pełną notatkę (z komentarzami)
        return note.comments().stream()
                .filter(comment -> comment.id().equals(cid))
                .map(comment -> new CommentResponse(comment.id(), comment.text()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Comment not found " + cid));
    }

    @PostMapping()
    public List<NoteResponse> createNote(@RequestBody CreateNoteRequest request) {
        Note newNote = new Note(
                idCounter.getAndIncrement(),
                request.title(),
                request.content(),
                request.author(),
                new ArrayList<>() // Nowa notatka nie ma komentarzy na starcie
        );
        this.notes.add(newNote);

        return this.notes.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public NoteResponse updateNote(@PathVariable Long id, @RequestBody UpdateNoteRequest request) {
        Note oldNote = findNoteById(id);

        Note newNote = new Note(
                id,
                request.title(),
                request.content(),
                oldNote.author(), // Autor zostaje stary
                oldNote.comments() // Komentarze zostają stare
        );

        int index = notes.indexOf(oldNote);
        notes.set(index, newNote);

        return mapToResponse(newNote);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        Note note = findNoteById(id);
        this.notes.remove(note);
    }

    // Helpery, które ułatwiają życie i czyszczą kod kontrolera

    private Note findNoteById(Long id) {
        return this.notes.stream()
                .filter(note -> note.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Note not found " + id));
    }

    private NoteResponse mapToResponse(Note note) {
        return new NoteResponse(
                note.id(),
                note.title(),
                note.content(),
                note.author());
    }
}