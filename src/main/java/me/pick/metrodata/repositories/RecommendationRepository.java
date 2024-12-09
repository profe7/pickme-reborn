package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Recommendation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long>, JpaSpecificationExecutor<Recommendation> {

    @Query("SELECT r FROM Recommendation r WHERE r.vacancy.id = ?1")
    List<Recommendation> findRecommendationByVacancyId(Long vacancyId);

    @Query("SELECT r FROM Recommendation r")
    Page<Recommendation> findAllWithFilters(Pageable pageable);

    @Query("SELECT r FROM Recommendation r WHERE r.vacancy.position = ?1")
    List<Recommendation> findRecommendationByVacancy(String vacancy);
}
