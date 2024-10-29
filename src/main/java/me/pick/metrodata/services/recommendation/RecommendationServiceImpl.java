package me.pick.metrodata.services.recommendation;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.dto.responses.RecommendationPaginationResponse;
import me.pick.metrodata.models.dto.responses.RecommendationResponse;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.entity.Recommendation;
import me.pick.metrodata.models.entity.RecommendationApplicant;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.RecommendationRepository;
import me.pick.metrodata.repositories.specifications.RecommendationSpecification;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.utils.AnyUtil;
import me.pick.metrodata.utils.AuthUtil;
import me.pick.metrodata.utils.PageData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommendationServiceImpl implements RecommendationService {
	private final RecommendationRepository recommendationRepository;
	private final TalentService talentService;

	public RecommendationPaginationResponse getAll(Integer currentPage, Integer perPage) {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("");
		Pageable pageable = PageRequest.of(currentPage, perPage);
		currentPage = currentPage == null ? 0 : currentPage;
		perPage = perPage == null ? 10 : perPage;

		Specification<Recommendation> spec = RecommendationSpecification.searchSpecification();
		var recommendations = recommendationRepository.findAll(spec, pageable).getContent();
		int total = (int) recommendationRepository.count(spec);

		PageData pageData = AnyUtil.pagination(total, currentPage, perPage, uriBuilder);

		var response = new ArrayList<RecommendationResponse> ();
		for (Recommendation recommendation : recommendations) {
			List<TalentResponse> talents = new ArrayList<>();
			for (RecommendationApplicant applicants : recommendation.getRecommendationApplicants()) {
				talents.add(talentService.getById(applicants.getApplicant().getTalent().getId()));
			}
			response.add(new RecommendationResponse(
					recommendation.getId(),
					recommendation.getCreatedAt(),
					recommendation.getVacancy().getPosition(),
					recommendation.getVacancy().getId(),
					recommendation.getUser().getInstitute(),
					recommendation.getUser(),
					talents,
					recommendation.getDescription(),
					recommendation.getRecommendationApplicants().size()));
		}
		return new RecommendationPaginationResponse (pageData, response);
	}

	public List<RecommendationGroupedResponse> getAllByInstituteOrUser() {
		var userId = AuthUtil.getLoginUserId();
		var groupedResponses = new HashMap<String, RecommendationGroupedResponse> ();
		var recommendations = recommendationRepository.findByUser_id (userId);

		if (recommendations == null || recommendations.isEmpty()) {
			return new ArrayList<>();
		}

		for (Recommendation recommendation : recommendations) {
			Long id = recommendation.getId();
			Vacancy vacancy = recommendation.getVacancy();

			String position = vacancy.getPosition();
			Long vacancyId = vacancy.getId();

			RecommendationGroupedResponse groupedResponse = groupedResponses
					.getOrDefault(position, new RecommendationGroupedResponse(id, position, vacancyId, new ArrayList<>()));

			List<RecommendationApplicant> applicants = recommendation.getRecommendationApplicants();

			if (applicants != null) {
				for (RecommendationApplicant applicant : applicants) {
					Talent talent = applicant.getApplicant().getTalent();

					if (talent == null) {
						continue;
					}

					TalentResponse talentResponse = talentService.getById(talent.getId());
					groupedResponse.getTalents().add(talentResponse);
				}
			}

			groupedResponses.put(position, groupedResponse);
		}

		return new ArrayList<>(groupedResponses.values());
	}
}
