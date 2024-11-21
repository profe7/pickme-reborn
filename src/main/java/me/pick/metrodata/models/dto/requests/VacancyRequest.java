package me.pick.metrodata.models.dto.requests;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyRequest {

    @NotNull
    private String title;

    @NotNull
    private String position;

    @NotNull
    private String description;

    @NotNull
    private String status;

    @NotNull
    private Integer requiredPositions;

    @NotNull
    private String expiredDate;
}
