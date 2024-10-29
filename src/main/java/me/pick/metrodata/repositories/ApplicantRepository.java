package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.Mitra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long>, JpaSpecificationExecutor<Applicant> {

    Applicant findByVacancyIdAndTalent_Id(Long vacancyId, String talentId);

    @Query ("SELECT COUNT(a.id) FROM Applicant a WHERE a.talent.mitra = :mitra")
    Long countByMitra(@Param  ("mitra") Mitra mitra);

    @Query("SELECT a FROM Applicant a WHERE a.vacancy.id = :vacancyId")
    List<Applicant> findApplicantByVacancyId(@Param("vacancyId") Long vacancyId);
}