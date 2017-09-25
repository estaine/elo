package com.estaine.elo.controller;

import com.estaine.elo.service.MatchService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
public class SlackController {

    private final MatchService matchService;

    @Autowired
    public SlackController(@NonNull MatchService matchService) {
        this.matchService = matchService;
    }

    @RequestMapping(value = "/match", method = RequestMethod.POST, headers = "Content-Type=application/x-www-form-urlencoded", consumes = {MediaType.ALL_VALUE})
    public String registerMatch(@RequestParam String token, @RequestParam("channel_name") String channelName,
            @RequestParam("user_name") String userName, @RequestParam String text) {

        System.out.println("Slack /match request received from @" + userName);
        return matchService.registerMatch(userName, channelName, text, token);
    }

    @RequestMapping(value = "/group-match", method = RequestMethod.POST, headers = "Content-Type=application/x-www-form-urlencoded", consumes = {MediaType.ALL_VALUE})
    public String registerTournamentMatch(@RequestParam String token, @RequestParam("channel_name") String channelName,
            @RequestParam("user_name") String userName, @RequestParam String text) {

        System.out.println("Slack /group-match request received from @" + userName);
        return matchService.registerGroupMatch(userName, channelName, text, token);
    }
}
