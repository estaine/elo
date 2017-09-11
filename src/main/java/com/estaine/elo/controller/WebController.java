package com.estaine.elo.controller;

import com.estaine.elo.format.RatingFormatter;
import com.estaine.elo.service.RatingService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingFormatter ratingFormatter;

    @RequestMapping(value = "/*", method = RequestMethod.GET)
    public String getRatings(Model model) {
        model.addAttribute("ratings", ratingFormatter.formatRating(ratingService.calculateRatings()));
        return "index";
    }
}
