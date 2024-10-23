package me.pick.metrodata.services.applicant;

import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.exceptions.talent.TalentDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyNotExistException;
import me.pick.metrodata.models.dto.requests.ApplicantCreationRequest;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.repositories.ApplicantRepository;
import me.pick.metrodata.repositories.TalentRepository;
import me.pick.metrodata.repositories.VacancyRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicantServiceImpl implements ApplicantService{

    private final ApplicantRepository applicantRepository;
    private final VacancyRepository vacancyRepository;
    private final TalentRepository talentRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository, VacancyRepository vacancyRepository, TalentRepository talentRepository){
        this.applicantRepository = applicantRepository;
        this.vacancyRepository = vacancyRepository;
        this.talentRepository = talentRepository;
    }

    public Applicant createApplicant(ApplicantCreationRequest request){
        Applicant applicant = new Applicant();
        applicant.setStatus(ApplicantStatus.ASSIGNED);
        applicant.setVacancy(vacancyRepository.findVacancyById(request.getVacancyId()).orElseThrow(() -> new VacancyNotExistException(request.getVacancyId())));
        applicant.setTalent(talentRepository.findById(request.getTalentId()).orElseThrow(() -> new TalentDoesNotExistException(request.getTalentId())));
        return applicantRepository.save(applicant);
    }
}
