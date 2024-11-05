package me.pick.metrodata.models.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalentFromVacancyRequest {

    private String talentName;

    private String talentEmail;

    private String talentNik;

    private Long talentMitraId;

    private Long vacancyId;

}
