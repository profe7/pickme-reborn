package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.models.dto.responses.ReadVacancyDetailResponse;
import me.pick.metrodata.models.entity.Vacancy;
import org.springframework.data.domain.Page;
import java.util.List;

import java.util.Optional;
import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;

public interface VacancyService {
    Page<Vacancy> getAllAvailableVacancies(Integer page, Integer size);
    List<String> getAllPositions();

    Page<Vacancy> searchVacanciesByTitle(String title, Integer page, Integer size);
    Page<Vacancy> searchVacanciesByPosition(String position, Integer page, Integer size);    

    Optional<Vacancy> getVacancyById(Long id);

    ReadVacancyDetailResponse getVacancyDetailWithApplicants(Long vacancyId, Long mitraId);

    void createVacancy(VacancyCreationRequest request);
}
   
