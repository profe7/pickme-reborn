package me.pick.metrodata.models.dto.requests;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.AbilityLevel;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class LanguageSkillRequest {

    @NotNull
    private AbilityLevel readingAbilityLevel;

    @NotNull
    private AbilityLevel writingAbilityLevel;

    @NotNull
    private AbilityLevel speakingAbilityLevel;

    @NotNull
    private Long languageId;

    @NotNull
    private String talentId;
}

