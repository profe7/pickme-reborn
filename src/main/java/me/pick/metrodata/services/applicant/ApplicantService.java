package me.pick.metrodata.services.applicant;

import me.pick.metrodata.models.dto.requests.ApplicantCreationRequest;
import me.pick.metrodata.models.dto.requests.MultiTalentApplicantRequest;
import me.pick.metrodata.models.entity.Applicant;

import java.util.List;

public interface ApplicantService {
    Applicant createApplicant(ApplicantCreationRequest request);

    List<Applicant> multiCreateApplicant(MultiTalentApplicantRequest request);
}
