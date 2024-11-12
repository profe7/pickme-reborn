package me.pick.metrodata.services.recommendation;

import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.dto.responses.RecommendationResponse;

import java.util.List;

import org.springframework.data.domain.Page;

public interface RecommendationService {

	List<RecommendationGroupedResponse> getAllByInstituteOrUser();

	Page<RecommendationResponse> getFilteredRecommendation(Integer page,
			Integer size);
}
