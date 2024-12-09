package me.pick.metrodata.services.recommendation;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.recommendation.RecommendationDoesNotExistException;
import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.dto.responses.RecommendationResponse;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.dto.responses.VacancyApplicantsResponse;
import me.pick.metrodata.models.entity.*;
import me.pick.metrodata.repositories.RecommendationApplicantRepository;
import me.pick.metrodata.repositories.ApplicantRepository;
import me.pick.metrodata.repositories.RecommendationRepository;
import me.pick.metrodata.repositories.SkillRepository;
import me.pick.metrodata.repositories.specifications.RecommendationSpecification;
import me.pick.metrodata.services.talent.TalentService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class RecommendationServiceImpl implements RecommendationService {

	private final RecommendationRepository recommendationRepository;
	private final TalentService talentService;
	private final RecommendationApplicantRepository recommendationApplicantRepository;
	private final ModelMapper modelMapper;
	private final SkillRepository skillRepository;
	private final ApplicantRepository applicantRepository;

	@Override
	public Page<RecommendationResponse> getFilteredRecommendation(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return recommendationRepository.findAllWithFilters(pageable).map(recommendation -> {
			RecommendationResponse recommendationResponse = modelMapper.map(recommendation,
					RecommendationResponse.class);
			recommendationResponse
					.setAssignInstitute(
							recommendation.getVacancy().getClient().getUser().getInstitute().getInstituteName());
			recommendationResponse.setAssignDate(recommendation.getCreatedAt().toLocalDate());
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
	public Page<RecommendationGroupedResponse> getRecommendationClientPaged(Integer page, Integer size, Long clientId,
			String talentName, String position) {
		return recommendationPaginationHelper(page, size, fetchClientRecommendation(clientId, talentName, position));
	}

	@Override
	public void deleteRecommendation(Long id) {
		try {
			recommendationApplicantRepository.deleteByRecommendation_Id(id);
			recommendationRepository.deleteById(id);
		} catch (Exception e) {
			throw new RecommendationDoesNotExistException(id);
		}
	}

	private Page<RecommendationGroupedResponse> recommendationPaginationHelper(Integer page, Integer size,
			List<RecommendationGroupedResponse> recommendations) {
		Pageable pageable = PageRequest.of(page, size);
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), recommendations.size());

		return new PageImpl<>(recommendations.subList(start, end), pageable, recommendations.size());
	}

	private List<RecommendationGroupedResponse> fetchClientRecommendation(Long clientId, String talentName,
			String position) {
		var groupedResponses = new HashMap<String, RecommendationGroupedResponse>();
		var specification = RecommendationSpecification.filterByTalentNameAndPosition(talentName, position)
				.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("clients").get("id"), clientId));
		var recommendations = recommendationRepository.findAll(specification);

		for (Recommendation recommendation : recommendations) {
			Long recommendationId = recommendation.getId();
			Vacancy vacancy = recommendation.getVacancy();
			String vacancyPosition = vacancy.getPosition();
			Long vacancyId = vacancy.getId();

			RecommendationGroupedResponse groupedResponse = groupedResponses
					.getOrDefault(vacancyPosition,
							new RecommendationGroupedResponse(recommendationId, vacancyPosition, vacancyId,
									new ArrayList<>()));

			List<RecommendationApplicant> applicants = recommendation.getRecommendationApplicants();

			if (applicants != null) {
				for (RecommendationApplicant applicant : applicants) {
					Boolean invited = false;
					for (InterviewSchedule schedule : applicant.getApplicant().getInterviewSchedules()) {
						if (schedule.getPosition().equals(vacancyPosition)
								&& schedule.getClient().getId().equals(clientId)) {
							invited = true;
							break;
						}
					}
					if (invited) {
						continue;
					}
					Talent talent = applicant.getApplicant().getTalent();
					TalentResponse talentResponse = talentService.getById(talent.getId());
					if (talent.getPhoto() != null) {
						talentResponse.setPhoto(Base64.getEncoder().encodeToString(talent.getPhoto()));
					}
					groupedResponse.getTalents().add(talentResponse);
				}
			}

			groupedResponses.put(vacancyPosition, groupedResponse);
		}
		return new ArrayList<>(groupedResponses.values());
	}

	@Override
	public List<String> getPositions(Long recommendationId) {
		return recommendationApplicantRepository.findByRecommendationId(recommendationId).stream()
				.map(RecommendationApplicant::getPosition)
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getSkills() {
		return skillRepository.findAllDistinctSkill();
	}

	@Override
	public Page<VacancyApplicantsResponse> getRecommendationTalents(Long recommendationId, String searchName,
			String searchPosition, String searchSkill, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return applicantRepository
				.findAllWithFilters(recommendationId, searchName, searchPosition, searchSkill, pageable)
				.map(applicant -> {
					VacancyApplicantsResponse response = new VacancyApplicantsResponse();
					response.setApplicantName(applicant.getTalent().getName());
					response.setApplicantMitra(applicant.getTalent().getInstitute().getInstituteName());
					response.setApplicantPhoto(applicant.getTalent().getPhoto());
					response.setApplicantSkill(applicant.getTalent().getSkills());
					response.setApplicantId(applicant.getId());
					return response;
				});
	}

	@Override
	public RecommendationResponse getRecommendationById(Long id) {
		return recommendationRepository.findById(id).map(recommend -> {
			RecommendationResponse response = modelMapper.map(recommend, RecommendationResponse.class);
			response.setAssignInstitute(
					recommend.getVacancy().getClient().getUser().getInstitute().getInstituteName());
			response.setPosition(recommend.getVacancy().getPosition());
			return response;
		}).orElseThrow();
	}

}
