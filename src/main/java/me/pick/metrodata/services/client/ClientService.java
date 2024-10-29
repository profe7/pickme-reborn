package me.pick.metrodata.services.client;

import me.pick.metrodata.models.entity.Talent;

import java.util.List;

public interface ClientService {
    List<Talent> getClientEmployees(Long clientId);

    void deleteClientEmployee(Long clientId, String talentId);
}
