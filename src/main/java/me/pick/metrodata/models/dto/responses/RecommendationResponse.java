package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.models.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendationResponse {

    private Long id;

    private LocalDateTime assignDate;

    private String position;

    private Long jobId;

    private Institute recommendationInstitute;

    private User recommendationMaster;

    private List<TalentResponse> talents;

    private String description;

    private Integer totalTalents;

}
