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

import java.util.*;

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
        for (Answer ans : answerList) {
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

    @GetMapping("/getPopularUnansweredPolls")
    @ResponseBody
    public List<PollDto> getPopularUnansweredPolls(@RequestParam(name = "email") String email) {
        User curUser = userService.findByUsername(email);
        List<Poll> pollList = pollService.getAllPolls();
        List<PollDto> newPollList = new ArrayList<>();
        for (int i = 0; i < pollList.size(); i++) {
            Poll curPoll = pollList.get(i);

            if (!voteService.isThisPollAlreadyVotedByUser(curUser.getId(), curPoll.getId())) {
                PollDto curPollDto = new PollDto(curPoll.getId(), curPoll.getTitle(), userService.findUserById(curPoll.getAuthor_id()).getEmail(), pollService.getRateOfPoll(curPoll));
                newPollList.add(curPollDto);
            }
        }

        Collections.sort(newPollList, new SortByRate());
        List<PollDto> returnList = new ArrayList<>();
        for (int i = 0; i < Math.min (3, newPollList.size()); i ++) {
            returnList.add(newPollList.get(i));
        }
        return returnList;
    }

    @GetMapping("/getPopularAnsweredPolls")
    @ResponseBody
    public List<PollDto> getPopularAnsweredPolls(@RequestParam(name = "email") String email) {
        User curUser = userService.findByUsername(email);
        List<Poll> pollList = pollService.getAllPolls();
        List<PollDto> newPollList = new ArrayList<>();
        for (int i = 0; i < pollList.size(); i++) {
            Poll curPoll = pollList.get(i);

            if (voteService.isThisPollAlreadyVotedByUser(curUser.getId(), curPoll.getId())) {
                PollDto curPollDto = new PollDto(curPoll.getId(), curPoll.getTitle(), userService.findUserById(curPoll.getAuthor_id()).getEmail(), pollService.getRateOfPoll(curPoll));
                newPollList.add(curPollDto);
            }
        }

        Collections.sort(newPollList, new SortByRate());
        List<PollDto> returnList = new ArrayList<>();
        for (int i = 0; i < Math.min (3, newPollList.size()); i ++) {
            returnList.add(newPollList.get(i));
        }
        return returnList;
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

    @GetMapping("/getStatisticsOfPollByGroup")
    @ResponseBody
    public List<AnswerDto> getStatisticsOfPollByGroup(@RequestParam(name = "email") String email,
                                                      @RequestParam(name = "pollId") String pollId) {
        User curUser = userService.findByUsername(email);
        Poll curPoll = pollService.getPollById((long) Integer.parseInt(pollId));


        List<User> allUsers = userService.getAllUsers();
        List<User> answeredUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getGroupName() == null) continue;
            if (user.getGroupName().equals(curUser.getGroupName())) {
                if (voteService.isThisPollAlreadyVotedByUser(user.getId(), curPoll.getId())) {
                    answeredUsers.add(user);
                }
            }
        }

        return getAnswerDtoListByCurPoll(curPoll, answeredUsers);
    }

    @GetMapping("/getStatisticsOfPollByInterest")
    @ResponseBody
    public List<AnswerDto> getStatisticsOfPollByInterest(@RequestParam(name = "email") String email,
                                                        @RequestParam(name = "pollId") String pollId) {
        User curUser = userService.findByUsername(email);
        Poll curPoll = pollService.getPollById((long) Integer.parseInt(pollId));


        List<User> allUsers = userService.getAllUsers();
        List<User> answeredUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getInterests() == null) continue;
            if (user.getInterests().equals(curUser.getInterests())) {
                if (voteService.isThisPollAlreadyVotedByUser(user.getId(), curPoll.getId())) {
                    answeredUsers.add(user);
                }
            }
        }

        return getAnswerDtoListByCurPoll(curPoll, answeredUsers);
    }

    @GetMapping("/getStatisticsOfPollByGender")
    @ResponseBody
    public List<AnswerDto> getStatisticsOfPollByGender(@RequestParam(name = "email") String email,
                                                         @RequestParam(name = "pollId") String pollId) {
        User curUser = userService.findByUsername(email);
        Poll curPoll = pollService.getPollById((long) Integer.parseInt(pollId));

        List<User> userList = userService.getAllUsers();
        List<Answer> answerList = answerService.getAnswersByPoll(curPoll);

        List<AnswerDto> answerDtoList = new ArrayList<>();
        for (Answer ans : answerList) {
            int unknown = 0, male = 0, female = 0, nonBinary = 0;
            for (User user: userList) {
                if (voteService.isThisUserSelectedThisAnswer(user.getId(), ans.getId())) {
                    if (user.getGender() == null) {
                        unknown ++;
                    }
                    else if (user.getGender().equals("male")) {
                        male ++;
                    }
                    else if (user.getGender().equals("female")) {
                        male ++;
                    }
                    else if (user.getGender().equals("nonbinary")) {
                        nonBinary ++;
                    }
                }
            }

            if (male+female+nonBinary+unknown != 0) {
                float statMale = (float) (((float) (male) / (float) (male + female + nonBinary + unknown)) * 100.00);
                float statFemale = (float) (((float) (female) / (float) (male + female + nonBinary + unknown)) * 100.00);
                float statNonbinary = (float) (((float) (nonBinary) / (float) (male + female + nonBinary + unknown)) * 100.00);
                float statUnknown = (float) (((float) (unknown) / (float) (male + female + nonBinary + unknown)) * 100.00);

                System.out.println(ans.getValue() + ' ' + statMale + ' ' + statFemale + ' ' + statNonbinary + ' ' + statUnknown);

                answerDtoList.add(new AnswerDto(ans.getId(), ans.getValue(), statMale, statFemale, statNonbinary, statUnknown, (float)voteService.getRateOfAnswer(ans.getId())));
            } else {
                answerDtoList.add(new AnswerDto(ans.getId(), ans.getValue(), 0, 0, 0, 0, 0));

            }
        }
        return answerDtoList;
    }

    public List<AnswerDto> getAnswerDtoListByCurPoll(Poll curPoll, List<User> answeredUsers) {
        List<Answer> answerList = answerService.getAnswersByPoll(curPoll);
        List<AnswerDto> answerDtoList = new ArrayList<>();
        for (Answer ans : answerList) {
            List<String> userList = new ArrayList<>();
            for (User user : answeredUsers) {
                if (ans.getId().equals(voteService.getAnswerIdByUserIdAndPollId(user.getId(), curPoll.getId()))) {
                    userList.add(user.getEmail());
                }
            }
            answerDtoList.add(new AnswerDto(ans.getId(), ans.getValue(), userList));
        }

        return answerDtoList;
    }

    @GetMapping("/getStatisticsOfPoll")
    @ResponseBody
    public List<AnswerDto> getStatisticsOfPoll(@RequestParam(name = "email") String email,
                                               @RequestParam(name = "pollId") String pollId) {
        User curUser = userService.findByUsername(email);
        Poll curPoll = pollService.getPollById((long) Integer.parseInt(pollId));
        List<Answer> answerList = answerService.getAnswersByPoll(curPoll);
        Long checkedAnswerId = voteService.getAnswerIdByUserIdAndPollId(curUser.getId(), curPoll.getId());

        List<AnswerDto> answerDtoList = new ArrayList<>();
        for (Answer ans : answerList) {
            float stat = (float) ((float)voteService.getRateOfAnswer(ans.getId())/(float)(pollService.getRateOfPoll(curPoll))*100.00);
            answerDtoList.add(new AnswerDto(ans.getId(), ans.getValue(), stat, ans.getId().equals(checkedAnswerId)));
        }
        return answerDtoList;
    }
}

class SortByRate implements Comparator<PollDto> {
    @Override
    public int compare(PollDto a, PollDto b) {
        return Integer.compare(b.getRate(), a.getRate());
    }
}
