package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.RecommendationApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationApplicantRepository extends JpaRepository<RecommendationApplicant, Long> {
    Optional<RecommendationApplicant> findByApplicantIdAndPosition(Long applicantId, String position);
}
