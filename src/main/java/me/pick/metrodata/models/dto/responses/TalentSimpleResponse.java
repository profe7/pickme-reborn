package me.pick.metrodata.models.dto.responses;

import lombok.Data;

@Data
public class TalentSimpleResponse {
    private String talentId;

    private String talentName;

    private String talentPosition;

    private String talentSkill;
}
