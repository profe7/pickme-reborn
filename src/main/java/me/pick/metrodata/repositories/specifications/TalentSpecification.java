package me.pick.metrodata.repositories.specifications;

import jakarta.persistence.criteria.Predicate;
import me.pick.metrodata.models.entity.Talent;
import org.springframework.data.jpa.domain.Specification;


public class TalentSpecification {

    private TalentSpecification() {}

    public static Specification<Talent> buildSpecification(String name, Long minSalary, Long maxSalary, Boolean active, Long instituteId, String job, String skill, Boolean idle) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if (minSalary != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary));
            }

            if (maxSalary != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary));
            }

            if (active != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("active"), active));
            }

            if (instituteId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("institute").get("id"), instituteId));
            }

            if (job != null && !job.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("job"), "%" + job + "%"));
            }

            if (skill != null && !skill.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("skill"), "%" + skill + "%"));
            }

            if (idle != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("idle"), idle));
            }
            return predicate;
        };
    }
}