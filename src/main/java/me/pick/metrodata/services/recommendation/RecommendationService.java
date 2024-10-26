package me.pick.metrodata.services.recommendation;

import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.dto.responses.RecommendationPaginationResponse;

import java.util.List;

public interface RecommendationService {
	RecommendationPaginationResponse getAll(Integer currentPage, Integer perPage);

	List<RecommendationGroupedResponse> getAllByInstituteOrUser();
}
