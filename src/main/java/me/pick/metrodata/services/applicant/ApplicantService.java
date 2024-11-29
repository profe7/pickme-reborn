package me.pick.metrodata.services.applicant;

import me.pick.metrodata.models.dto.requests.ApplicantCreationRequest;
import me.pick.metrodata.models.dto.requests.MultiTalentApplicantRequest;
import me.pick.metrodata.models.dto.requests.RecommendApplicantRequest;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.Recommendation;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ApplicantService {

    Applicant createApplicant(ApplicantCreationRequest request);

    List<Applicant> multiCreateApplicant(MultiTalentApplicantRequest request);

    Recommendation recommendApplicant(RecommendApplicantRequest request);

    Page<Applicant> getRecommendedApplicant(Long vacancyId, Integer page, Integer size);

    Page<Applicant> getApplicantsByVacancyAndInstitute(Long vacancyId, String searchInstitute, Integer page,
            Integer size);

    Applicant getApplicantById(Long id);

}
