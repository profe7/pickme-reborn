package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long>, JpaSpecificationExecutor<Recommendation> {

    List<Recommendation> findByUser_id(Long rmId);

    @Query("SELECT r FROM Recommendation r WHERE r.vacancy.id = ?1")
    List<Recommendation> findRecommendationByVacancyId(Long vacancyId);

}
