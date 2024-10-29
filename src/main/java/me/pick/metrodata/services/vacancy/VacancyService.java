package me.pick.metrodata.services.vacancy;

import me.pick.metrodata.models.entity.Vacancy;
import org.springframework.data.domain.Page;
import java.util.List;


public interface VacancyService {
    Page<Vacancy> getAllAvailableVacancies(Integer page, Integer size);
    List<String> getAllPositions();

    Page<Vacancy> searchVacanciesByTitle(String title, Integer page, Integer size);
    Page<Vacancy> searchVacanciesByPosition(String position, Integer page, Integer size);    
}
   
