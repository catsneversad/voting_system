package com.example.final_project.service;

import com.example.final_project.model.Vote;
import com.example.final_project.repository.UserRepository;
import com.example.final_project.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    private VoteRepository voteRepository;

    public VoteServiceImpl(VoteRepository voteRepository) {
        super();
        this.voteRepository = voteRepository;
    }

    @Override
    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public List<Vote> getAllVotesByUserId(Long id) {
        return voteRepository.getAllByUserId(id);
    }

    @Override
    public Long getAnswerIdByUserIdAndPollId(Long userId, Long pollId) {
        return voteRepository.getVoteByUserIdAndPollId(userId, pollId).getAnswerId();
    }

    @Override
    public int getRateOfAnswer(Long answerId) {
        return voteRepository.rateOfAnswer(answerId);
    }

    @Override
    public boolean isThisPollAlreadyVotedByUser(Long userId, Long pollId) {
        return (voteRepository.isVotedByUser(userId, pollId) > 0);
    }

    @Override
    public boolean isThisUserSelectedThisAnswer(Long id, Long id1) {
        return (voteRepository.isThisAnswerVotedByUser(id, id1) > 0);
    }
}
