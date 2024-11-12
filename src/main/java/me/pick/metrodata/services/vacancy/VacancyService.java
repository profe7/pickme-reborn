package me.pick.metrodata.services.vacancy;


import me.pick.metrodata.models.dto.responses.ReadVacancyDetailResponse;
import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;


public interface VacancyService {

    Page<Vacancy> getAllAvailableVacancies(Integer page, Integer size);
  
    List<String> getAllPositions();

    Page<Vacancy> searchVacanciesByTitle(String title, Integer page, Integer size);
  
    Page<Vacancy> searchVacanciesByPosition(String position, Integer page, Integer size);    

    Optional<Vacancy> getVacancyById(Long id);

    Page<Vacancy> getOpenVacancies(Integer page, Integer size, String expiredDate, String updatedAt, String title, String position);

    Page<Vacancy> getAllRm(String title, String position, String expiredDate, String updatedAt, String timeInterval, Integer page, Integer size, Long clientId);

    ReadVacancyDetailResponse getVacancyDetailWithApplicants(Long vacancyId, Long mitraId);

    void createVacancy(VacancyCreationRequest request);

    Page<Vacancy> getFilteredVacancy(String searchTitle, String searchPosition, LocalDate date, VacancyStatus status, Integer page, Integer size);
  
    void editVacancy(VacancyCreationRequest request, Long id);

    void deleteVacancy(Long id);
}
   
