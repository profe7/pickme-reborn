package me.pick.metrodata.models.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class ClientDashboardTelemetryResponse {
    private Long clientId;

    private Long totalEmployees;

    private List<PositionTelemetryResponse> totalEmployeesByPosition;
}
