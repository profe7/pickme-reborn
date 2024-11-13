package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendationGroupedResponse {

    private Long id;

    private String position;

    private Long jobId;

    private List<TalentResponse> talents;

}
