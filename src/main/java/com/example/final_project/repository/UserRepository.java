package com.example.final_project.repository;

import com.example.final_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
	User findUserById(long id);
}
