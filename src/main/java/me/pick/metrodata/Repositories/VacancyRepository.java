package me.pick.metrodata.Repositories;

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

        @Query("SELECT j, COUNT(a.id) AS total_nominee " +
                        "FROM Vacancy j " +
                        "LEFT JOIN j.applicants a " +
                        "WHERE :timeInterval IS NULL OR " +
                        "(:timeInterval = 'hari' AND a.createdAt >= CURRENT_DATE) OR " +
                        "(:timeInterval = 'minggu' AND a.createdAt >= CURRENT_DATE - 7) OR " +
                        "(:timeInterval = 'bulan' AND a.createdAt >= CURRENT_DATE - 30) " +
                        "GROUP BY j.id, j.title")
        List<Object[]> findVacancyWithTotalNominee(
                        @Param("timeInterval") String timeInterval,
                        Pageable pageable);

        Optional<Vacancy> findVacancyById(Long id);

        List<Vacancy> findByClientId(Long clientId);

}