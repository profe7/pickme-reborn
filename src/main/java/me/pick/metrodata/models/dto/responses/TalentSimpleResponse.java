package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalentSimpleResponse {

    private String talentId;

    private String talentName;

    private String talentPosition;

    private String talentSkill;

}
