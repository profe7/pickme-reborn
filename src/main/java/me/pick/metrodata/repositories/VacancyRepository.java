package me.pick.metrodata.repositories;

import me.pick.metrodata.enums.VacancyStatus;
import me.pick.metrodata.models.entity.Vacancy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {

        Optional<Vacancy> findVacancyById(Long id);

        @Query("SELECT v FROM Vacancy v WHERE v.status != 'CLOSED'")
        List<Vacancy> findOpenVacancies();

        @Query("SELECT DISTINCT v.position FROM Vacancy v WHERE v.status != 'CLOSED'")
        List<String> findDistinctPositions();

        Page<Vacancy> findByTitleContainingIgnoreCase(String title, Pageable pageable);

        Page<Vacancy> findByPositionContainingIgnoreCase(String position, Pageable pageable);

        @Query("SELECT v FROM Vacancy v WHERE v.status != 'CLOSED'")
        List<Vacancy> findOpenVacancies(Specification<Vacancy> spec);

        @Query("SELECT v, COUNT(a.id) AS totalNominee " +
                        "FROM Vacancy v " +
                        "LEFT JOIN v.applicants a " +
                        "GROUP BY v")
        List<Object[]> findVacancyWithTotalNominee(
                        Pageable pageable);

        List<Vacancy> findTop5ByOrderByCreatedAtDesc();

        @Query("SELECT COUNT(v) FROM Vacancy v WHERE v.expiredDate > CURRENT_DATE")
        Long countActiveVacancy();

        @Query("SELECT v FROM Vacancy v WHERE "
                        + "(:searchTitle IS NULL OR v.title LIKE %:searchTitle%) AND "
                        + "(:searchPosition IS NULL OR v.position LIKE %:searchPosition%) AND "
                        + "(:date IS NULL OR v.expiredDate >= :date) AND "
                        + "(:status IS NULL OR v.status = :status)")
        Page<Vacancy> findAllWithFilters(
                        @Param("searchTitle") String searchTitle,
                        @Param("searchPosition") String searchPosition,
                        @Param("date") LocalDate date,
                        @Param("status") VacancyStatus status,
                        Pageable pageable);
}
