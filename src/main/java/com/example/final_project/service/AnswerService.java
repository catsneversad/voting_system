package com.example.final_project.service;


import com.example.final_project.model.Answer;
import com.example.final_project.model.Poll;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService {
    Answer save(Answer answer);
    Answer getAnswerById(Long id);
    List<Answer> getAnswersByPoll(Poll poll);
    void delete(Answer answer);
}
