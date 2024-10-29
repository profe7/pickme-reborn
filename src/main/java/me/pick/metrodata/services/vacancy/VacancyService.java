package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.models.dto.responses.CountVacancyApplicantPaginationResponse;
import me.pick.metrodata.models.entity.Vacancy;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VacancyService {
    Page<Vacancy> getAllAvailableVacancies(Integer page, Integer size);

    Vacancy getVacancyById(Long id);
}
