package com.example.final_project.service;


import com.example.final_project.model.Answer;
import com.example.final_project.model.Vote;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VoteService {
    Vote save(Vote vote);
    List<Vote> getAllVotesByUserId(Long id);
    Long getAnswerIdByUserIdAndPollId(Long userId, Long pollId);
    int getRateOfAnswer (Long answerId);
    boolean isThisPollAlreadyVotedByUser(Long userId, Long pollId);
}
