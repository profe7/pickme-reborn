package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;
import me.pick.metrodata.models.entity.Vacancy;
import org.springframework.data.domain.Page;

public interface VacancyService {

    Page<Vacancy> getOpenVacancies(Integer page, Integer size);

    Page<Vacancy> getAll(String title, String position, String expiredDate, String updatedAt, String timeInterval, Integer page, Integer size);

    Vacancy getVacancyById(Long id);

    void createVacancy(VacancyCreationRequest request);

    void editVacancy(VacancyCreationRequest request, Long id);
}
