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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PollController {
    private UserService userService;
    private PollService pollService;
    private AnswerService answerService;

    public PollController(UserService userService, PollService pollService, AnswerService answerService) {
        super();
        this.userService = userService;
        this.pollService = pollService;
        this.answerService = answerService;
    }
    @GetMapping("/admin/poll/{id}")
    public String poll(Model model, @AuthenticationPrincipal UserDetails currentUser, @PathVariable("id") Long id) {
        User user = userService.findByUsername(currentUser.getUsername());
        Poll curPoll = pollService.getPollById(id);
        User author = userService.findUserById(curPoll.getAuthor_id());
        model.addAttribute("currentUser", user);
        model.addAttribute("currentPoll", curPoll);
        model.addAttribute("pollAuthor","Author: " + author.getEmail());
        return "poll";
    }

    @GetMapping("/addAnswer")
    @ResponseBody
    public String addAnswer (@RequestParam(name = "pollId") String pollId,
                             @RequestParam(name = "answerList") List<String> answerList) {


        System.out.println("Hello " + answerList);
        Poll curPoll = pollService.getPollById((long)Integer.parseInt(pollId));
        List<Answer> pollAnswerList = (List<Answer>) curPoll.getAnswers();

        System.out.println("not Hello " + pollAnswerList);
        for (Answer answer: pollAnswerList) {
            answerService.delete(answer);
        }

        pollAnswerList.clear();

        for (String s: answerList) {
            if (!s.equals("")) {
                Answer newAnswer = new Answer(s, curPoll);
                System.out.println(newAnswer.getValue());
                pollAnswerList.add(newAnswer);

                answerService.save(newAnswer);
            }
        }

        curPoll.setAnswers(pollAnswerList);
        pollService.save(curPoll);
        return "success";
    }

    @GetMapping("/getPollAnswers")
    @ResponseBody
    public List<Answer> getPollAnswers (@RequestParam(name = "pollId") String pollId) {
        Poll curPoll = pollService.getPollById((long)Integer.parseInt(pollId));
        return answerService.getAnswersByPoll(curPoll);
    }
}
