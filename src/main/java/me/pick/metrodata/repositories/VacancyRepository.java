package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Vacancy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;

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


        @Query("SELECT v, COUNT(a.id) AS totalNominee " +
                "FROM Vacancy v " +
                "LEFT JOIN v.applicants a " +
                "GROUP BY v")
        List<Object[]> findVacancyWithTotalNominee(
                Pageable pageable);

        @Query("SELECT COUNT(v) FROM Vacancy v WHERE v.expiredDate > CURRENT_DATE")
        Long countActiveVacancy();



}
