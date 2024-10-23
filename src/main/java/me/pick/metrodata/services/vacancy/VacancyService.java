package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.models.entity.Vacancy;
import org.springframework.data.domain.Page;


public interface VacancyService {
    Page<Vacancy> getAllAvailableVacancies(Integer page, Integer size);
}
