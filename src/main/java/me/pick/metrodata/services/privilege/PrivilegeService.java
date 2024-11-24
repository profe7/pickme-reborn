package me.pick.metrodata.services.privilege;

import me.pick.metrodata.models.entity.Privilege;

import java.util.List;
import java.util.Map;

public interface PrivilegeService {

    List<Privilege> getAll();

    Map<String, List<Privilege>> getAllStructuredPrivilegeResponses();

    Privilege getPrivilegeById(Long id);
}
