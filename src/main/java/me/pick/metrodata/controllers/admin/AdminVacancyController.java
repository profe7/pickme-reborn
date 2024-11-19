package me.pick.metrodata.controllers.admin;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.services.user.UserService;
import me.pick.metrodata.services.vacancy.VacancyService;

@Controller
@RequestMapping("/admin/vacancy")
@AllArgsConstructor
public class AdminVacancyController {

    private final VacancyService vacancyService;
    private final UserService userService;

    private static final String SUCCESS = "SUCCESS";

    @GetMapping
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "vacancy");
        return "vacancy-admin/index";
    }

    @GetMapping("/api")
    // @PreAuthorize("hasAnyAuthority('READ_JOB')")
    public ResponseEntity<Map<String, Object>> getVacancies(
            @RequestParam(value = "searchTitle", required = false, defaultValue = "") String searchTitle,
            @RequestParam(value = "searchPosition", required = false) String searchPosition,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(value = "status", required = false) VacancyStatus status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<Vacancy> vacancyPage = vacancyService.getFilteredVacancy(searchTitle, searchPosition, date, status, page,
                size);

        Map<String, Object> response = new HashMap<>();
        response.put("vacancies", vacancyPage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", vacancyPage.getTotalPages());
        response.put("totalItems", vacancyPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-rm-vacancies")
    @PreAuthorize("hasAnyAuthority('EXTERNAL_READ_ACCOUNT')")
    public ResponseEntity<Object> getAllRm(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String position,
            @RequestParam(defaultValue = "") String expiredDate,
            @RequestParam(defaultValue = "") String updatedAt,
            @RequestParam(defaultValue = "") String timeInterval,
            @RequestParam(defaultValue = "") Long clientId) {
        return ResponseHandler.generateResponse(new Response(
                "All vacancies", HttpStatus.OK, SUCCESS,
                vacancyService.getAllRm(title, position, expiredDate, updatedAt, timeInterval, page, size, clientId)));
    }

    @GetMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_JOB')")
    public String createForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("jobStatus", VacancyStatus.values());

        return "vacancy-admin/create";
    }

    @GetMapping("/update/{id}")
    // @PreAuthorize("hasAnyAuthority('UPDATE_JOB')")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        Vacancy vacancy = vacancyService.getVacancyById(id);

        model.addAttribute("logged", loggedUser);
        model.addAttribute("job", vacancy);
        model.addAttribute("currentStatus", vacancy.getStatus() != null ? vacancy.getStatus().toString() : null);
        model.addAttribute("jobStatus", VacancyStatus.values());

        return "vacancy-admin/update";
    }
}
