package me.pick.metrodata.controllers.rest;

import me.pick.metrodata.services.vacancy.VacancyService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
