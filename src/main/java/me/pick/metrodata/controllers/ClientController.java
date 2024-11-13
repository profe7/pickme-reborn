package me.pick.metrodata.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.services.interview.InterviewScheduleService;
import me.pick.metrodata.services.recommendation.RecommendationService;

@Controller
@AllArgsConstructor
public class ClientController {

    private final RecommendationService recommendationService;

    private final InterviewScheduleService interviewScheduleService;
    
    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendationGroupedResponse>> getTalentRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<RecommendationGroupedResponse> recommendations = recommendationService.getAllByInstituteOrUser();

        // Implement pagination logic
        int fromIndex = Math.min(page * size, recommendations.size());
        int toIndex = Math.min((page + 1) * size, recommendations.size());
        List<RecommendationGroupedResponse> paginatedRecommendations = recommendations.subList(fromIndex, toIndex);

        return ResponseEntity.ok(paginatedRecommendations);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Page<InterviewSchedule>> getAllSchedules(
            @PathVariable Long clientId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) InterviewType type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) InterviewStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InterviewSchedule> schedules = interviewScheduleService.getAll(search, clientId, type, startDate, endDate, status, page, size);
        return ResponseEntity.ok(schedules);
    }


}
