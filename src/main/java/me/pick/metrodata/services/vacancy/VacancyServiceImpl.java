package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.models.dto.responses.ReadApplicantResponse;
import me.pick.metrodata.models.dto.responses.ReadVacancyDetailResponse;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.ApplicantRepository;
import me.pick.metrodata.repositories.VacancyRepository;
import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.IncompleteVacancyRequestException;
import me.pick.metrodata.exceptions.vacancy.VacancyNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyStatusDoesNotExistException;
import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;
import me.pick.metrodata.models.entity.Client;
import me.pick.metrodata.models.entity.Mitra;
import me.pick.metrodata.models.entity.User;

import me.pick.metrodata.repositories.UserRepository;

import me.pick.metrodata.repositories.ClientRepository;
import me.pick.metrodata.repositories.specifications.VacancySpecification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;
    private final ClientRepository clientRepository;

    @Override
    public Page<Vacancy> getOpenVacancies(Integer page, Integer size, String expiredDate, String updatedAt,
            String title, String position) {
        Specification<Vacancy> spec = VacancySpecification.searchSpecification(title, position, expiredDate, updatedAt,
                null);
        List<Vacancy> vacancies = vacancyRepository.findOpenVacancies(spec);
        return vacancyPaginationHelper(page, size, vacancies);
    }

    @Override
    public Page<Vacancy> getAllRm(String title, String position, String expiredDate, String updatedAt,
            String timeInterval, Integer page, Integer size, Long clientId) {
        if (clientId == null) {
            throw new ClientDoesNotExistException(0L);
        }
        Client client = clientRepository.findClientById(clientId)
                .orElseThrow(() -> new ClientDoesNotExistException(clientId));
        Specification<Vacancy> spec = VacancySpecification.combinedSpecification(title, position, expiredDate,
                updatedAt, timeInterval, client);
        List<Vacancy> vacancies = vacancyRepository.findAll(spec);
        return vacancyPaginationHelper(page, size, vacancies);
    }

    @Override
    public List<String> getAllPositions() {
        return vacancyRepository.findDistinctPositions();
    }

    @Override
    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public ReadVacancyDetailResponse getVacancyDetailWithApplicants(Long vacancyId, Long mitraId) {
        Optional<Vacancy> vacancyOptional = vacancyRepository.findById(vacancyId);

        if (vacancyOptional.isPresent()) {
            Vacancy vacancy = vacancyOptional.get();
            List<Applicant> applicants = applicantRepository.findApplicantsByVacancyIdAndMitraId(vacancyId, mitraId);

            List<Applicant> filtered = applicants.stream()
                    .filter(applicant -> applicant.getTalent().getApplicants().stream()
                            .noneMatch(applicant1 -> applicant1.getStatus() == ApplicantStatus.ACCEPTED))
                    .toList();

            List<ReadApplicantResponse> applicantResponses = filtered.stream()
                    .map(applicant -> {
                        Talent talent = applicant.getTalent();
                        Mitra mitra = talent.getMitra();
                        return new ReadApplicantResponse(
                                mitra != null ? mitra.getId() : null,
                                talent != null ? talent.getName() : null,
                                talent != null ? talent.getStatusCV().toString() : null);
                    }).toList();

            ReadVacancyDetailResponse vacancyResponse = new ReadVacancyDetailResponse();
            vacancyResponse.setVacancyId(vacancyId);
            vacancyResponse.setTitle(vacancy.getTitle());
            vacancyResponse.setExpiredDate(vacancy.getExpiredDate());
            vacancyResponse.setDescription(vacancy.getDescription());
            vacancyResponse.setPosition(vacancy.getPosition());
            vacancyResponse.setApplicants(applicantResponses);

            return vacancyResponse;

        }

        return null;

    }

    @Override
    public void createVacancy(VacancyCreationRequest request) {
        User user = userRepository.findUserById(request.getClientUserId())
                .orElseThrow(() -> new UserDoesNotExistException(request.getClientUserId().toString()));
        try {
            VacancyStatus.valueOf(request.getVacancyStatus());
        } catch (IllegalArgumentException e) {
            throw new VacancyStatusDoesNotExistException(request.getVacancyStatus());
        }

        if (request.getClientUserId() == null
                || request.getVacancyTitle() == null
                || request.getVacancyPosition() == null
                || request.getVacancyStatus() == null
                || request.getVacancyEndDate() == null
                || request.getApplicantQuantity() == null
                || request.getVacancyDescription() == null) {
            throw new IncompleteVacancyRequestException();
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

    @Override
    public Page<Vacancy> getFilteredVacancy(String searchTitle, String searchPosition, LocalDate date,
            VacancyStatus status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return vacancyRepository.findAllWithFilters(searchTitle, searchPosition, date, status, pageable);
    }

    @Override
    public void editVacancy(VacancyCreationRequest request, Long id) {
        try {
            VacancyStatus.valueOf(request.getVacancyStatus());
        } catch (IllegalArgumentException e) {
            throw new VacancyStatusDoesNotExistException(request.getVacancyStatus());
        }
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow(() -> new VacancyNotExistException(id));
        vacancy.setTitle(request.getVacancyTitle());
        vacancy.setPosition(request.getVacancyPosition());
        vacancy.setStatus(VacancyStatus.valueOf(request.getVacancyStatus()));
        vacancy.setExpiredDate(request.getVacancyEndDate());
        vacancy.setRequiredPositions(request.getApplicantQuantity());
        vacancy.setDescription(request.getVacancyDescription());
        vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteVacancy(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow(() -> new VacancyNotExistException(id));
        vacancyRepository.delete(vacancy);
    }

    private Page<Vacancy> vacancyPaginationHelper(Integer page, Integer size, List<Vacancy> vacancies) {
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), vacancies.size());

        return new PageImpl<>(vacancies.subList(start, end), pageable, vacancies.size());
    }
}
