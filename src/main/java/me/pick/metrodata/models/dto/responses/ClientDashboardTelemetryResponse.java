package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDashboardTelemetryResponse {

    private Long clientId;

    private Long totalEmployees;

    private List<PositionTelemetryResponse> totalEmployeesByPosition;

}
