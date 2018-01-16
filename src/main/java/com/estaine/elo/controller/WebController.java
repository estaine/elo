package com.estaine.elo.controller;

import com.estaine.elo.dto.PlayerStats;
import com.estaine.elo.entity.Player;
import com.estaine.elo.format.GroupStatsFormatter;
import com.estaine.elo.format.PlayerStatsFormatter;
import com.estaine.elo.format.PlayoffStatsFormatter;
import com.estaine.elo.format.RatingFormatter;
import com.estaine.elo.service.PlayerStatsService;
import com.estaine.elo.service.RatingService;
import com.estaine.elo.service.TournamentService;
import java.util.Map;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController {

    private final RatingService ratingService;
    private final TournamentService tournamentService;
    private final PlayerStatsService playerStatsService;
    private final GroupStatsFormatter groupStatsFormatter;
    private final PlayoffStatsFormatter playoffStatsFormatter;
    private final PlayerStatsFormatter playerStatsFormatter;
    private final RatingFormatter ratingFormatter;

    @Autowired
    public WebController(@NonNull RatingService ratingService,
            @NonNull TournamentService tournamentService,
            @NonNull PlayerStatsService playerStatsService,
            @NonNull GroupStatsFormatter groupStatsFormatter,
            PlayoffStatsFormatter playoffStatsFormatter, @NonNull PlayerStatsFormatter playerStatsFormatter,
            @NonNull RatingFormatter ratingFormatter) {
        this.ratingService = ratingService;
        this.tournamentService = tournamentService;
        this.playerStatsService = playerStatsService;
        this.groupStatsFormatter = groupStatsFormatter;
        this.playoffStatsFormatter = playoffStatsFormatter;
        this.playerStatsFormatter = playerStatsFormatter;
        this.ratingFormatter = ratingFormatter;
    }

    @RequestMapping(value = {"/", "/rating"}, method = RequestMethod.GET)
    public String getRatings(Model model) {
        Map<Player, PlayerStats> ratings = ratingService.calculateRatings();
        model.addAttribute("ratings", ratingFormatter.formatRating(ratings));
        model.addAttribute("inactiveRatings", ratingFormatter.getInactiveRatings(ratings));
        return "rating";
    }

    @RequestMapping(value = "/tournament/groups", method = RequestMethod.GET)
    public String getTournamentGroupStats(Model model) {
        model.addAttribute("tournament", groupStatsFormatter.formatGroupStage(tournamentService.getGroupStats()));
        return "groups";
    }

    @RequestMapping(value = "/tournament/playoff", method = RequestMethod.GET)
    public String getTournamentPlayoffStats(Model model) {
        model.addAttribute("tournament", playoffStatsFormatter.formatPlayoffStats(tournamentService.getPlayoffStats()));
        return "playoff";
    }


    @RequestMapping(value = "/player/{username:.+}", method = RequestMethod.GET)
    public String getPlayerStats(Model model, @PathVariable String username) {
        model.addAttribute("playerStats", playerStatsFormatter.formatPlayerStats(playerStatsService.getPlayerStats(username)));
        return "player";
    }
}
