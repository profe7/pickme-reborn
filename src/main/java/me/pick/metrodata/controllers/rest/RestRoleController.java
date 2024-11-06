package me.pick.metrodata.controllers.rest;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.responses.RolePaginationResponse;
import me.pick.metrodata.services.role.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/role")
public class RestRoleController {

    private final RoleService roleService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('READ_ROLE')")
    public RolePaginationResponse getAll(@RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return roleService.getAll(name, page, size);
    }
}
