package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mitra")
@AllArgsConstructor
public class MitraController {

    // @PreAuthorize("hasAuthority('READ_APPLICANT_NOMINEE') and hasAuthority('CREATE_APPLICANT_NOMINEE') and hasAuthority('UPDATE_APPLICANT_NOMINEE') and hasAuthority('DELETE_APPLICANT_NOMINEE') and hasAuthority('READ_JOB')")
    @GetMapping
    public String mitraHomePage(Model model) {
        return "mitra/dashboard"; 
    }
}
