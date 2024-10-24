package me.pick.metrodata.controllers;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/landing-page")
@AllArgsConstructor
public class LandingPageController {

    @GetMapping
    public String landingPage() {
        return "landing-page";
    }
}