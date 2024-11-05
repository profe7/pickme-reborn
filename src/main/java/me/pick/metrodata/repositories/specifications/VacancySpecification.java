package me.pick.metrodata.repositories.specifications;

import jakarta.persistence.criteria.Predicate;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.utils.DateTimeUtil;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class VacancySpecification {

    public static Specification<Vacancy> searchSpecification(String title, String position, String expiredDate, String updatedAt) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (title != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            if (position != null && !position.isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("position"), position));
            }

            if (expiredDate != null && !expiredDate.isEmpty() && !expiredDate.equals("undefined")) {
                LocalDate localDate = DateTimeUtil.stringToLocalDate(expiredDate);
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("expiredDate"), localDate));
            }

            if (updatedAt != null && !updatedAt.isEmpty() && !updatedAt.equals("undefined")) {
                LocalDate localDate = DateTimeUtil.stringToLocalDate(updatedAt);
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), localDate.atStartOfDay())
                );
            }

            return predicate;
        };
    }

    public static Specification<Vacancy> timeIntervalSpecification(String timeInterval) {
        return (root, query, criteriaBuilder) -> {
            LocalDate startDate;
            LocalDate endDate = null;

            switch (timeInterval.toLowerCase()) {
                case "hari" ->
                    startDate = LocalDate.now().minusDays(1);
                case "minggu" ->
                    startDate = LocalDate.now().minusWeeks(1);
                case "bulan" -> {
                    startDate = LocalDate.now().withDayOfMonth(1);
                    endDate = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1);
                }
                default ->
                    throw new IllegalArgumentException("Interval waktu tidak valid");
            }

            if (timeInterval.equalsIgnoreCase("bulan")) {
                if (endDate == null) {
                    throw new IllegalArgumentException("End date cannot be null");
                }
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), startDate.atStartOfDay()),
                        criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), endDate.atTime(23, 59, 59))
                );
            } else {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), startDate.atStartOfDay())
                );
            }
        };
    }

    public static Specification<Vacancy> combinedSpecification(String title, String position, String expiredDate, String updatedAt, String timeInterval) {
        Specification<Vacancy> searchSpec = searchSpecification(title, position, expiredDate, updatedAt);

        if (timeInterval != null && !timeInterval.isEmpty()) {
            Specification<Vacancy> timeIntervalSpec = timeIntervalSpecification(timeInterval);
            return Specification.where(searchSpec).and(timeIntervalSpec);
        } else {
            return searchSpec;
        }
    }
}
