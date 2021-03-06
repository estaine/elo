package com.estaine.elo.controller;

import com.estaine.elo.dto.PlayerStats;
import com.estaine.elo.format.RatingFormatter;
import com.estaine.elo.service.AwardService;
import com.estaine.elo.service.RatingService;
import com.estaine.elo.service.TournamentService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RatingService ratingService;
    private final TournamentService tournamentService;
    private final RatingFormatter ratingFormatter;
    private final AwardService awardService;

    @Autowired
    public ApiController(@NonNull RatingService ratingService,
            @NonNull TournamentService tournamentService,
            @NonNull RatingFormatter ratingFormatter, AwardService awardService) {
        this.ratingService = ratingService;
        this.tournamentService = tournamentService;
        this.ratingFormatter = ratingFormatter;
        this.awardService = awardService;
    }

    @RequestMapping("/start/group/{tournamentId}")
    public String startGroupStage(@PathVariable Long tournamentId) {
        tournamentService.startGroupStage(tournamentId);
        return "OK";
    }

    @RequestMapping("/start/playoff/{tournamentId}")
    public String startPlayoffStage(@PathVariable Long tournamentId) {
        tournamentService.startPlayoffStage(tournamentId);
        return "OK";
    }

    @RequestMapping(value = {"/", "/rating"}, method = RequestMethod.GET)
    public List<PlayerStats> getRatings() {
        return ratingFormatter.formatRating(ratingService.calculateRatings());
    }

    @RequestMapping(value = "/update/awards")
    public String updateAwards() {
        awardService.updateAwards();
        return "OK";
    }
}
