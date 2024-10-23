package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.InterviewScheduleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewScheduleHistoryRepository extends JpaRepository<InterviewScheduleHistory, Long> {
	@Query (value = "SELECT t.name AS talentName, ish.feedback AS feedback, ish.created_at AS createdAt, c.name AS clientName " +
			"FROM talent t " +
			"JOIN applicant a ON t.id = a.talent_id " +
			"JOIN interview_schedule isch ON a.id = isch.applicant_id " +
			"JOIN interview_schedule_history ish ON isch.id = ish.interview_schedule_id " +
			"JOIN client c ON isch.client_id = c.id " +
			"WHERE ish.status = 'ON_PROCESS' " +
			"AND t.institute_id = :instituteId", nativeQuery = true)
	List<Object[]> findFeedbackAndCreatedBy(@Param  ("instituteId") Long instituteId);

	@Query(value = "SELECT t.name AS talentName, c.name AS clientName, isch.on_board_date AS onBoardDate " +
			"FROM interview_schedule isch " +
			"JOIN applicant a ON isch.applicant_id = a.id " +
			"JOIN talent t ON a.talent_id = t.id " +
			"JOIN client c ON c.id = isch.client_id " +
			"WHERE isch.status = 'ON_PROCESS' " +
			"AND t.institute_id = :instituteId", nativeQuery = true)
	List<Object[]> findOnBoardByInstitute(@Param("instituteId") Long instituteId);

}
