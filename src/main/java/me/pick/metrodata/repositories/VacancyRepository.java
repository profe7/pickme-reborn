package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Vacancy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {

    Optional<Vacancy> findVacancyById(Long id);

    @Query("SELECT v FROM Vacancy v WHERE v.status != 'CLOSED'")
    List<Vacancy> findOpenVacancies(Specification<Vacancy> spec);

    @Query("SELECT v, COUNT(a.id) AS totalNominee "
            + "FROM Vacancy v "
            + "LEFT JOIN v.applicants a "
            + "GROUP BY v")
    List<Object[]> findVacancyWithTotalNominee(
            Pageable pageable);

    List<Vacancy> findTop5ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(v) FROM Vacancy v WHERE v.expiredDate > CURRENT_DATE")
    Long countActiveVacancy();

}
