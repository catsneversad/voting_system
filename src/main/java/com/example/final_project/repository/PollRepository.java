package com.example.final_project.repository;

import com.example.final_project.model.Answer;
import com.example.final_project.model.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    Poll findPollById(Long id);
    @Query(value = "SELECT c FROM Poll c WHERE c.author_id = :user_id")
    List<Poll> findSendAuthorId(@Param("user_id") Long id);
    @Override
    List<Poll> findAll();
}
