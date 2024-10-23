package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long>, JpaSpecificationExecutor<Recommendation> {

        List<Recommendation> findByUser_id(Long rmId);

}
