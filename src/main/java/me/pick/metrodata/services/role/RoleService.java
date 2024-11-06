package me.pick.metrodata.services.role;

import me.pick.metrodata.models.dto.responses.RolePaginationResponse;

public interface RoleService {
    RolePaginationResponse getAll(String name, Integer currentPage, Integer perPage);
}
