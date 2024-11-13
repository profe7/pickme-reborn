package me.pick.metrodata.services.recommendation;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.recommendation.RecommendationDoesNotExistException;
import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.dto.responses.RecommendationResponse;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.entity.Recommendation;
import me.pick.metrodata.models.entity.RecommendationApplicant;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.RecommendationApplicantRepository;
import me.pick.metrodata.repositories.RecommendationRepository;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.utils.AuthUtil;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RecommendationServiceImpl implements RecommendationService {

	private final RecommendationRepository recommendationRepository;
	private final TalentService talentService;
	private final RecommendationApplicantRepository recommendationApplicantRepository;
	private final ModelMapper modelMapper;

	@Override
	public Page<RecommendationResponse> getFilteredRecommendation(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return recommendationRepository.findAllWithFilters(pageable).map(recommendation -> {
			RecommendationResponse recommendationResponse = modelMapper.map(recommendation,
					RecommendationResponse.class);
			recommendationResponse
					.setAssignInstitute(
							recommendation.getVacancy().getClient().getUser().getInstitute().getInstituteName());
			recommendationResponse.setAssignDate(recommendation.getCreatedAt());
			recommendationResponse.setTotalTalents(recommendation.getTotalTalents());
			recommendationResponse
					.setPosition(recommendationApplicantRepository.findByRecommendationId(recommendation.getId())
							.stream()
							.map(RecommendationApplicant::getPosition)
							.findFirst()
							.orElse(null));
			return recommendationResponse;
		});
	}

	@Override
	public Page<RecommendationGroupedResponse> getAllByInstituteOrUser(Integer page, Integer size) {
		var userId = AuthUtil.getLoginUserId();
		var groupedResponses = new HashMap<String, RecommendationGroupedResponse>();
		var recommendations = recommendationRepository.findByUser_id(userId);

		if (recommendations == null || recommendations.isEmpty()) {
			return Page.empty();
		}
	
		for (Recommendation recommendation : recommendations) {
			Long recommendationId = recommendation.getId();
			Vacancy vacancy = recommendation.getVacancy();
			if (vacancy == null) continue;
	
			String position = vacancy.getPosition();
			Long vacancyId = vacancy.getId();
	
			RecommendationGroupedResponse groupedResponse = groupedResponses
					.getOrDefault(position,
							new RecommendationGroupedResponse(userId, position, vacancyId, new ArrayList<>()));

			List<RecommendationApplicant> applicants = recommendation.getRecommendationApplicants();
	
			if (applicants != null) {
				for (RecommendationApplicant applicant : applicants) {
					Talent talent = applicant.getApplicant().getTalent();
					if (talent == null) continue;
	
					TalentResponse talentResponse = talentService.getById(talent.getId());
					groupedResponse.getTalents().add(talentResponse);
				}
			}
	
			groupedResponses.put(position, groupedResponse);
		}

		List<RecommendationGroupedResponse> responseList = new ArrayList<>(groupedResponses.values());
		Pageable pageable = PageRequest.of(page, size);

		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), responseList.size());

		return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
	}

	@Override
	public void deleteRecommendation(Long id) {
		try {
			recommendationRepository.deleteById(id);
		} catch (Exception e) {
			throw new RecommendationDoesNotExistException(id);
		}
		recommendationApplicantRepository.deleteByRecommendation_Id(id);
	}
	
}
