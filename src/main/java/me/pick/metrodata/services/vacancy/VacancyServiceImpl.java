package me.pick.metrodata.services.vacancy;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyStatusDoesNotExistException;
import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;
import me.pick.metrodata.models.dto.responses.CountVacancyApplicantPaginationResponse;
import me.pick.metrodata.models.dto.responses.CountVacancyApplicationResponse;
import me.pick.metrodata.models.dto.responses.VacancyPaginationResponse;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.UserRepository;
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
@RequiredArgsConstructor
public class    VacancyServiceImpl implements VacancyService{
    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;

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

    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    public void createVacancy(VacancyCreationRequest request) {
        User user = userRepository.findUserById(request.getClientUserId()).orElseThrow(() -> new UserDoesNotExistException(request.getClientUserId().toString()));
        try {
            VacancyStatus.valueOf(request.getVacancyStatus());
        } catch (IllegalArgumentException e) {
            throw new VacancyStatusDoesNotExistException(request.getVacancyStatus());
        }

        Vacancy vacancy = Vacancy.builder()
                .client(user.getClient())
                .title(request.getVacancyTitle())
                .position(request.getVacancyPosition())
                .status(VacancyStatus.valueOf(request.getVacancyStatus()))
                .expiredDate(request.getVacancyEndDate())
                .requiredPositions(request.getApplicantQuantity())
                .description(request.getVacancyDescription())
                .build();

        vacancyRepository.save(vacancy);
    }
}
