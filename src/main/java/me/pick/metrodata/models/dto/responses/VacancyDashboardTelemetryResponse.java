package me.pick.metrodata.models.dto.responses;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacancyDashboardTelemetryResponse {
    private String vacancyTitle;

    private String vacancyPosition;

    private Long vacancyApplicants;

    private LocalDate expiredDate;
}
