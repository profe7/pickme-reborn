package me.pick.metrodata.models.dto.responses;

import lombok.Data;

@Data
public class VacancyApplicantsResponse {
    private String applicantName;

    private String applicantMitra;

    private Long applicantId;

    public VacancyApplicantsResponse(String name, String instituteName, Long id) {
        this.applicantName = name;
        this.applicantMitra = instituteName;
        this.applicantId = id;
    }
}
