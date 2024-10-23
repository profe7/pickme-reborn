package me.pick.metrodata.Repositories;

import me.pick.backend.enums.InterviewStatus;
import me.pick.metrodata.models.entity.models.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterviewScheduleRepository
                extends JpaRepository<InterviewSchedule, Long>, JpaSpecificationExecutor<InterviewSchedule> {

        List<InterviewSchedule> findAllByApplicant_Vacancy_IdAndStatus(Long vacancyId, InterviewStatus status);

        List<InterviewSchedule> findByApplicant_TalentAndDate(Talent talent, LocalDate date);

        List<InterviewSchedule> findByClient_User(User recruiter, Pageable pageable);

        List<InterviewSchedule> findByClient_User(User recruiter);

        List<InterviewSchedule> findByApplicantOrderByUpdatedAtDesc(Applicant applicant);

        InterviewSchedule findInterviewScheduleByClientAndApplicantAndDate(Client client, Applicant applicant,
                        LocalDate date);

        List<InterviewSchedule> findInterviewScheduleByClientAndDate(Client client, LocalDate date);

        @Query("SELECT DISTINCT sched FROM InterviewSchedule sched " +
                        "LEFT JOIN sched.client c " +
                        "LEFT JOIN sched.applicant a " +
                        "LEFT JOIN a.talent t " +
                        "LEFT JOIN t.mitra m " +
                        "WHERE c.user.institute.id = :instituteId OR m.user.institute.id = :instituteId")
        public List<InterviewSchedule> findScheduleByMitra(@Param("instituteId") Long instituteId);

        @Query(value = "SELECT DISTINCT t.id FROM talent t " +
                        "JOIN interview_schedule iss ON t.id = iss.applicant_id AND iss.on_board_date IS NULL " +
                        "JOIN interview_schedule_history ish ON iss.id = ish.interview_schedule_id AND ish.status == 'ACCEPTED' "
                        +
                        "WHERE t.id IN :talentIds", nativeQuery = true)
        List<String> findTalentsByIdsWhereInterviewScheduleIsAccepted(@Param("talentIds") List<String> talentIds);

        List<InterviewSchedule> findInterviewScheduleByClientAndApplicantAndStatus(Client client, Applicant applicant, InterviewStatus status);

}