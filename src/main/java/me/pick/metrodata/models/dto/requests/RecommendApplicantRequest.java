package me.pick.metrodata.models.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendApplicantRequest {

    Long applicantId;

    Long vacancyId;

    Long rmId;

    String description;

}
