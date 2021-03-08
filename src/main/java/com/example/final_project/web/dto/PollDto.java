package com.example.final_project.web.dto;

public class PollDto {
    private Long id;
    private String title;
    private int rate;
    private String authorUsername;

    public PollDto(Long id, String title, String authorUsername) {
        this.id = id;
        this.title = title;
        this.authorUsername = authorUsername;
    }

    public PollDto(Long id, String title, String authorUsername, int rate) {
        this.id = id;
        this.title = title;
        this.authorUsername = authorUsername;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
