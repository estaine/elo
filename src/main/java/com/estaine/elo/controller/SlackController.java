package com.estaine.elo.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
public class SlackController {
    @RequestMapping(value = "/match", method = RequestMethod.POST)
    public String test(@RequestBody String string) {
        return string;
    }
}
