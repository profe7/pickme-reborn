package me.pick.metrodata.repositories;

import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.Mitra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long>, JpaSpecificationExecutor<Applicant> {

   Applicant findByVacancyIdAndTalent_Id(Long vacancyId, String talentId);

   Optional<Applicant> findByTalent_IdAndStatus(String talentId, ApplicantStatus status);
   

   @Query ("SELECT COUNT(a.id) FROM Applicant a WHERE a.talent.mitra = :mitra")
   Long countByMitra(@Param  ("mitra") Mitra mitra);

   @Query("SELECT a FROM Applicant a " +
      "JOIN a.talent t " +
      "JOIN a.vacancy v " +
      "WHERE v.id = :vacancyId AND t.mitra.id = :mitraId")
   List<Applicant> findApplicantsByVacancyIdAndMitraId(@Param("vacancyId") Long vacancyId, 
                                                   @Param("mitraId") Long mitraId);

   @Query("SELECT COUNT(a) FROM Applicant a " +
         "JOIN a.talent t " +
         "JOIN a.vacancy v " +
         "WHERE t.mitra.id = :mitraId AND v.expiredDate > CURRENT_DATE")
   Long countActiveApplicantByMitra(@Param("mitraId") Long mitraId);

   @Query("SELECT COUNT(a) FROM Applicant a " +
         "JOIN a.talent t " +
         "JOIN a.vacancy v " +
         "WHERE t.mitra.id = :mitraId AND v.expiredDate > CURRENT_DATE AND a.status = :status")
   Long countApplicantByStatusAndMitra(@Param("mitraId") Long mitraId, @Param("status") ApplicantStatus status);

   @Query("SELECT COUNT(a) FROM Applicant a " +
         "JOIN a.vacancy v " +
         "WHERE v.id = :vacancyId")
   Long countTotalApplicantByVacancy(@Param("vacancyId") Long vacancyId);
}