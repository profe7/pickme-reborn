package me.pick.metrodata.services.applicant;

import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.repositories.ApplicantRepository;

public class ApplicantServiceImpl implements ApplicantService{

    private final ApplicantRepository applicantRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository){
        this.applicantRepository = applicantRepository;
    }

    public Applicant createApplicant(Long vacancyId, Long talentId){
        Applicant existing = applicantRepository.findByTalentId(talentId);
    }
}
