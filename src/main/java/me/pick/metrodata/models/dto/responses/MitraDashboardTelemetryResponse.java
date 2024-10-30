package me.pick.metrodata.models.dto.responses;

import lombok.Data;
import me.pick.metrodata.models.entity.Vacancy;

import java.util.List;

@Data
public class MitraDashboardTelemetryResponse {
    private Long availableVacancies;

    private Long totalApplicants;

    private Long totalRejectedApplicants;

    private Long totalAcceptedApplicants;

    private List<VacancyDashboardTelemetryResponse> newestVacancies;
}
