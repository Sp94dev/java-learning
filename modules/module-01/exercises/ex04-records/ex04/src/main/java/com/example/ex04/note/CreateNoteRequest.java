package com.example.ex04.note;

public record CreateNoteRequest(
        String title,
        String content,
        String author) {
}