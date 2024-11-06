package me.pick.metrodata.services.vacancy;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.exceptions.client.ClientDoesNotExistException;
import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.IncompleteVacancyRequestException;
import me.pick.metrodata.exceptions.vacancy.VacancyNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyStatusDoesNotExistException;
import me.pick.metrodata.models.dto.requests.VacancyCreationRequest;
import me.pick.metrodata.models.entity.Client;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.ClientRepository;
import me.pick.metrodata.repositories.UserRepository;
import me.pick.metrodata.repositories.VacancyRepository;
import me.pick.metrodata.repositories.specifications.VacancySpecification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Override
    public Page<Vacancy> getOpenVacancies(Integer page, Integer size, String expiredDate, String updatedAt, String title, String position) {
        Specification<Vacancy> spec = VacancySpecification.searchSpecification(title, position, expiredDate, updatedAt, null);
        List<Vacancy> vacancies = vacancyRepository.findOpenVacancies(spec);
        return vacancyPaginationHelper(page, size, vacancies);
    }

    @Override
    public Page<Vacancy> getAll(String title, String position, String expiredDate, String updatedAt, String timeInterval, Integer page, Integer size) {
        Specification<Vacancy> spec = VacancySpecification.combinedSpecification(title, position, expiredDate, updatedAt, timeInterval, null);
        List<Vacancy> vacancies = vacancyRepository.findAll(spec);
        return vacancyPaginationHelper(page, size, vacancies);
    }

    @Override
    public Page<Vacancy> getAllRm(String title, String position, String expiredDate, String updatedAt, String timeInterval, Integer page, Integer size, Long clientId) {
        if (clientId == null) {
            throw new ClientDoesNotExistException(0L);
        }
        Client client = clientRepository.findClientById(clientId).orElseThrow(() -> new ClientDoesNotExistException(clientId));
        Specification<Vacancy> spec = VacancySpecification.combinedSpecification(title, position, expiredDate, updatedAt, timeInterval, client);
        List<Vacancy> vacancies = vacancyRepository.findAll(spec);
        return vacancyPaginationHelper(page, size, vacancies);
    }

    @Override
    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public void createVacancy(VacancyCreationRequest request) {
        User user = userRepository.findUserById(request.getClientUserId()).orElseThrow(() -> new UserDoesNotExistException(request.getClientUserId().toString()));
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
