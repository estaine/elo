package com.estaine.elo.controller;

import com.estaine.elo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
public class SlackController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/match", method = RequestMethod.POST, headers = "Content-Type=application/x-www-form-urlencoded", consumes = {MediaType.ALL_VALUE})
    public String registerMatch(@RequestParam String token, @RequestParam("team_id") String teamId, @RequestParam("team_domain") String teamDomain,
            @RequestParam("channel_id") String channelId, @RequestParam("channel_name") String channelName,
            @RequestParam("user_id") String userId, @RequestParam("user_name") String userName, @RequestParam String command,
            @RequestParam String text, @RequestParam("response_url") String responseUrl, @RequestParam("trigger_id") String triggerId) {

        System.out.println("token: " + token);
        System.out.println("teamId: " + teamId);
        System.out.println("teamDomain: " + teamDomain);
        System.out.println("channelId: " + channelId);
        System.out.println("channelName: " + channelName);
        System.out.println("userId: " + userId);
        System.out.println("userName: " + userName);
        System.out.println("command: " + command);
        System.out.println("text: " + text);
        System.out.println("responseUrl: " + responseUrl);
        System.out.println("triggerId: " + triggerId);

        return gameService.registerMatch(userName, channelName, text, token);
    }

    @RequestMapping(value = "/cup-match", method = RequestMethod.POST, headers = "Content-Type=application/x-www-form-urlencoded", consumes = {MediaType.ALL_VALUE})
    public String registerTournamentMatch(@RequestParam String token, @RequestParam("team_id") String teamId, @RequestParam("team_domain") String teamDomain,
            @RequestParam("channel_id") String channelId, @RequestParam("channel_name") String channelName,
            @RequestParam("user_id") String userId, @RequestParam("user_name") String userName, @RequestParam String command,
            @RequestParam String text, @RequestParam("response_url") String responseUrl, @RequestParam("trigger_id") String triggerId) {

        return gameService.registerTournamentMatch(userName, channelName, text, token);
    }
}
