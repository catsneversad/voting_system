package com.example.final_project.service;

import com.example.final_project.model.Role;
import com.example.final_project.model.User;
import com.example.final_project.repository.UserRepository;
import com.example.final_project.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public void update(String newPassword, Long id) {
		User curUser = userRepository.findUserById(id);
		curUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		userRepository.save(curUser);
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));

        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<User> nonAdmins = new ArrayList<>();
        for (User curUser: allUsers) {
            if (!isAdmin(curUser)) {
                nonAdmins.add(curUser);
            }
        }
        return nonAdmins;
    }

    @Override
    public List<User> getAllAdmins() {
        List<User> allUsers = userRepository.findAll();
        List<User> admins = new ArrayList<>();
        for (User curUser: allUsers) {
            if (isAdmin(curUser)) {
                admins.add(curUser);
            }
        }
        return admins;
    }

    @Override
    public boolean isAdmin (User user) {
        for (Role userRoles: user.getRoles()) {
            if (userRoles.getName().equals("ROLE_ADMIN"))
                return true;
        }
        return false;
    }

    @Override
    public boolean updatePassword(String lastPassword, String newPassword, String email) {
        User curUser = findByUsername(email);

        boolean result = passwordEncoder.matches(lastPassword, curUser.getPassword());
        if (!result) {
            return false;
        }
        update(newPassword, curUser.getId());
        return true;
    }

    @Override
    public Long findIdByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return user.getId();

    }

    @Override
    public void makeAdmin(User editUser) {
        List<Role> userRoles = (List<Role>) editUser.getRoles();
        userRoles.add(new Role("ROLE_ADMIN"));
        editUser.setRoles(userRoles);
        userRepository.save(editUser);
    }

    @Override
    public void makeUser(User editUser) {
        List<Role> userRoles = (List<Role>) editUser.getRoles();
        userRoles.clear();
        userRoles.add(new Role("ROLE_USER"));
        editUser.setRoles(userRoles);
        userRepository.save(editUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }


    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {

        User user = (User) userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return user;
    }

    @Override
    public User findUserById(Long id) {
        User user = userRepository.findUserById(id);
        return user;
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}