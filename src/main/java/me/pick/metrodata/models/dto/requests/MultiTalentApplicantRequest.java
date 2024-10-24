package me.pick.metrodata.models.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MultiTalentApplicantRequest {
    private Long vacancyId;

    private List<String> talentIds;
}
