package me.pick.metrodata.repositories.specifications;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.utils.DateTimeUtil;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.time.LocalDate;

public class InterviewScheduleSpecification {

    private InterviewScheduleSpecification() {}

    public static Specification<InterviewSchedule> searchSpecification(String search, Long clientId, InterviewType type, String startDate, String endDate, InterviewStatus status) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (search != null && !search.isEmpty()) {
                Join<Object, Object> applicantJoin = root.join("applicant");
                Join<Object, Object> talentJoin = applicantJoin.join("talent");
                Join<Object, Object> clientJoin = root.join("client");
                Join<Object, Object> userJoin = clientJoin.join("user");
                Join<Object, Object> instituteJoin = userJoin.join("institute");

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("position"), "%" + search + "%"),
                                criteriaBuilder.like(talentJoin.get("name"), "%" + search + "%"),
                                criteriaBuilder.like(instituteJoin.get("instituteName"), "%" + search + "%")));
            }
            if (clientId != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("client").get("id"), clientId));
            }

            if (type != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("interviewType"), type));
            }

            if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
                LocalDate localStartDate = DateTimeUtil.stringToLocalDate(startDate);
                LocalDate localEndDate = DateTimeUtil.stringToLocalDate(endDate);
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.between(root.get("date"), localStartDate, localEndDate));
            }

            if (status != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("status"), status));
            }

            return predicate;
        };
    }
}