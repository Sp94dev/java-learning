package com.example.ex04.note;

public record NoteResponse(
        Long id,
        String title,
        String content,
        String author) {
}