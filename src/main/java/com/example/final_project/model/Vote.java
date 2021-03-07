package com.example.final_project.model;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private Long pollId;
    private Long answerId;
    private Long userId;

    public Vote() {
    }

    public Vote(Long pollId, Long answerId, Long userId) {
        this.pollId = pollId;
        this.answerId = answerId;
        this.userId = userId;
    }

    public Vote(Long id, Long pollId, Long answerId, Long userId) {
        this.id = id;
        this.pollId = pollId;
        this.answerId = answerId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
