package com.estaine.elo.controller;

import com.estaine.elo.format.GroupStatsFormatter;
import com.estaine.elo.format.RatingFormatter;
import com.estaine.elo.service.PlayerStatsService;
import com.estaine.elo.service.RatingService;
import com.estaine.elo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private PlayerStatsService playerStatsService;

    @Autowired
    private GroupStatsFormatter groupStatsFormatter;

    @Autowired
    private RatingFormatter ratingFormatter;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getDefault(Model model) {
        return getRatings(model);
    }

    @RequestMapping(value = "/rating", method = RequestMethod.GET)
    public String getRatings(Model model) {
        model.addAttribute("ratings", ratingFormatter.formatRating(ratingService.calculateRatings()));
        return "rating";
    }

    @RequestMapping(value = "/tournament/{tournamentId}/groups", method = RequestMethod.GET)
    public String getTournamentGroupStats(Model model, @PathVariable Long tournamentId) {
        model.addAttribute("groups", groupStatsFormatter.formatGroupStats(tournamentService.getTournamentRoundRobinStats()));
        //List<Box> boxes = groupStatsFormatter.formatBoxStats(tournamentService.getBoxStats(tournamentId));
        return "groups";
    }

    @RequestMapping(value = "/tournament/{id}/playoff", method = RequestMethod.GET)
    public String getTournamentPlayoffStats(Model model) {
        return null;
    }

    @RequestMapping(value = "/player/{username}", method = RequestMethod.GET)
    public String getPlayerStats(Model model, @PathVariable String username) {
        model.addAttribute("playerStats", playerStatsService.getPlayerStats(username));
        return "player";
    }
}
