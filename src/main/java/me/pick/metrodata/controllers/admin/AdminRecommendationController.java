package me.pick.metrodata.controllers.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.responses.RecommendationResponse;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.dto.responses.VacancyApplicantsResponse;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.institute.InstituteService;
import me.pick.metrodata.services.recommendation.RecommendationService;
import me.pick.metrodata.services.user.UserService;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.services.vacancy.VacancyService;

@Controller
@RequestMapping("/admin/recommendation")
@AllArgsConstructor
public class AdminRecommendationController {

    private final UserService userService;
    private final RecommendationService recommendationService;
    private final TalentService talentService;
    private final VacancyService vacancyService;
    private final InstituteService instituteService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "recommendation");
        return "recommendation-admin/index";
    }

    @GetMapping("/api")
    // @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Map<String, Object>> getRecommendations(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<RecommendationResponse> recommendationPage = recommendationService.getFilteredRecommendation(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("recommendations", recommendationPage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "recommendation");
        model.addAttribute("recommendation", recommendationService.getRecommendationById(id));
        model.addAttribute("skills", recommendationService.getSkills());
        model.addAttribute("positions", recommendationService.getPositions(id));

        return "recommendation-admin/detail";
    }

    @GetMapping("/detail/talent/{id}")
    // @PreAuthorize("hasAnyAuthority('READ_JOB')")
    public ResponseEntity<Map<String, Object>> getDetailTalents(
            @PathVariable Long id,
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "searchPosition", required = false, defaultValue = "") String searchPosition,
            @RequestParam(value = "searchSkill", required = false, defaultValue = "") String searchSkill,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "3", required = false) Integer size) {

        Page<VacancyApplicantsResponse> responsePage = recommendationService
                .getRecommendationTalents(id, searchName, searchPosition, searchSkill, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("talents", responsePage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", responsePage.getTotalPages());
        response.put("totalItems", responsePage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    // @PreAuthorize("hasAnyAuthority('CREATE_JOB')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        try {
            recommendationService.deleteRecommendation(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/talent/{id}")
    // @PreAuthorize("hasAnyAuthority('READ_JOB')")
    public ResponseEntity<List<TalentResponse>> getTalents(@PathVariable Long id) {

        List<TalentResponse> talents = talentService.getTalentsByInstituteId(id);

        return ResponseEntity.ok(talents);
    }

    @GetMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_PARAMETER')")
    public String createForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("vacancies", vacancyService.getAll());
        model.addAttribute("institutes", instituteService.getAll());

        return "recommendation-admin/create";
    }
}
