package me.pick.metrodata.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.models.entity.InterviewScheduleHistory;

@Repository
public interface InterviewScheduleHistoryRepository extends JpaRepository<InterviewScheduleHistory, Long> {

    List<InterviewScheduleHistory> findInterviewScheduleHistoriesByInterviewSchedule(
            InterviewSchedule interviewSchedule);

    List<InterviewScheduleHistory> findByInterviewSchedule_Id(Long id);

    @Query("SELECT ish " +
       "FROM InterviewScheduleHistory ish " +
       "JOIN ish.interviewSchedule is2 " +
       "JOIN is2.client c " +
       "JOIN is2.applicant a " +
       "JOIN a.talent t " +
       "WHERE t.id = :talentId AND c.id = :clientId AND is2.status = 'ACCEPTED'")
    List<InterviewScheduleHistory> findInterviewScheduleHistoryByTalentIdAndClientId(
        @Param("talentId") String talentId,
        @Param("clientId") Long clientId);

}
