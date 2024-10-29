package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.VacancyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;

    public VacancyServiceImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Override
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
}
