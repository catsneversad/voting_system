package com.example.final_project.service;

import com.example.final_project.model.Poll;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PollService {
    Poll save(Poll poll);
    void deleteById(long id);
    Poll getPollById(Long id);
    List<Poll> getPollsByAuthorId(Long authorId);
    List<Poll> getAllPolls();
    int getRateOfPoll(Poll poll);
}
