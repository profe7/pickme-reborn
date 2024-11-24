package me.pick.metrodata.services.client;

import me.pick.metrodata.models.dto.responses.ClientDashboardTelemetryResponse;
import me.pick.metrodata.models.entity.Talent;

import java.util.List;

import me.pick.metrodata.models.dto.responses.ClientResponse;

public interface ClientService {
    List<Talent> getClientEmployees(Long clientId);

    void deleteClientEmployee(Long clientId, String talentId);

    ClientDashboardTelemetryResponse getClientDashboardTelemetry(Long clientId);

    List<ClientResponse> getClients();
}
