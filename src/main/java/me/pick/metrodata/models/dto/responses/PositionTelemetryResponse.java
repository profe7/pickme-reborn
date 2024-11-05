package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionTelemetryResponse {

    private String positionName;

    private Long totalEmployees;

}
