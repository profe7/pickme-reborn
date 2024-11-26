package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.responses.MitraDashboardTelemetryResponse;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.services.mitra.MitraService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@PreAuthorize("hasRole('ROLE_MITRA')")
@RequestMapping("/mitra")
@AllArgsConstructor
public class MitraController {

    private final MitraService mitraService;

    @GetMapping
    public String mitraHomePage(Model model, HttpSession session) {
        Long mitraId = (Long) session.getAttribute("mitraId");

        MitraDashboardTelemetryResponse response = mitraService.getMitraDashboardTelemetry(mitraId);

        model.addAttribute("availableVacancies", response.getAvailableVacancies());
        model.addAttribute("totalApplicants", response.getTotalApplicants());
        model.addAttribute("totalRejectedApplicants", response.getTotalRejectedApplicants());
        model.addAttribute("totalAcceptedApplicants", response.getTotalAcceptedApplicants());
        model.addAttribute("totalAssignedApplicants", response.getTotalAssignedApplicants());

        model.addAttribute("newestVacancies", response.getNewestVacancies());

        return "mitra/dashboard-mitra";
    }

    @GetMapping("/chart-data")
    @ResponseBody
    public Map<String, Long> getChartData(HttpSession session) {
        Long mitraId = (Long) session.getAttribute("mitraId");

        MitraDashboardTelemetryResponse response = mitraService.getMitraDashboardTelemetry(mitraId);

        Map<String, Long> chartData = new HashMap<>();
        chartData.put("totalRejectedApplicants", response.getTotalRejectedApplicants());
        chartData.put("totalAcceptedApplicants", response.getTotalAcceptedApplicants());
        chartData.put("totalAssignedApplicants", response.getTotalAssignedApplicants());

        return chartData; 
    }

    @GetMapping("/talent")
    public String viewAllTalentByMitra(HttpSession session,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String skill,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Model model) {
        Long mitraId = (Long) session.getAttribute("mitraId");
        Page<Talent> talents = mitraService.getFilteredMitraTalents(mitraId, page, size, position, skill);
        model.addAttribute("talents", talents);
        model.addAttribute("currentPage", page);
        model.addAttribute("position", position);
        model.addAttribute("skill", skill);
        return "mitra/mitra-view-all-talents";
    }

}
