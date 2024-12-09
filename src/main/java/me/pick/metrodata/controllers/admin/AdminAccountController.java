package me.pick.metrodata.controllers.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.dto.responses.AccountResponse;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.account.AccountService;
import me.pick.metrodata.services.institute.InstituteService;
import me.pick.metrodata.services.role.RoleService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/account")
@AllArgsConstructor
public class AdminAccountController {

    private final AccountService accountService;
    private final UserService userService;
    private final RoleService roleService;
    private final InstituteService instituteService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "account");
        model.addAttribute("roles", roleService.getRoles());
        return "account-admin/index";
    }

    @GetMapping("/api")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public ResponseEntity<Map<String, Object>> getTalents(
            @RequestParam(value = "searchUsername", required = false) String searchUsername,
            @RequestParam(value = "role", required = false) Long role,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<AccountResponse> accountPage = accountService.getFilteredAccount(
                searchUsername, role, status, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("accounts", accountPage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public String detail(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "account");
        model.addAttribute("account", accountService.getAccountById(id));

        return "account-admin/detail";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_CREATE_ACCOUNT','EXTERNAL_CREATE_ACCOUNT')")
    public String createForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("institutes", instituteService.getAll());
        model.addAttribute("isActive", "account");
        model.addAttribute("roles", roleService.getRoles());

        return "account-admin/create";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_UPDATE_ACCOUNT','EXTERNAL_UPDATE_ACCOUNT')")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("institutes", instituteService.getAll());
        model.addAttribute("account", accountService.getAccountById(id));
        model.addAttribute("isActive", "account");
        model.addAttribute("roles", roleService.getRoles());

        return "account-admin/update";
    }

    @GetMapping("/update-profile")
    @PreAuthorize("hasAnyAuthority('UPDATE_PROFILE')")
    public String updateProfileForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("isActive", "home");
        model.addAttribute("logged", loggedUser);
        model.addAttribute("account", accountService.getAccountById(loggedUser.getId()));

        return "account-admin/update-profile";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_CREATE_ACCOUNT','EXTERNAL_CREATE_ACCOUNT')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AccountRequest accountRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            accountService.create(accountRequest);
            response.put("message", "Akun baru berhasil ditambahkan");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat menambahkan akun baru");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_UPDATE_ACCOUNT','EXTERNAL_UPDATE_ACCOUNT')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody AccountRequest accountRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            accountService.update(id, accountRequest);
            response.put("message", "Akun berhasil diperbarui");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat memperbarui akun");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update-profile")
    @PreAuthorize("hasAnyAuthority('UPDATE_PROFILE')")
    public ResponseEntity<Void> updateProfile(@RequestBody AccountRequest accountRequest, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        try {
            accountService.updateProfile(loggedUser.getId(), accountRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/update-access/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT_UPDATE_ACCOUNT','EXTERNAL_UPDATE_ACCOUNT')")
    public ResponseEntity<Void> updateAccess(@PathVariable Long id) {

        try {
            accountService.updateAccess(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
