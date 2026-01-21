package com.example.ex02;

public class Note {
    Long id;
    String title;
    String content;

    Note() {} 
    Note(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getContent() { return this.content; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
}
