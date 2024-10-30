package me.pick.metrodata.services.applicant;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.exceptions.applicant.ApplicantAlreadyExistsException;
import me.pick.metrodata.exceptions.applicant.ApplicantDoesNotExistException;
import me.pick.metrodata.exceptions.talent.TalentDoesNotExistException;
import me.pick.metrodata.exceptions.user.UserDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyNotExistException;
import me.pick.metrodata.models.dto.requests.ApplicantCreationRequest;
import me.pick.metrodata.models.dto.requests.MultiTalentApplicantRequest;
import me.pick.metrodata.models.dto.requests.RecommendApplicantRequest;
import me.pick.metrodata.models.entity.*;
import me.pick.metrodata.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService{

    private final ApplicantRepository applicantRepository;
    private final VacancyRepository vacancyRepository;
    private final TalentRepository talentRepository;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;
    private final RecommendationApplicantRepository recommendationApplicantRepository;

    @Override
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

    @Override
    public List<Applicant> multiCreateApplicant(MultiTalentApplicantRequest request) {
        List<Applicant> newApplicants = new ArrayList<>();
        for (String talentId : request.getTalentIds()) {
            newApplicants.add(createApplicant(new ApplicantCreationRequest(request.getVacancyId(), talentId)));
        }
        return newApplicants;
    }

    @Override
    public Recommendation recommendApplicant(RecommendApplicantRequest request){
        Applicant applicant = applicantRepository.findById(request.getApplicantId()).orElseThrow(() -> new ApplicantDoesNotExistException(request.getApplicantId()));
        Vacancy vacancy = vacancyRepository.findVacancyById(request.getVacancyId()).orElseThrow(() -> new VacancyNotExistException(request.getVacancyId()));

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(userRepository.findById(request.getRmId()).orElseThrow(() -> new UserDoesNotExistException(request.getRmId().toString())));
        recommendation.setVacancy(vacancy);
        recommendation.setDescription(request.getDescription());
        recommendationRepository.save(recommendation);

        RecommendationApplicant recommendationApplicant = new RecommendationApplicant();
        recommendationApplicant.setApplicant(applicant);
        recommendationApplicant.setRecommendation(recommendation);
        recommendationApplicant.setPosition(vacancy.getPosition());
        recommendationApplicantRepository.save(recommendationApplicant);

        return recommendation;
    }

    @Override
    public Page<Applicant> getRecommendedApplicant(Long vacancyId, Integer page, Integer size) {
        List<Recommendation> recommendations = recommendationRepository.findRecommendationByVacancyId(vacancyId);
        List<RecommendationApplicant> recommendationApplicants = new ArrayList<>();
        List<Applicant> applicants = new ArrayList<>();
        Pageable pageable  = PageRequest.of(page, size);

        for (Recommendation recommendation : recommendations) {
            recommendationApplicants.addAll(recommendation.getRecommendationApplicants());
        }

        for (RecommendationApplicant recommendationApplicant : recommendationApplicants) {
            applicants.add(recommendationApplicant.getApplicant());
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), applicants.size());
        return new PageImpl<>(applicants.subList(start, end), pageable, applicants.size());
    }
    
}
