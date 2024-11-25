package me.pick.metrodata.models.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyRequest {

    private String title;

    private String position;

    private String description;

    private String status;

    private Integer requiredPositions;

    private String expiredDate;
}
