package me.pick.metrodata.controllers.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.requests.RoleRequest;
import me.pick.metrodata.models.dto.responses.RoleResponse;
import me.pick.metrodata.models.entity.Role;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.privilege.PrivilegeService;
import me.pick.metrodata.services.role.RoleService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/role")
@AllArgsConstructor
public class AdminRoleController {

    private final UserService userService;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "role");
        return "role-admin/index";
    }

    @GetMapping("/api")
    // @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Map<String, Object>> getRoles(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<RoleResponse> rolePage = roleService.getFilteredRole(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("roles", rolePage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_ROLE')")
    public String createForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "role");
        model.addAttribute("groups", privilegeService.getAllStructuredPrivilegeResponses());

        return "role-admin/create";
    }

    @GetMapping("/update/{id}")
    // @PreAuthorize("hasAnyAuthority('UPDATE_ROLE')")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "role");
        model.addAttribute("role", roleService.getRoleById(id));
        model.addAttribute("privileges", privilegeService.getAll());
        model.addAttribute("groups", privilegeService.getAllStructuredPrivilegeResponses());

        return "role-admin/update";
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyAuthority('READ_ROLE')")
    public Role getById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @PostMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_PARAMETER')")
    public ResponseEntity<Void> create(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {

        try {
            roleService.create(roleRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    // @PreAuthorize("hasAnyAuthority('UPDATE_PARAMETER')")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody RoleRequest roleRequest) {

        try {
            roleService.update(id, roleRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    // @PreAuthorize("hasAnyAuthority('DELETE_PARAMETER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        try {
            roleService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
