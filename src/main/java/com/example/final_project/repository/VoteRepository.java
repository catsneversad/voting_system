package com.example.final_project.repository;

import com.example.final_project.model.Poll;
import com.example.final_project.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> getAllByUserId(long userId);
    @Query(value = "SELECT count (c) FROM Vote c WHERE c.userId = :user_id and c.pollId = :poll_id")
    int isVotedByUser(@Param("user_id") Long user_id, @Param("poll_id") Long poll_id);

    @Query(value = "SELECT count (c) FROM Vote c WHERE c.answerId = :answer_id")
    int rateOfAnswer(@Param("answer_id") Long answer_id);

    @Query(value = "SELECT c FROM Vote c WHERE c.userId = :user_id and c.pollId = :poll_id")
    Vote getVoteByUserIdAndPollId(@Param("user_id") Long user_id, @Param("poll_id") Long poll_id);
}
