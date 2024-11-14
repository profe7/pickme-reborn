package me.pick.metrodata.repositories.specifications;

import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.Institute;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import java.util.List;

public class AccountSpecification {
  private AccountSpecification() {}

  public static Specification<Account> searchSpecification(
      String search, Long institute, Long baseBudget, Long limitBudget,
      List<Institute> institutes) {
    return (root, query, criteriaBuilder) -> {
      Predicate predicate = criteriaBuilder.conjunction();
      CriteriaBuilder.In<Institute> inBuilder = criteriaBuilder.in(root.get("user").get("institute"));

      if (search != null) {
        Predicate usernamePredicate = criteriaBuilder.like(root.get("username"), "%" + search + "%");
        Predicate emailPredicate = criteriaBuilder.like(root.get("user").get("email"), "%" + search + "%");
        Predicate firstNamePredicate = criteriaBuilder.like(root.get("user").get("firstName"), "%" + search + "%");

        predicate = criteriaBuilder.or(usernamePredicate, emailPredicate, firstNamePredicate);
      }

      if (institute != null) {
        predicate = criteriaBuilder.and(
            predicate,
            criteriaBuilder.equal(root.get("user").get("institute").get("id"), institute));
      }

      if (baseBudget != null) {
        predicate = criteriaBuilder.and(
            predicate,
            criteriaBuilder.greaterThanOrEqualTo(root.get("user").get("baseBudget"), baseBudget));
      }

      if (limitBudget != null) {
        predicate = criteriaBuilder.and(
            predicate,
            criteriaBuilder.lessThanOrEqualTo(root.get("user").get("limitBudget"), limitBudget));
      }

      if (institutes != null && !institutes.isEmpty()) {
        institutes.forEach(inBuilder::value);
        predicate = criteriaBuilder.and(predicate, inBuilder);
      }

      return predicate;
    };
  }
}