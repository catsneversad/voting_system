package com.example.final_project.service;

import com.example.final_project.model.Answer;
import com.example.final_project.model.Poll;
import com.example.final_project.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private AnswerRepository answerRepository;
    @Autowired
    public AnswerServiceImpl (AnswerRepository answerRepository) {
        super();
        this.answerRepository = answerRepository;
    }

    @Override
    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public Answer getAnswerById(Long id) {
        return answerRepository.findAnswerById(id);
    }

    @Override
    public List<Answer> getAnswersByPollId(Poll poll) {
        return answerRepository.findAllByPoll(poll);
    }
}
