package com.example.final_project.web;


import com.example.final_project.model.Poll;
import com.example.final_project.model.Role;
import com.example.final_project.model.User;
import com.example.final_project.service.UserService;
import com.example.final_project.web.dto.UpdatePasswordDto;
import com.example.final_project.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        super();
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());
        if (userService.isAdmin(user))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isAdmin", false);
        model.addAttribute("currentUser", user);
        return "profile";
    }


    @GetMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(@RequestParam(name = "email") String email,
                                 @RequestParam(name = "currentPassword") String currentPassword,
                                 @RequestParam(name = "newPassword") String newPassword,
                                 @RequestParam(name = "repeatNewPassword") String repeatPassword) {

        User user = userService.findByUsername(email);
        System.out.println(currentPassword + ' ' + newPassword + ' ' + repeatPassword);

        if (!newPassword.equals(repeatPassword) || !userService.updatePassword(currentPassword, newPassword, user.getEmail())) {
            return null;
        }
        return "success";
    }

    @GetMapping("/getAllUsers")
    @ResponseBody
    public List<User> getAllUsers(@AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());
        if (userService.isAdmin(user)) {
            return userService.getAllUsers();
        } else {
            return new LinkedList<>();
        }
    }

    @GetMapping("/getAllAdmins")
    @ResponseBody
    public List<User> getAllAdmins(@AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());
        List<User> admins = userService.getAllAdmins();
        List<User> newAdminList = new ArrayList<>();
        if (userService.isAdmin(user)) {
            for (User to: admins) {
                if (to != user) {
                  newAdminList.add(to);
                }
            }
            return newAdminList;
        } else {
            return new LinkedList<>();
        }
    }

    @GetMapping("/makeAdmin/{id}")
    public String makeAdmin(@AuthenticationPrincipal UserDetails currentUser,
                            @PathVariable("id") Long id) {
        User editUser = userService.findUserById (id);
        userService.makeAdmin (editUser);
        return "redirect:/admin";
    }

    @GetMapping("/makeUser/{id}")
    public String makeUser(@AuthenticationPrincipal UserDetails currentUser,
                            @PathVariable("id") Long id) {
        User editUser = userService.findUserById (id);
        userService.makeUser (editUser);
        return "redirect:/admin";
    }

    @GetMapping("/updateInfo")
    @ResponseBody
    public String updateInfo(@RequestParam(name = "email") String email,
                                 @RequestParam(name = "firstName") String firstName,
                                 @RequestParam(name = "secondName") String secondName,
                                 @RequestParam(name = "age") String age,
                                 @RequestParam(name = "interest") String interest) {

        User user = userService.findByUsername(email);
        if (!firstName.equals("")) {
            user.setFirstName(firstName);
        }
        if (!secondName.equals("")) {
            user.setLastName(secondName);
        }
        if (!age.equals("")) {
            user.setAge(Integer.parseInt(age));
        }
        if (!interest.equals("")) {
            user.setInterests(interest);
        }
        System.out.println(firstName + ' ' + secondName + ' ' + age + ' ' + interest);
        userService.save(user);
        return "success";
    }
}
