package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.models.dto.responses.CountVacancyApplicantPaginationResponse;
import me.pick.metrodata.models.dto.responses.CountVacancyApplicationResponse;
import me.pick.metrodata.models.dto.responses.VacancyPaginationResponse;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.VacancyRepository;
import me.pick.metrodata.repositories.specifications.VacancySpecification;
import me.pick.metrodata.utils.AnyUtil;
import me.pick.metrodata.utils.PageData;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class    VacancyServiceImpl implements VacancyService{
    private final VacancyRepository vacancyRepository;

    public VacancyServiceImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    private Pair<UriComponentsBuilder, Pageable> pageableAndUriBuilder(String title, String position, String expiredDate, String updatedAt, Integer currentPage, Integer perPage) {
        currentPage = currentPage == null ? 0 : currentPage;
        perPage = perPage == null ? 10 : perPage;
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

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    public CountVacancyApplicantPaginationResponse getVacanciesWithTotalNominee(
            String timeInterval,
            Integer currentPage,
            Integer perPage
    ) {

        currentPage = currentPage == null ? 0 : currentPage;
        perPage = perPage == null ? 5 : perPage;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("")
                .queryParam("timeInterval", timeInterval);
        Pageable pageable = PageRequest.of(currentPage, perPage, Sort.by("createdAt").descending());

        List<CountVacancyApplicationResponse> countJobApplicant = findVacanciesWithTotalNominee(pageable);
        List<CountVacancyApplicationResponse> jobApplicant = findVacanciesWithTotalNominee(null);

        int totalJob = jobApplicant.size();

        PageData pageData = AnyUtil.pagination(totalJob, currentPage, perPage, uriBuilder);

        return new CountVacancyApplicantPaginationResponse (pageData, countJobApplicant);
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
        return new CountVacancyApplicationResponse (vacancy, totalNominee);
    }
    
}
