package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.responses.MitraDashboardTelemetryResponse;
import me.pick.metrodata.services.mitra.MitraService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mitra")
@AllArgsConstructor
public class MitraController {

    private final MitraService mitraService;

    @GetMapping("/{mitraId}")
    // @PreAuthorize("hasAuthority('READ_APPLICANT_NOMINEE') and hasAuthority('CREATE_APPLICANT_NOMINEE') and hasAuthority('UPDATE_APPLICANT_NOMINEE') and hasAuthority('DELETE_APPLICANT_NOMINEE') and hasAuthority('READ_JOB')")
    public String mitraHomePage(Model model, @PathVariable Long mitraId) {
        
        MitraDashboardTelemetryResponse response = mitraService.getMitraDashboardTelemetry(mitraId);
        
        model.addAttribute("availableVacancies", response.getAvailableVacancies());
        model.addAttribute("totalApplicants", response.getTotalApplicants());
        model.addAttribute("totalRejectedApplicants", response.getTotalRejectedApplicants());
        model.addAttribute("totalAcceptedApplicants", response.getTotalAcceptedApplicants());

        model.addAttribute("newestVacancies", response.getNewestVacancies());

        return "mitra/dashboard"; 
    }
}
