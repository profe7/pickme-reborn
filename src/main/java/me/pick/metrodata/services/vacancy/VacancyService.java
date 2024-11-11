package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;
import me.pick.metrodata.models.entity.Vacancy;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

public interface VacancyService {

    Page<Vacancy> getAllAvailableVacancies(Integer page, Integer size);

    Vacancy getVacancyById(Long id);

    void createVacancy(VacancyCreationRequest request);

    Page<Vacancy> getFilteredVacancy(String searchTitle, String searchPosition, LocalDate date, VacancyStatus status, Integer page, Integer size);
}
