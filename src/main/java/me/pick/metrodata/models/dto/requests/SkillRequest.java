package me.pick.metrodata.models.dto.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.SkillCategory;
import me.pick.metrodata.enums.SkillLevel;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkillRequest {
    private String name;
    private SkillCategory category;
    private SkillLevel level;
    private String talentId;
}

