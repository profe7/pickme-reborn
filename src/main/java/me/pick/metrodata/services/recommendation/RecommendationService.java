package me.pick.metrodata.services.recommendation;

import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.dto.responses.RecommendationResponse;
import me.pick.metrodata.models.dto.responses.VacancyApplicantsResponse;

import java.util.List;

import org.springframework.data.domain.Page;

public interface RecommendationService {

	Page<RecommendationResponse> getFilteredRecommendation(Integer page,
			Integer size);

	void deleteRecommendation(Long id);

	Page<RecommendationGroupedResponse> getRecommendationClientPaged(Integer page, Integer size, Long clientId,
			String talentName, String position);

	List<String> getPositions(Long recommendationId);

	List<String> getSkills();

	Page<VacancyApplicantsResponse> getRecommendationTalents(Long recommendationId, String searchName,
			String searchPosition, String searchSkill, Integer page, Integer size);

	RecommendationResponse getRecommendationById(Long id);

}
