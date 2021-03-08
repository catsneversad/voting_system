package com.example.final_project.web;

import com.example.final_project.model.Poll;
import com.example.final_project.model.User;
import com.example.final_project.service.AnswerService;
import com.example.final_project.service.PollService;
import com.example.final_project.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AnswerController {
    private AnswerService answerService;
    private UserService userService;
    private PollService pollService;

    public AnswerController (AnswerService answerService, UserService userService, PollService pollService) {
        super();
        this.answerService = answerService;
        this.userService = userService;
        this.pollService = pollService;
    }


    @GetMapping("/makeVote/{id}")
    public String makeVote(Model model, @AuthenticationPrincipal UserDetails currentUser, @PathVariable("id") Long id) {
        User user = userService.findByUsername(currentUser.getUsername());
        Poll curPoll = pollService.getPollById(id);
        User author = userService.findUserById(curPoll.getAuthor_id());

        model.addAttribute("currentUser", user);
        model.addAttribute("currentPoll", curPoll);
        model.addAttribute("pollAuthor","Author: " + author.getEmail());
        if (userService.isAdmin(user))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isAdmin", false);
        return "makeVote";
    }


}
