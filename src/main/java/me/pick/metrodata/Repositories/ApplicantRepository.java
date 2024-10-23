package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long>, JpaSpecificationExecutor<Applicant> {

    Applicant findByVacancyIdAndTalent_Id(Long vacancyId, String talentId);

    List<Applicant> findByVacancyId(Long vacancyId);

    List<Applicant> findByTalentId(String talentId);

}