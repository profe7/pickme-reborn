package me.pick.metrodata.models.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class TalentAvailableForVacancyResponse {
    private List<TalentSimpleResponse> talents;
}
