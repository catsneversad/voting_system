package com.example.final_project.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "polls")
public class Poll implements Serializable {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(name="title")
    private String title;
    @Column(name="author_id")
    private Long author_id;

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Collection<Answer> answers;

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

    public Collection<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author_id=" + author_id +
                ", answers=" + answers +
                '}';
    }
}
