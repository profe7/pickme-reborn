package me.pick.metrodata.controllers;

import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.services.vacancy.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

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

    return "vacancies"; 


    }
}
    