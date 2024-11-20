package me.pick.metrodata.services.role;

import java.util.List;

import org.springframework.data.domain.Page;

import me.pick.metrodata.models.dto.requests.RoleUpdateRequest;
import me.pick.metrodata.models.dto.responses.RolePaginationResponse;
import me.pick.metrodata.models.dto.responses.RoleResponse;
import me.pick.metrodata.models.entity.Role;

public interface RoleService {
    RolePaginationResponse getAll(String name, Integer currentPage, Integer perPage);

    void updateRole(RoleUpdateRequest roleUpdateRequest);

    Role getRoleById(Long id);

    Role createRole(RoleUpdateRequest roleUpdateRequest);

    Page<RoleResponse> getFilteredRole(Integer page, Integer size);

    List<Role> getRoles();
}
