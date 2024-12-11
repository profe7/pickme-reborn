package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.requests.MultiTalentApplicantRequest;
import me.pick.metrodata.models.dto.requests.RecommendApplicantRequest;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.services.applicant.ApplicantService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicant")
@AllArgsConstructor
public class RestApplicantController {

    private final ApplicantService applicantService;

    private static final String SUCCESS = "SUCCESS";

    @PostMapping("/apply-multiple-talents")
    @PreAuthorize("hasAnyAuthority('CREATE_APPLICANT', 'READ_JOB')")
    public ResponseEntity<Object> applyMultipleTalents(@RequestBody MultiTalentApplicantRequest request) {
        List<Applicant> applicants = applicantService.multiCreateApplicant(request);
        return ResponseHandler.generateResponse(new Response(
                "Applicants created", HttpStatus.CREATED, SUCCESS, null
        ));
    }

    @PostMapping("/recommend-applicant")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public ResponseEntity<Object> recommendApplicant(@RequestBody RecommendApplicantRequest request) {
        return ResponseHandler.generateResponse(new Response(
                "Applicant recommended", HttpStatus.OK, SUCCESS, applicantService.recommendApplicant(request)
        ));
    }

    @GetMapping("/recommended-applicants/{vacancyId}")
    @PreAuthorize("hasAnyAuthority('CREATE_INTERVIEW', 'READ_INTERVIEW', 'UPDATE_INTERVIEW')")
    public ResponseEntity<Object> getRecommendedApplicant(@PathVariable Long vacancyId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseHandler.generateResponse(new Response(
                "Recommended applicants", HttpStatus.OK, SUCCESS, applicantService.getRecommendedApplicant(vacancyId, page, size)
        ));
    }
}
