package me.pick.metrodata.controllers.admin;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.models.dto.requests.VacancyRequest;
import me.pick.metrodata.models.dto.responses.VacancyApplicantsResponse;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.services.institute.InstituteService;
import me.pick.metrodata.services.user.UserService;
import me.pick.metrodata.services.vacancy.VacancyService;

@Controller
@RequestMapping("/admin/vacancy")
@AllArgsConstructor
public class AdminVacancyController {

    private final VacancyService vacancyService;
    private final UserService userService;
    private final InstituteService instituteService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "vacancy");
        model.addAttribute("statuses", VacancyStatus.values());
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

    @GetMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_JOB')")
    public String createForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "vacancy");
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
        model.addAttribute("isActive", "vacancy");
        model.addAttribute("jobStatus", VacancyStatus.values());

        return "vacancy-admin/update";
    }

    @PostMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_JOB')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody VacancyRequest vacancyRequest,
            HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));
            vacancyService.create(loggedUser.getId(), vacancyRequest);
            response.put("message", "Lowongan baru berhasil ditambahkan");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat menambahkan lowongan baru");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    // @PreAuthorize("hasAnyAuthority('CREATE_JOB')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody VacancyRequest vacancyRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            vacancyService.update(id, vacancyRequest);
            response.put("message", "Lowongan berhasil diperbarui");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat memperbarui lowongan");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    // @PreAuthorize("hasAnyAuthority('CREATE_JOB')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        try {
            vacancyService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/applied/{id}")
    public String appliedForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "vacancy");
        model.addAttribute("vacancy", vacancyService.getVacancyById(id));
        model.addAttribute("institutes", instituteService.getAllInstituteByVacancyId(id));

        return "vacancy-admin/applied";
    }

    @GetMapping("/applied/applicant/{id}")
    // @PreAuthorize("hasAnyAuthority('READ_JOB')")
    public ResponseEntity<Map<String, Object>> getApplicant(
            @PathVariable Long id,
            @RequestParam(value = "searchInstitute", required = false, defaultValue = "") String searchInstitute,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "3", required = false) Integer size) {

        Page<VacancyApplicantsResponse> vacancyPage = vacancyService.getAppliedTalents(id, page, size, searchInstitute);

        Map<String, Object> response = new HashMap<>();
        response.put("applicants", vacancyPage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", vacancyPage.getTotalPages());
        response.put("totalItems", vacancyPage.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
