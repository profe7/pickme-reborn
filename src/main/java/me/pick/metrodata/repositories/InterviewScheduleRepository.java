package me.pick.metrodata.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.entity.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewScheduleRepository
                extends JpaRepository<InterviewSchedule, Long>, JpaSpecificationExecutor<InterviewSchedule> {

        InterviewSchedule findInterviewScheduleByClientAndApplicantAndDate(Client client, Applicant applicant,
                        LocalDate date);

        List<InterviewSchedule> findInterviewScheduleByClientAndDate(Client client, LocalDate date);

        @Query("SELECT is2 FROM InterviewSchedule is2 "
                        + "JOIN is2.applicant a "
                        + "JOIN a.talent t "
                        + "JOIN t.mitra m "
                        + "JOIN m.user u "
                        + "JOIN u.institute i "
                        + "WHERE i.id = :instituteId")
        List<InterviewSchedule> findScheduleByMitra(@Param("instituteId") Long instituteId);

        Optional<InterviewSchedule> findInterviewScheduleById(Long id);

        List<InterviewSchedule> findInterviewScheduleByClientIdAndStatus(Long clientId, InterviewStatus status);

        List<InterviewSchedule> findInterviewScheduleByClient(Client client);

        @Query("SELECT is2 FROM InterviewSchedule is2 "
                        + "JOIN is2.applicant a "
                        + "JOIN a.talent t "
                        + "JOIN t.mitra m "
                        + "WHERE m.id = :mitraId")
        List<InterviewSchedule> findInterviewScheduleByMitraId(@Param("mitraId") Long mitraId);

        Optional<InterviewSchedule> findByApplicantAndClientIdAndStatus(Applicant applicant, Long clientId,
                        InterviewStatus status);

        @Query("SELECT i FROM InterviewSchedule i WHERE "
                        + "(:recruiter IS NULL OR i.client.user.firstName LIKE %:recruiter%) AND "
                        + "(:talent IS NULL OR i.applicant.talent.name LIKE %:talent%) AND "
                        + "(:type IS NULL OR i.interviewType = :type) AND "
                        + "(:date IS NULL OR i.date = :date) AND "
                        + "(:status IS NULL OR i.status = :status)")
        Page<InterviewSchedule> findAllWithFilters(
                        @Param("recruiter") String recruiter,
                        @Param("talent") String talent,
                        @Param("type") InterviewType type,
                        @Param("date") LocalDate date,
                        @Param("status") InterviewStatus status,
                        Pageable pageable);

        @Query("SELECT is2 FROM InterviewSchedule is2 "
                        + "JOIN is2.applicant a "
                        + "JOIN a.talent t "
                        + "WHERE t.id = :talentId")
        List<InterviewSchedule> findInterviewScheduleByTalentId(@Param("talentId") String talentId);
}
