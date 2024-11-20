package me.pick.metrodata.models.dto.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendationResponse {

    private Long id;

    private String position;

    private String assignInstitute;

    private Integer totalTalents;

    private LocalDate assignDate;

    private String description;

}
