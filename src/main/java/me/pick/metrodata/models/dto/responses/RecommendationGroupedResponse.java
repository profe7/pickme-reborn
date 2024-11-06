package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendationGroupedResponse {

    Long id;

    String position;

    Long jobId;

    List<TalentResponse> talents;

}
