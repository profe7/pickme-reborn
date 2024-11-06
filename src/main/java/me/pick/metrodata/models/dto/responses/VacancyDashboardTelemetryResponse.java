package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyDashboardTelemetryResponse {

    private Long vacancyId;

    private String vacancyTitle;

    private String vacancyPosition;

    private Long vacancyApplicants;

    private LocalDate expiredDate;

}
