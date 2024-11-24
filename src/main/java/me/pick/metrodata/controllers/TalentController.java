package me.pick.metrodata.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import me.pick.metrodata.services.talent.TalentService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
@Controller
@RequestMapping("/talent")
@AllArgsConstructor

public class TalentController {
    @GetMapping
    public String talentPage() {
        // private final TalentService talentService;

        return "talent/talent-form-cv";
    }
    
   


}