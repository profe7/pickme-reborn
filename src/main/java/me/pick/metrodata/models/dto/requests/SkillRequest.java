package me.pick.metrodata.models.dto.requests;


import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.SkillCategory;
import me.pick.metrodata.enums.SkillLevel;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkillRequest {
    @NotNull
    private String name;

    @NotNull
    private SkillCategory category;

    @NotNull
    private SkillLevel level;

    @NotNull
    private String talentId;
}

