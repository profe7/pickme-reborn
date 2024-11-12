package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MitraDashboardTelemetryResponse {

    private Long availableVacancies;

    private Long totalApplicants;

    private Long totalRejectedApplicants;

    private Long totalAcceptedApplicants;

    private Long totalAssignedApplicants;

    private List<VacancyDashboardTelemetryResponse> newestVacancies;
    
}
