package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class RestVacancyController {

    private final VacancyService vacancyService;

    private static final String SUCCESS = "SUCCESS";

    @GetMapping("/available-vacancies")
    @PreAuthorize("hasAnyAuthority('READ_JOB', 'CREATE_APPLICANT')")
    public ResponseEntity<Object> getAvailableVacancies(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String expiredDate,
            @RequestParam(defaultValue = "") String updatedAt,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String position) {
        return ResponseHandler.generateResponse(new Response(
                "Available vacancies", HttpStatus.OK, SUCCESS, vacancyService.getOpenVacancies(page, size, expiredDate, updatedAt, title, position)
        ));
    }

    @GetMapping("/available-vacancies/{id}")
    @PreAuthorize("hasAnyAuthority('READ_JOB', 'CREATE_APPLICANT')")
    public ResponseEntity<Object> getVacancyById(@PathVariable Long id) {
        return ResponseHandler.generateResponse(new Response(
                "Vacancy", HttpStatus.OK, SUCCESS, vacancyService.getVacancyById(id)
        ));
    }

    @PostMapping("/create-vacancy")
    @PreAuthorize("hasAnyAuthority('CREATE_JOB')")
    public ResponseEntity<Object> createVacancy(@RequestBody VacancyCreationRequest request) {
        vacancyService.createVacancy(request);
        return ResponseHandler.generateResponse(new Response(
                "Vacancy created", HttpStatus.CREATED, SUCCESS, null
        ));
    }

    @PostMapping("/edit-vacancy/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE_JOB')")
    public ResponseEntity<Object> editVacancy(@RequestBody VacancyCreationRequest request, @PathVariable Long id) {
        vacancyService.editVacancy(request, id);
        return ResponseHandler.generateResponse(new Response(
                "Vacancy edited", HttpStatus.OK, SUCCESS, null
        ));
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_JOB')")
    public ResponseEntity<Object> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseHandler.generateResponse(new Response(
                "Vacancy deleted", HttpStatus.OK, SUCCESS, null
        ));
    }

}
