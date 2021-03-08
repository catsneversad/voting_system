package com.example.final_project.web;

import com.example.final_project.model.Answer;
import com.example.final_project.model.Poll;
import com.example.final_project.model.User;
import com.example.final_project.model.Vote;
import com.example.final_project.service.AnswerService;
import com.example.final_project.service.PollService;
import com.example.final_project.service.UserService;
import com.example.final_project.service.VoteService;
import com.example.final_project.web.dto.AnswerDto;
import com.example.final_project.web.dto.PollDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PollController {
    private UserService userService;
    private PollService pollService;
    private AnswerService answerService;
    private VoteService voteService;

    public PollController(UserService userService, PollService pollService, AnswerService answerService,
                          VoteService voteService) {
        super();
        this.userService = userService;
        this.pollService = pollService;
        this.answerService = answerService;
        this.voteService = voteService;
    }

    @GetMapping("/admin/poll/{id}")
    public String poll(Model model, @AuthenticationPrincipal UserDetails currentUser, @PathVariable("id") Long id) {
        User user = userService.findByUsername(currentUser.getUsername());
        Poll curPoll = pollService.getPollById(id);
        User author = userService.findUserById(curPoll.getAuthor_id());
        model.addAttribute("currentUser", user);
        model.addAttribute("currentPoll", curPoll);
        model.addAttribute("pollAuthor", "Author: " + author.getEmail());
        if (userService.isAdmin(user))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isAdmin", false);
        return "poll";
    }

    @GetMapping("/checkPoll/{id}")
    public String checkPoll(Model model, @AuthenticationPrincipal UserDetails currentUser, @PathVariable("id") Long id) {
        User user = userService.findByUsername(currentUser.getUsername());
        Poll curPoll = pollService.getPollById(id);
        User author = userService.findUserById(curPoll.getAuthor_id());
        List<Answer> answerList = answerService.getAnswersByPoll(curPoll);

        int sumOfRate = 0;
        for (Answer ans: answerList) {
            sumOfRate += voteService.getRateOfAnswer(ans.getId());
        }

        model.addAttribute("sumOfRate", sumOfRate);
        model.addAttribute("currentUser", user);
        model.addAttribute("currentPoll", curPoll);
        model.addAttribute("pollAuthor", "Author: " + author.getEmail());
        if (userService.isAdmin(user))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isAdmin", false);
        return "checkPoll";
    }

    @GetMapping("/addAnswer")
    @ResponseBody
    public String addAnswer(@RequestParam(name = "pollId") String pollId,
                            @RequestParam(name = "answerList") List<String> answerList) {


        System.out.println("Hello " + answerList);
        Poll curPoll = pollService.getPollById((long) Integer.parseInt(pollId));
        List<Answer> pollAnswerList = (List<Answer>) curPoll.getAnswers();

        System.out.println("not Hello " + pollAnswerList);
        for (Answer answer : pollAnswerList) {
            answerService.delete(answer);
        }

        pollAnswerList.clear();

        for (String s : answerList) {
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
    public List<Answer> getPollAnswers(@RequestParam(name = "pollId") String pollId) {
        Poll curPoll = pollService.getPollById((long) Integer.parseInt(pollId));
        return answerService.getAnswersByPoll(curPoll);
    }

    @GetMapping("/saveVote")
    @ResponseBody
    public String saveVote(@RequestParam(name = "pollId") String pollId1,
                           @RequestParam(name = "userEmail") String userEmail,
                           @RequestParam(name = "selectedId") String selectedId1) {
        Long pollId = (long) Integer.parseInt(pollId1);
        Long selectedId = (long) Integer.parseInt(selectedId1);
        Long userId = userService.findIdByUsername(userEmail);

        Vote curVote = new Vote(pollId, selectedId, userId);
        voteService.save(curVote);
        return "success";
    }

    //    getStatisticsOfPoll
    @GetMapping("/getStatisticsOfPoll")
    @ResponseBody
    public List<AnswerDto> getStatisticsOfPoll(@RequestParam(name = "email") String email,
                                               @RequestParam(name = "pollId") String pollId) {
        User curUser = userService.findByUsername(email);
        Poll curPoll = pollService.getPollById((long)Integer.parseInt(pollId));
        List<Answer> answerList = answerService.getAnswersByPoll(curPoll);
        Long checkedAnswerId = voteService.getAnswerIdByUserIdAndPollId(curUser.getId(), curPoll.getId());


        List<AnswerDto> answerDtoList = new ArrayList<>();
        for (Answer ans: answerList) {
            answerDtoList.add(new AnswerDto(ans.getId(), ans.getValue(), voteService.getRateOfAnswer(ans.getId()), ans.getId().equals(checkedAnswerId)));
        }
        return answerDtoList;
    }

    @GetMapping("/getAllUnansweredPolls")
    @ResponseBody
    public List<PollDto> getAllUnansweredPolls(@RequestParam(name = "email") String email) {
        User curUser = userService.findByUsername(email);
        List<Poll> pollList = pollService.getAllPolls();
        List<PollDto> newPollList = new ArrayList<>();
        for (int i = 0; i < pollList.size(); i++) {
            Poll curPoll = pollList.get(i);
            if (!voteService.isThisPollAlreadyVotedByUser(curUser.getId(), curPoll.getId())) {
                PollDto curPollDto = new PollDto(curPoll.getId(), curPoll.getTitle(), userService.findUserById(curPoll.getAuthor_id()).getEmail());
                newPollList.add(curPollDto);
            }
        }
        return newPollList;
    }

    @GetMapping("/getAllAnsweredPolls")
    @ResponseBody
    public List<PollDto> getAllAnsweredPolls(@RequestParam(name = "email") String email) {
        User curUser = userService.findByUsername(email);
        List<Poll> pollList = pollService.getAllPolls();
        List<PollDto> newPollList = new ArrayList<>();
        for (int i = 0; i < pollList.size(); i++) {
            Poll curPoll = pollList.get(i);
            if (voteService.isThisPollAlreadyVotedByUser(curUser.getId(), curPoll.getId())) {
                PollDto curPollDto = new PollDto(curPoll.getId(), curPoll.getTitle(), userService.findUserById(curPoll.getAuthor_id()).getEmail());
                newPollList.add(curPollDto);
            }
        }
        return newPollList;
    }
}
