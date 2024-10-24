package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.dto.requests.MultiTalentApplicantRequest;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.services.applicant.ApplicantService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicant")
public class RestApplicantController {
    private final ApplicantService applicantService;

    public RestApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @PostMapping("/apply-multiple-talents")
    @PreAuthorize("hasAnyAuthority('CREATE_APPLICANT', 'READ_JOB')")
    public ResponseEntity<Object> applyMultipleTalents(@RequestBody MultiTalentApplicantRequest request) {
        List<Applicant> applicants = applicantService.multiCreateApplicant(request);
        return ResponseHandler.generateResponse(new Response(
                "Applicants created", HttpStatus.CREATED, "SUCCESS", applicants
        ));
    }
}
