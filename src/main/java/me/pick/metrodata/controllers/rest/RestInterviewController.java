package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.services.interview.InterviewScheduleService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all-interview")
    @PreAuthorize("hasAnyAuthority('CREATE_INTERVIEW', 'UPDATE_INTERVIEW', 'READ_INTERVIEW')")
    public ResponseEntity<Object> getAllInterviews (
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) InterviewType type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false)InterviewStatus status
            ) {
        return ResponseHandler.generateResponse(new Response(
                "Interviews", HttpStatus.OK, "SUCCESS", interviewScheduleService.getAll(search, clientId, type, startDate, endDate, status, page, size)
        ));
    }

    @GetMapping("/all-interview-rm")
    @PreAuthorize("hasAnyAuthority('CREATE_INTERVIEW', 'UPDATE_INTERVIEW', 'READ_INTERVIEW')")
    public ResponseEntity<Object> getAllInterviewsRm(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam Long clientId,
            @RequestParam(required = false) InterviewType type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false)InterviewStatus status
    ) {
        return ResponseHandler.generateResponse(new Response(
                "Interviews", HttpStatus.OK, "SUCCESS", interviewScheduleService.getByRm(search, clientId, type, startDate, endDate, status, page, size)
        ));
    }

    @GetMapping("/talent-interview-history/{interviewId}")
    @PreAuthorize("hasAnyAuthority('READ_INTERVIEW')")
    public ResponseEntity<Object> getTalentInterviewHistory(@PathVariable Long interviewId) {
        return ResponseHandler.generateResponse(new Response(
                "Interviews", HttpStatus.OK, "SUCCESS", interviewScheduleService.getTalentInterviewHistory(interviewId)
        ));
    }
}
