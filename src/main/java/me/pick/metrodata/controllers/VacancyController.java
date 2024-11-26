package me.pick.metrodata.controllers;

import me.pick.metrodata.enums.StatusCV;
import me.pick.metrodata.models.dto.requests.TalentFromVacancyRequest;
import me.pick.metrodata.models.dto.responses.ReadVacancyDetailResponse;
import me.pick.metrodata.models.dto.responses.TalentAvailableForVacancyResponse;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.services.vacancy.VacancyService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/vacancies")
@PreAuthorize("hasRole('ROLE_MITRA')")
@AllArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    private final TalentService talentService;

    @GetMapping
    public String getVacanciesPage(Model model,HttpSession session,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) String position,
                                    @RequestParam(required = false) String expiredDate,
                                    @RequestParam(required = false) String updateAt,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {

        Page<Vacancy> vacancyPage = vacancyService.getOpenVacancies(page, size, expiredDate, updateAt, title, position);

        model.addAttribute("vacancies", vacancyPage.getContent());
        model.addAttribute("positions", vacancyService.getAllPositions());
        model.addAttribute("currentPage", "/vacancies");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacancyPage.getTotalPages());
        model.addAttribute("expiredDate", expiredDate);
        model.addAttribute("updateAt", updateAt);

        return "mitra/vacancies-mitra";
    }

    @GetMapping("/search")
    public String getFilteredVacanciesPage(Model model, HttpSession session,
                                            @RequestParam(required = false) String title,
                                            @RequestParam(required = false) String position,
                                            @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size) {

        Page<Vacancy> vacancyPage = vacancyService.getFilteredVacancy(title, position, null, null, page, size);

        model.addAttribute("vacancies", vacancyPage.getContent());
        model.addAttribute("positions", vacancyService.getAllPositions());
        model.addAttribute("currentPage", "/vacancies/search");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacancyPage.getTotalPages());
        model.addAttribute("title", title);
        model.addAttribute("position", position);

        return "mitra/vacancies-mitra";
    }

    @GetMapping("/{vacancyId}")
    public String detailVacanciesPage(Model model, @PathVariable("vacancyId") Long vacancyId, HttpSession httpSession){
        Long mitraId = (Long) httpSession.getAttribute("mitraId");
        ReadVacancyDetailResponse vacancyDetail = vacancyService.getVacancyDetailWithApplicants(vacancyId, mitraId);

        if (vacancyDetail != null) {
            model.addAttribute("detailVacancy", vacancyDetail);
            model.addAttribute("mitraId", mitraId);
            model.addAttribute("vacancyId", vacancyId);

        }

        return "mitra/vacancy-detail-mitra";
    }

    @GetMapping("/{mitraId}/{vacancyId}/talent")
    public ResponseEntity<TalentAvailableForVacancyResponse> getCompleteTalentListByMitra(@PathVariable("vacancyId") Long vacancyId, HttpSession httpSession) 
    {
        Long mitraId = (Long) httpSession.getAttribute("mitraId");
        TalentAvailableForVacancyResponse listTalentComplete = talentService.availableForVacancy(vacancyId, mitraId);
        return ResponseEntity.ok(listTalentComplete);
    }

    @PostMapping("/applyNewTalent")
    public ResponseEntity<String> applyNewTalent(@RequestBody TalentFromVacancyRequest request) {
        try {
            talentService.createViaVacancy(request);
            return ResponseEntity.ok("Talent successfully applied");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan saat mendaftarkan talent");
        }
    }

   

}
