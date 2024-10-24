package me.pick.metrodata.services.applicant;

import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.exceptions.Applicant.ApplicantAlreadyExistsException;
import me.pick.metrodata.exceptions.talent.TalentDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyNotExistException;
import me.pick.metrodata.models.dto.requests.ApplicantCreationRequest;
import me.pick.metrodata.models.dto.requests.MultiTalentApplicantRequest;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.repositories.ApplicantRepository;
import me.pick.metrodata.repositories.TalentRepository;
import me.pick.metrodata.repositories.VacancyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Applicant existing = applicantRepository.findByVacancyIdAndTalent_Id(request.getVacancyId(), request.getTalentId());
        if (existing != null){
            throw new ApplicantAlreadyExistsException(existing.getTalent().getName());
        }
        Applicant applicant = new Applicant();
        applicant.setStatus(ApplicantStatus.ASSIGNED);
        applicant.setVacancy(vacancyRepository.findVacancyById(request.getVacancyId()).orElseThrow(() -> new VacancyNotExistException(request.getVacancyId())));
        applicant.setTalent(talentRepository.findById(request.getTalentId()).orElseThrow(() -> new TalentDoesNotExistException(request.getTalentId())));
        return applicantRepository.save(applicant);
    }

    public List<Applicant> multiCreateApplicant(MultiTalentApplicantRequest request) {
        List<Applicant> newApplicants = new ArrayList<>();
        for (String talentId : request.getTalentIds()) {
            newApplicants.add(createApplicant(new ApplicantCreationRequest(request.getVacancyId(), talentId)));
        }
        return newApplicants;
    }
}
