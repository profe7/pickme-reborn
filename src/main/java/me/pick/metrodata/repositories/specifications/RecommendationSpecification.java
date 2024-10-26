package me.pick.metrodata.repositories.specifications;

import me.pick.metrodata.models.entity.Recommendation;
import org.springframework.data.jpa.domain.Specification;

public class RecommendationSpecification {
	public static Specification<Recommendation> searchSpecification() {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.conjunction();
		};
	}
}
