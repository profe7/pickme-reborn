package me.pick.metrodata.models.dto.requests;

import lombok.Data;

@Data
public class ApplicantCreationRequest {
    private Long vacancyId;

    private String talentId;

    public ApplicantCreationRequest(Long vacancyId, String talentId) {
        this.vacancyId = vacancyId;
        this.talentId = talentId;
    }
}
