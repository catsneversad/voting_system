package com.example.final_project.repository;

import com.example.final_project.model.Answer;
import com.example.final_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Answer findAnswerById(Long id);
    @Query(value = "SELECT c FROM Answer c WHERE c.poll_id = :id")
    List<Answer> findAnswersByPollId(@Param("id") Long id);
}
