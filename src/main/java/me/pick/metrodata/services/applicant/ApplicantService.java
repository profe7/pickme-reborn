package me.pick.metrodata.services.applicant;

import me.pick.metrodata.models.entity.Applicant;

public interface ApplicantService {
    Applicant createApplicant(Long vacancyId);
}
