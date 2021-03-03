package com.example.final_project.service;

import com.example.final_project.model.User;
import com.example.final_project.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
	User save(User user);
	void update(String newPassword, Long id);
	boolean updatePassword(String lastPassword, String newPassword, String email);
	User findByUsername(String username);
	Long findIdByUsername(String username);
}
