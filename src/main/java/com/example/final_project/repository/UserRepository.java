package com.example.final_project.repository;

import com.example.final_project.model.Answer;
import com.example.final_project.model.Poll;
import com.example.final_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
	User findUserById(long id);
	@Override
	List<User> findAll();
}
