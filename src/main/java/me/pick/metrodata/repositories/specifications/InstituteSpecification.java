package me.pick.metrodata.repositories.specifications;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.entity.Institute;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

@AllArgsConstructor
public class InstituteSpecification {

  public static Specification<Institute> searchSpecification(String instituteName, Long instituteTypeId, Long userId) {
    return (root, query, criteriaBuilder) -> {
      Predicate predicate = criteriaBuilder.conjunction();

      if (instituteName != null) {
        predicate = criteriaBuilder.and(
            predicate,
            criteriaBuilder.like(root.get("instituteName"), "%" + instituteName + "%"));
      }

      if (instituteTypeId != null) {
        predicate = criteriaBuilder.and(
            predicate,
            criteriaBuilder.equal(root.get("instituteType").get("id"), instituteTypeId));
      }

      if (userId != null) {
        predicate = criteriaBuilder.and(
            predicate,
            criteriaBuilder.equal(root.get("user").get("id"), userId));
      }

      return predicate;
    };
  }
}