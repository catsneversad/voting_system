package com.example.final_project.service;

import com.example.final_project.model.Poll;
import com.example.final_project.repository.AnswerRepository;
import com.example.final_project.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollServiceImpl implements PollService {
    private PollRepository pollRepository;
    @Autowired
    public PollServiceImpl (PollRepository pollRepository) {
        super();
        this.pollRepository = pollRepository;
    }

    @Override
    public Poll save(Poll poll) {
        return pollRepository.save(poll);
    }

    @Override
    public void deleteById(long id) {
        pollRepository.deleteById(id);
    }

    @Override
    public Poll getPollById(Long id) {
        return pollRepository.findPollById(id);
    }

    @Override
    public List<Poll> getPollsByAuthorId(Long authorId) {
        return pollRepository.findSendAuthorId(authorId);
    }

    @Override
    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }
}
