package me.pick.metrodata.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.models.entity.InterviewScheduleHistory;

@Repository
public interface InterviewScheduleHistoryRepository extends JpaRepository<InterviewScheduleHistory, Long> {

    List<InterviewScheduleHistory> findInterviewScheduleHistoriesByInterviewSchedule(
            InterviewSchedule interviewSchedule);

    List<InterviewScheduleHistory> findByInterviewSchedule_Id(Long id);
}
