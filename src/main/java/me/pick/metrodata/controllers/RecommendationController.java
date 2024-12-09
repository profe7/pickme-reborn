package me.pick.metrodata.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/recommendation")
@AllArgsConstructor
public class RecommendationController {

    @GetMapping
    public String recommendation(Model model) {

        return "client/recommendation";
        
    }
}