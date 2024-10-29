package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;
import me.pick.metrodata.services.vacancy.VacancyService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vacancy")
public class RestVacancyController {

    private final VacancyService vacancyService;

    public RestVacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping("/available-vacancies")
    @PreAuthorize("hasAnyAuthority('READ_JOB', 'CREATE_APPLICANT')")
    public ResponseEntity<Object> getAvailableVacancies(
            @RequestParam(defaultValue = "0")Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseHandler.generateResponse(new Response(
                "Available vacancies", HttpStatus.OK, "SUCCESS", vacancyService.getAllAvailableVacancies(page, size)
        ));
    }

    @GetMapping("/available-vacancies/{id}")
    @PreAuthorize("hasAnyAuthority('READ_JOB', 'CREATE_APPLICANT')")
    public ResponseEntity<Object> getVacancyById(@PathVariable Long id) {
        return ResponseHandler.generateResponse(new Response(
                "Vacancy", HttpStatus.OK, "SUCCESS", vacancyService.getVacancyById(id)
        ));
    }

    @PostMapping("/create-vacancy")
    @PreAuthorize("hasAnyAuthority('CREATE_JOB')")
    public ResponseEntity<Object> createVacancy(@RequestBody VacancyCreationRequest request) {
        vacancyService.createVacancy(request);
        return ResponseHandler.generateResponse(new Response(
                "Vacancy created", HttpStatus.CREATED, "SUCCESS", null
        ));
    }
}
