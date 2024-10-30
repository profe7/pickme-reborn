package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.models.dto.responses.CountVacancyApplicantPaginationResponse;
import me.pick.metrodata.models.dto.responses.CountVacancyApplicationResponse;
import me.pick.metrodata.models.dto.responses.ReadApplicantResponse;
import me.pick.metrodata.models.dto.responses.ReadVacancyDetailResponse;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.ApplicantRepository;
import me.pick.metrodata.repositories.VacancyRepository;
import me.pick.metrodata.utils.AnyUtil;
import me.pick.metrodata.utils.PageData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacancyServiceImpl implements VacancyService {
    
    @Autowired
    VacancyRepository vacancyRepository;

    @Autowired
    ApplicantRepository applicantRepository;

    private Pair<UriComponentsBuilder, Pageable> pageableAndUriBuilder(String title, String position, String expiredDate, String updatedAt, Integer currentPage, Integer perPage) {
        currentPage = (currentPage == null) ? 0 : currentPage;
        perPage = (perPage == null) ? 10 : perPage;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("")
                .queryParam("title", title)
                .queryParam("position", position)
                .queryParam("expiredDate", expiredDate)
                .queryParam("updatedAt", updatedAt);
        Pageable pageable = PageRequest.of(currentPage, perPage, Sort.by("createdAt").descending());
        return Pair.of(uriBuilder, pageable);
    }

    public Page<Vacancy> getAllAvailableVacancies(Integer page, Integer size) {
        List<Vacancy> vacancies = vacancyRepository.findOpenVacancies();
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), vacancies.size());

        return new PageImpl<>(vacancies.subList(start, end), pageable, vacancies.size());
    }

    @Override
    public List<String> getAllPositions() {
        return vacancyRepository.findDistinctPositions();
    }

    @Override
    public Page<Vacancy> searchVacanciesByTitle(String title, Integer page, Integer size) {
        return vacancyRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size));
    }

    @Override
    public Page<Vacancy> searchVacanciesByPosition(String position, Integer page, Integer size) {
        return vacancyRepository.findByPositionContainingIgnoreCase(position, PageRequest.of(page, size));
    }

    @Override
    public Optional<Vacancy> getVacancyById(Long id) {
        return vacancyRepository.findById(id);
    }

    public CountVacancyApplicantPaginationResponse getVacanciesWithTotalNominee(String timeInterval, Integer currentPage, Integer perPage) {
        currentPage = (currentPage == null) ? 0 : currentPage;
        perPage = (perPage == null) ? 5 : perPage;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("")
                .queryParam("timeInterval", timeInterval);
        Pageable pageable = PageRequest.of(currentPage, perPage, Sort.by("createdAt").descending());
        List<CountVacancyApplicationResponse> countJobApplicant = findVacanciesWithTotalNominee(pageable);
        List<CountVacancyApplicationResponse> jobApplicant = findVacanciesWithTotalNominee(null);
        int totalJob = jobApplicant.size();
        PageData pageData = AnyUtil.pagination(totalJob, currentPage, perPage, uriBuilder);
        
        return new CountVacancyApplicantPaginationResponse(pageData, countJobApplicant);
    }

    private List<CountVacancyApplicationResponse> findVacanciesWithTotalNominee(Pageable pageable) {
        return vacancyRepository.findVacancyWithTotalNominee(pageable)
                .stream()
                .map(this::mapToCountVacancyApplicantResponse)
                .collect(Collectors.toList());
    }

    private CountVacancyApplicationResponse mapToCountVacancyApplicantResponse(Object[] result) {
        Vacancy vacancy = (Vacancy) result[0];
        Long totalNominee = (Long) result[1];
        
        return new CountVacancyApplicationResponse(vacancy, totalNominee);
    }

    @Override
    public ReadVacancyDetailResponse getVacancyDetailWithApplicants(Long vacancyId, Long mitraId){
        Optional<Vacancy> vacancyOptional = getVacancyById(vacancyId);

        if (vacancyOptional.isPresent()){
            Vacancy vacancy = vacancyOptional.get();
            List<Applicant> applicants = applicantRepository.findApplicantsByVacancyIdAndMitraId(vacancyId, mitraId);

            List<ReadApplicantResponse> applicantResponses = applicants.stream()
                .map(applicant -> {
                    Talent talent = applicant.getTalent();
                    return new ReadApplicantResponse(
                        talent != null ? talent.getName() : null,
                        talent != null ? talent.getStatusCV().toString() : null
                    );
                }).collect(Collectors.toList());

            ReadVacancyDetailResponse vacancyResponse = new ReadVacancyDetailResponse();
            vacancyResponse.setTitle(vacancy.getTitle());
            vacancyResponse.setExpiredDate(vacancy.getExpiredDate());
            vacancyResponse.setDescription(vacancy.getDescription());
            vacancyResponse.setPosition(vacancy.getPosition());
            vacancyResponse.setApplicants(applicantResponses);

            return vacancyResponse;

        }

        return null;

    }
}