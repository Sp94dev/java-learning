package com.example.ex04;

import java.util.List;

public record Note(
                Long id,
                String title,
                String content,
                String author,
                List<Comment> comments) {
}
