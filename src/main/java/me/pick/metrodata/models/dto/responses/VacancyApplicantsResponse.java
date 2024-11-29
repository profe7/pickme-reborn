package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.models.entity.Skill;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyApplicantsResponse {

    private byte[] applicantPhoto;

    private String applicantName;

    private String applicantMitra;

    private Long applicantId;

    private List<Skill> applicantSkill;

}
