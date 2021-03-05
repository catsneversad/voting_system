package com.example.final_project.web;

import com.example.final_project.model.Answer;
import com.example.final_project.model.Poll;
import com.example.final_project.model.User;
import com.example.final_project.service.AnswerService;
import com.example.final_project.service.PollService;
import com.example.final_project.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/admin")
public class AdminPanelController {
    private PollService pollService;
    private UserService userService;
    private AnswerService answerService;

    public AdminPanelController(UserService userService, PollService pollService, AnswerService answerService) {
        super();
        this.answerService = answerService;
        this.userService = userService;
        this.pollService = pollService;
    }

    @GetMapping("/admin")
    public String admin(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());
        model.addAttribute("currentUser", user);
        return "admin";
    }

    @GetMapping("/getMyPolls")
    @ResponseBody
    public List<Poll> getMyPolls(@AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());
        return pollService.getPollsByAuthorId(user.getId());
    }

    @GetMapping("/createPoll")
    @ResponseBody
    public String createPoll(@RequestParam(name = "email") String email,
                             @RequestParam(name = "title") String title) {
        User user = userService.findByUsername(email);
        Poll newPoll = new Poll(title, user.getId());
        pollService.save(newPoll);
        return "success";
    }

    @GetMapping("/updatePoll")
    @ResponseBody
    public String updatePoll(@RequestParam(name = "email") String email,
                             @RequestParam(name = "pollId") String pollId) {
        User user = userService.findByUsername(email);
        Poll curPoll = pollService.getPollById((long) Integer.parseInt(pollId));
        pollService.save(curPoll);
        return "success";
    }

    @GetMapping("/createAnswer")
    @ResponseBody
    public String createAnswer(@RequestParam(name = "value") String answerValue,
                               @RequestParam(name = "rate") String answerRate,
                               @RequestParam(name = "pollId") String pollId) {

        Answer answer = new Answer(answerValue, (long) Integer.parseInt(answerRate), (long) Integer.parseInt(pollId));
        answerService.save(answer);
        return "success";
    }

    @GetMapping("/updateAnswer")
    @ResponseBody
    public String updateAnswer(@RequestParam(name = "value") String answerValue,
                               @RequestParam(name = "rate") String answerRate,
                               @RequestParam(name = "answerId") String answerId) {
        Answer curAnswer = answerService.getAnswerById((long) Integer.parseInt(answerId));
        if (!answerValue.equals("")) {
            curAnswer.setValue(answerValue);
        }
        if (!answerRate.equals("")) {
            curAnswer.setRate((long) Integer.parseInt(answerRate));
        }
        answerService.save(curAnswer);
        return "success";
    }

    @GetMapping("/deletePoll/{id}")
    @ResponseBody
    public String deletePoll(@PathVariable("id") Long id) {
        pollService.deleteById(id);
        return "success";
    }
}
