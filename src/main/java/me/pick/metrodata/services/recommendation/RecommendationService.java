package me.pick.metrodata.services.recommendation;

import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.dto.responses.RecommendationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecommendationService {

	Page<RecommendationResponse> getFilteredRecommendation(Integer page,
			Integer size);

	void deleteRecommendation(Long id);

	Page<RecommendationGroupedResponse> getRecommendationClientPaged(Integer page, Integer size, Long clientId, String talentName, String position);
}
