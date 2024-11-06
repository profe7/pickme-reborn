package me.pick.metrodata.controllers.rest;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.requests.RoleUpdateRequest;
import me.pick.metrodata.services.role.RoleService;
import me.pick.metrodata.utils.Response;
import me.pick.metrodata.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
public class RestRoleController {

    private final RoleService roleService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('READ_ROLE')")
    public ResponseEntity<Object> getAll(@RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseHandler.generateResponse(new Response(
                "Roles fetched successfully", HttpStatus.OK, "SUCCESS", roleService.getAll(name, page, size)
        ));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('UPDATE_ROLE')")
    public ResponseEntity<Object> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        roleService.updateRole(roleUpdateRequest);
        return ResponseHandler.generateResponse(new Response(
                "Role updated successfully", HttpStatus.OK, "SUCCESS", null
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('READ_ROLE')")
    public ResponseEntity<Object> getRoleById(@PathVariable Long id) {
        return ResponseHandler.generateResponse(new Response(
                "Role fetched successfully", HttpStatus.OK, "SUCCESS", roleService.getRoleById(id)
        ));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CREATE_ROLE')")
    public ResponseEntity<Object> createRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        return ResponseHandler.generateResponse(new Response(
                "Role created successfully", HttpStatus.CREATED, "SUCCESS", roleService.createRole(roleUpdateRequest)
        ));
    }

}
