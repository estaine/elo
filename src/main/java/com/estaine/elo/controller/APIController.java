package com.estaine.elo.controller;

import com.estaine.elo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private TournamentService tournamentService;

    @RequestMapping("/start/group/{tournamentId}")
    public String startGroupStage(@PathVariable Long tournamentId) {
        tournamentService.startGroupStage(tournamentId);
        return "OK";
    }
}
