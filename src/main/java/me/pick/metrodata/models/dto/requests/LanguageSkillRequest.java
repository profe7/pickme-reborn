package me.pick.metrodata.models.dto.requests;


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

    private AbilityLevel readingAbilityLevel;

    private AbilityLevel writingAbilityLevel;

    private AbilityLevel speakingAbilityLevel;

    private Long languageId;

    private String talentId;
}

