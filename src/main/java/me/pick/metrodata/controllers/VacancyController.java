package me.pick.metrodata.controllers;

import me.pick.metrodata.models.dto.responses.ReadVacancyDetailResponse;
import me.pick.metrodata.models.dto.responses.TalentAvailableForVacancyResponse;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.services.vacancy.VacancyService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    private final TalentService talentService;

    @GetMapping("/vacancies")
    public String getVacanciesPage(Model model,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) String position,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
    
            Page<Vacancy> vacancyPage;

        if (title != null && !title.isEmpty()) {
            vacancyPage = vacancyService.searchVacanciesByTitle(title, page, size);
        } else if (position != null && !position.isEmpty()) {
            vacancyPage = vacancyService.searchVacanciesByPosition(position, page, size);
        } else {
            vacancyPage = vacancyService.getAllAvailableVacancies(page, size);
        }

        model.addAttribute("vacancies", vacancyPage.getContent());
        model.addAttribute("positions", vacancyService.getAllPositions());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacancyPage.getTotalPages());

        model.addAttribute("title", title);
        model.addAttribute("position", position);

    return "vacancy/vacancies"; 

    }

    @GetMapping("vacancies/{mitraId}/{vacancyId}")
    public String detailVacanciesPage(Model model, @PathVariable("mitraId") Long mitraId, @PathVariable("vacancyId") Long vacancyId){
        ReadVacancyDetailResponse vacancyDetail = vacancyService.getVacancyDetailWithApplicants(vacancyId, mitraId);

        if (vacancyDetail != null){
            model.addAttribute("detailVacancy", vacancyDetail);
        }

        return "vacancy/vacancy-detail";
    }

    @GetMapping("vacancies/{mitraId}/{vacancyId}/talent")
    public ResponseEntity<TalentAvailableForVacancyResponse> getCompleteTalentListByMitra(@PathVariable("mitraId") Long mitraId, @PathVariable("vacancyId") Long vacancyId){
        TalentAvailableForVacancyResponse listTalentComplete = talentService.availableForVacancy(vacancyId, mitraId);

        return ResponseEntity.ok(listTalentComplete);
    }

}
    