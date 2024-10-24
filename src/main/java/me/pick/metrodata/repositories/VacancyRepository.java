package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Vacancy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {

        Optional<Vacancy> findVacancyById(Long id);

        List<Vacancy> findByClientId(Long clientId);

        @Query("SELECT v FROM Vacancy v WHERE v.status != 'CLOSED'")
        List<Vacancy> findOpenVacancies();

}