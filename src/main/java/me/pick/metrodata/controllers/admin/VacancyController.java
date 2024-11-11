package me.pick.metrodata.controllers.admin;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.services.user.UserService;
import me.pick.metrodata.services.vacancy.VacancyService;

@Controller
@RequestMapping("/admin/vacancy")
@AllArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;
    private final UserService userService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        Account loggedAccount = (Account) session.getAttribute("loggedAccount");
        User loggedUser = userService.getById(loggedAccount.getId());

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

        Page<Vacancy> vacancyPage = vacancyService.getFilteredVacancy(searchTitle, searchPosition, date, status, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("vacancies", vacancyPage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", vacancyPage.getTotalPages());
        response.put("totalItems", vacancyPage.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
