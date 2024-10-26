package me.pick.metrodata.repositories.specifications;

import jakarta.persistence.criteria.Predicate;
import me.pick.metrodata.models.entity.Talent;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TalentSpecification {
	public static Specification<Talent> searchSpecification(String name, Long minSalary, Long maxSalary, Boolean active) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if (name != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.like(root.get("name"), "%" + name + "%"));
			}

			if (minSalary != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary));
			}

			if (maxSalary != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary));
			}

			if (active != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(root.get("active"), active));
			}

			return predicate;
		};
	}

	public static Specification<Talent> byInstituteId(Long instituteId) {
		return (root, query, criteriaBuilder) -> {
			if (instituteId == null) {
				return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
			} else {
				return criteriaBuilder.equal(root.get("institute").get("id"), instituteId);
			}
		};
	}

	public static Specification<Talent> hasInstituteId(Long instituteId) {
		return (root, query, cb) -> instituteId == null ? cb.conjunction()
				: cb.equal(root.get("institute").get("id"), instituteId);
	}

	public static Specification<Talent> nameContains(String name) {
		return (root, query, builder) -> {
			if (name == null || name.isEmpty()) {
				return builder.conjunction();
			}
			return builder.like(root.get("name"), "%" + name + "%");
		};
	}

	public static Specification<Talent> jobContains(String job) {
		return (root, query, builder) -> {
			if (job == null || job.isEmpty()) {
				return builder.conjunction();
			}
			return builder.like(root.get("job"), "%" + job + "%");
		};
	}

	public static Specification<Talent> skillContains(String skill) {
		return (root, query, builder) -> {
			if (skill == null || skill.isEmpty()) {
				return builder.conjunction();
			}
			// Asumsi skill adalah string dasar dalam Talent, kita tidak melakukan join
			return builder.like(root.get("skill"), "%" + skill + "%");
		};
	}

	public static Specification<Talent> isIdle(Boolean idle) {
		return (root, query, cb) -> idle == null ? cb.conjunction() : cb.equal(root.get("idle"), idle);
	}

	public static Specification<Talent> hasInstitute() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("institute"));
	}

	public static Specification<Talent> hasInstituteIds(List<Long> instituteIds) {
		return (root, query, criteriaBuilder) -> root.get("institute").get("id").in(instituteIds);
	}
}
