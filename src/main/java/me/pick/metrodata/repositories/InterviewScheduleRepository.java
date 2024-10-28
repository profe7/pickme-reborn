package me.pick.metrodata.repositories;

import java.time.LocalDate;
import java.util.List;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.models.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

        @Query("SELECT is2 FROM InterviewSchedule is2 " +
                "JOIN is2.applicant a " +
                "JOIN a.talent t " +
                "JOIN t.mitra m " +
                "JOIN m.user u " +
                "JOIN u.institute i " +
                "WHERE i.id = :instituteId")
        public List<InterviewSchedule> findScheduleByMitra(@Param("instituteId") Long instituteId);

        List<InterviewSchedule> findInterviewScheduleByClientAndApplicantAndStatus(Client client, Applicant applicant, InterviewStatus status);

}