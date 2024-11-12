package me.pick.metrodata.services.role;

import me.pick.metrodata.models.dto.requests.RoleUpdateRequest;
import me.pick.metrodata.models.dto.responses.RolePaginationResponse;
import me.pick.metrodata.models.entity.Role;

public interface RoleService {
    RolePaginationResponse getAll(String name, Integer currentPage, Integer perPage);

    void updateRole(RoleUpdateRequest roleUpdateRequest);

    Role getRoleById(Long id);

    Role createRole(RoleUpdateRequest roleUpdateRequest);
}
