package com.example.final_project.web;

import com.example.final_project.service.EmailChecker;
import com.example.final_project.service.UserService;
import com.example.final_project.service.UsernamePasswordChecker;
import com.example.final_project.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    private UserService userService;

    ExecutorService threadExecutor;


    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
        this.threadExecutor = Executors.newCachedThreadPool();
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(Model model, @ModelAttribute("user") UserRegistrationDto registrationDto) throws ExecutionException, InterruptedException {
        Future<Boolean> futureCall1 = this.threadExecutor.submit(new EmailChecker(registrationDto.getEmail()));
        Future<Boolean> futureCall2 = this.threadExecutor.submit(new UsernamePasswordChecker(registrationDto.getEmail(), registrationDto.getPassword()));
        boolean result1 = futureCall1.get();
        boolean result2 = futureCall2.get();

        if (!result1) {
            return "redirect:/registration?errorMail";
        } else if (!result2) {
            return "redirect:/registration?errorPassword";
        } else {
            userService.save(registrationDto);
            return "redirect:/registration?success";
        }
    }
}
