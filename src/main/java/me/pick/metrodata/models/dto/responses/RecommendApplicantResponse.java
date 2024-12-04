package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendApplicantResponse {

    private Long applicantId;

    private String talentName;

    private Long clientId;

    private String clientName;

    private String talentEmail;
}
