package me.pick.metrodata.repositories.specifications;

import me.pick.metrodata.models.entity.Recommendation;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class RecommendationSpecification {

	private RecommendationSpecification() {}

	public static Specification<Recommendation> filterByTalentNameAndPosition(String talentName, String position) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if (talentName != null && !talentName.isEmpty()) {
				Join<Object, Object> recommendationApplicantJoin = root.join("recommendationApplicants");
				Join<Object, Object> applicantJoin = recommendationApplicantJoin.join("applicant");
				Join<Object, Object> talentJoin = applicantJoin.join("talent");

				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.like(talentJoin.get("name"), "%" + talentName + "%")
				);
			}

			if (position != null && !position.isEmpty()) {
				Join<Object, Object> vacancyJoin = root.join("vacancy");

				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.like(vacancyJoin.get("position"), "%" + position + "%")
				);
			}

			return predicate;
		};
	}
}