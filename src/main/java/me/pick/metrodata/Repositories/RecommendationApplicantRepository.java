package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.RecommendationApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationApplicantRepository extends JpaRepository<RecommendationApplicant, Long> {
}
