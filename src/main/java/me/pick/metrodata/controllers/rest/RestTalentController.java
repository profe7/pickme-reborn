package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.requests.TalentDataCompletionRequest;
import me.pick.metrodata.models.dto.requests.TalentFromVacancyRequest;
import me.pick.metrodata.models.dto.responses.TalentAvailableForVacancyResponse;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/talent")
@AllArgsConstructor
public class RestTalentController {

    private final TalentService talentService;

    @PostMapping("/create-via-vacancy")
    @PreAuthorize("hasAnyAuthority('CREATE_TALENT', 'CREATE_APPLICANT')")
    public ResponseEntity<Object> createTalentViaVacancy(@RequestBody TalentFromVacancyRequest request) {
        Talent talent = talentService.createViaVacancy(request);
        return ResponseHandler.generateResponse(new Response(
                "Talent created", HttpStatus.CREATED, "SUCCESS", talent));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        return ResponseHandler.generateResponse(new Response(
                "Talent fetched", HttpStatus.OK, "SUCCESS", talentService.getTalentDetail(id)));
    }

    @PostMapping("/create-new-talent")
    @PreAuthorize("hasAnyAuthority('CREATE_TALENT')")
    public ResponseEntity<Object> createTalent(@RequestBody TalentDataCompletionRequest request) {
        Talent talent = talentService.createNewTalent(request);
        return ResponseHandler.generateResponse(new Response(
                "Talent created", HttpStatus.CREATED, "SUCCESS", talent));
    }

    @PostMapping("/edit-talent")
    @PreAuthorize("hasAnyAuthority('READ_TALENT', 'UPDATE_TALENT')")
    public ResponseEntity<Object> completeNewTalentData(@RequestBody TalentDataCompletionRequest request) {
        Talent talent = talentService.completeNewTalentData(request);
        return ResponseHandler.generateResponse(new Response(
                "Talent data completed", HttpStatus.OK, "SUCCESS", talent));
    }

    @GetMapping("/available-for-vacancy/{vacancyId}/{mitraId}")
    @PreAuthorize("hasAnyAuthority('READ_TALENT', 'READ_JOB', 'CREATE_APPLICANT')")
    public ResponseEntity<Object> availableForVacancy(@PathVariable Long vacancyId, @PathVariable Long mitraId) {
        TalentAvailableForVacancyResponse talents = talentService.availableForVacancy(vacancyId, mitraId);
        return ResponseHandler.generateResponse(new Response(
                "Talents available for vacancy", HttpStatus.OK, "SUCCESS", talents));
    }

    @GetMapping("/all-talents")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT', 'EXTERNAL_READ_ACCOUNT')")
    public ResponseEntity<Object> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "institute", required = false) Long institute,
            @RequestParam(value = "baseSalary", required = false) Long baseSalary,
            @RequestParam(value = "limitSalary", required = false) Long limitSalary,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "job", required = false) String job,
            @RequestParam(value = "skill", required = false) String skill,
            @RequestParam(value = "idle", required = false) Boolean idle
    ) {
        return ResponseHandler.generateResponse(new Response(
                "Talents fetched", HttpStatus.OK, "SUCCESS",
                talentService.getAll(page, size, search, institute, baseSalary, limitSalary, active, job, skill, idle)));
    }

}

