package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.models.entity.InterviewScheduleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewScheduleHistoryRepository extends JpaRepository<InterviewScheduleHistory, Long> {
    List<InterviewScheduleHistory> findInterviewScheduleHistoriesByInterviewSchedule(InterviewSchedule interviewSchedule);
}
