package com.example.final_project.model;

import javax.persistence.*;

@Entity
@Table(name = "poll")
public class Poll {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(name="title")
    private String title;
    @Column(name="author_id")
    private Long author_id;

    public Poll() {
    }

    public Poll(String title, long author_id) {
        this.title = title;
        this.author_id = author_id;
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

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }
}
