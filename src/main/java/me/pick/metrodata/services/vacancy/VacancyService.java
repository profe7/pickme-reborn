package me.pick.metrodata.services.vacancy;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;
import me.pick.metrodata.models.dto.requests.VacancyRequest;
import me.pick.metrodata.models.dto.responses.ReadVacancyDetailResponse;
import me.pick.metrodata.models.dto.responses.VacancyApplicantsResponse;
import me.pick.metrodata.models.entity.Vacancy;

public interface VacancyService {

        List<String> getAllPositions();

        Vacancy getVacancyById(Long id);

        Page<Vacancy> getOpenVacancies(Integer page, Integer size, String expiredDate, String updatedAt, String title,
                        String position);

        Page<Vacancy> getAllRm(String title, String position, String expiredDate, String updatedAt, String timeInterval,
                        Integer page, Integer size, Long clientId);

        ReadVacancyDetailResponse getVacancyDetailWithApplicants(Long vacancyId, Long mitraId);

        void createVacancy(VacancyCreationRequest request);

        Page<Vacancy> getFilteredVacancy(String searchTitle, String searchPosition, LocalDate date,
                        VacancyStatus status,
                        Integer page, Integer size);

        void editVacancy(VacancyCreationRequest request, Long id);

        void deleteVacancy(Long id);

        void create(Long userId, VacancyRequest vacancyRequest);

        void update(Long id, VacancyRequest vacancyRequest);

        void delete(Long id);

        Page<VacancyApplicantsResponse> getAppliedTalents(Long vacancyId, Integer page, Integer size,
                        String companyName);

        List<Vacancy> getAll();
}
