package me.pick.metrodata.controllers;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.services.interview.InterviewScheduleService;
import me.pick.metrodata.services.recommendation.RecommendationService;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final RecommendationService recommendationService;

    private final InterviewScheduleService interviewScheduleService;

    @GetMapping("/recommendations")
    public String clientRecommendationPage(
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(required = false) String talentName,
            @RequestParam(required = false) String position){
        Long clientId = (Long) session.getAttribute("clientId");
        model.addAttribute("clientId", clientId);
        Page<RecommendationGroupedResponse> recommendations = recommendationService.getRecommendationClientPaged(page, size, clientId, talentName, position);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("pageNumbers", IntStream.range(0, recommendations.getTotalPages()).boxed().toList());

        return "recommendation/index";
    }

    @GetMapping("/interview-schedules")
    public String getAllSchedules(
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) InterviewType type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) InterviewStatus status) {

        Long clientId = (Long) session.getAttribute("clientId");

        Page<InterviewSchedule> schedules = interviewScheduleService.getAll(search, clientId, type, startDate, endDate, status, page, size);

        model.addAttribute("interviewSchedules", schedules);
        model.addAttribute("pageNumbers", IntStream.range(0, schedules.getTotalPages()).boxed().collect(Collectors.toList()));

        return "interview-schedule/view-interview-schedule";
    }


}