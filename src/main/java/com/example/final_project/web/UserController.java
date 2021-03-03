package com.example.final_project.web;


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
}
