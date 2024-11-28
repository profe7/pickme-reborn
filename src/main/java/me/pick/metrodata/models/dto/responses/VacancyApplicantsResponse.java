package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyApplicantsResponse {

    private byte[] applicantPhoto;

    private String applicantName;

    private String applicantMitra;

    private Long applicantId;

}
