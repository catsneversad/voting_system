package com.example.final_project.model;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(name="value")
    private String value;
    @Column(name="rate")
    private Long rate;
    @Column(name="poll_id")
    private Long poll_id;

    public Answer() {
    }

    public Answer(String value, Long rate, Long poll_id) {
        this.value = value;
        this.rate = rate;
        this.poll_id = poll_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public Long getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(Long poll_id) {
        this.poll_id = poll_id;
    }
}
