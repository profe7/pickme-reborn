package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.services.interview.InterviewScheduleService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interview")
public class RestInterviewController {
    private final InterviewScheduleService interviewScheduleService;

    public RestInterviewController(InterviewScheduleService interviewScheduleService) {
        this.interviewScheduleService = interviewScheduleService;
    }

    @PostMapping("/invite")
    @PreAuthorize("hasAnyAuthority('CREATE_INTERVIEW', 'UPDATE_INTERVIEW', 'READ_INTERVIEW')")
    public ResponseEntity<Object> inviteInterview(@RequestBody InterviewScheduleRequest request) {
        interviewScheduleService.inviteToInterview(request);
        return ResponseHandler.generateResponse(
                new Response(
                        "Applicant invited", HttpStatus.CREATED, "SUCCESS", null)
        );
    }
}
