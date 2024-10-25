package me.pick.metrodata.repositories.specifications;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.persistence.criteria.*;

import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.utils.DateTimeUtil;
import org.springframework.data.jpa.domain.Specification;

public class InterviewScheduleSpecification {
	public static Specification<InterviewSchedule> searchSpecification(String search, Long recruiter, Boolean online, String startDate, String endDate,
																	   String status, Set<Long> userIds) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if (search != null) {
				Join<InterviewSchedule, Applicant> applicantJoin = root.join("applicant");
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.or(
								criteriaBuilder.like(root.get("position"), "%" + search + "%"),
								criteriaBuilder.like(applicantJoin.get("talent").get("name"), "%" + search + "%")));
			}
			if (recruiter != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(root.get("client").get("id"), recruiter)
				);
			}
			if (online != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(root.get("offline"), online));
			}

			if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
				LocalDate localStartDate = DateTimeUtil.stringToLocalDate(startDate);
				LocalDate localEndDate = DateTimeUtil.stringToLocalDate(endDate);
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.between(root.get("date"), localStartDate, localEndDate));
			}
			if (userIds != null && !userIds.isEmpty()) {
				predicate = criteriaBuilder.and(
						predicate,
						root.get("recruiter").get("id").in(userIds));
			}

			if (status != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(root.get("status"), status));
			}

			return predicate;
		};
	}

	public static Specification<InterviewSchedule> searchForClientSpecification(
			List<Long> excludedStatusIds, Long recruiterId, String onBoardDate) { // Removed talentName
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			predicate = criteriaBuilder.and(
					predicate,
					criteriaBuilder.equal(root.get("recruiter"), recruiterId),
					criteriaBuilder.not(root.get("status").in(excludedStatusIds)));

			if (onBoardDate != null && !onBoardDate.isEmpty()) {
				LocalDate localDate = DateTimeUtil.stringToLocalDate(onBoardDate);
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(root.get("onBoardDate"), localDate));
			}

			return predicate;
		};
	}

	public static Specification<InterviewSchedule> searchForProcessedInterviewsSpecification(
			Long recruiterId, String date) { // Removed talentName
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if (recruiterId != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(root.get("recruiter"), recruiterId));
			}

			if (date != null && !date.isEmpty()) {
				LocalDate localDate = LocalDate.parse(date);
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(root.get("date"), localDate));
			}

			return predicate;
		};
	}
}
