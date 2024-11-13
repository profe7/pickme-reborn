package me.pick.metrodata.controllers.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.responses.AccountResponse;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.account.AccountService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/account")
@AllArgsConstructor
public class AdminAccountController {

    private final AccountService accountService;
    private final UserService userService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "account");
        return "account-admin/index";
    }

    @GetMapping("/api")
    // @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Map<String, Object>> getTalents(
            @RequestParam(value = "searchUsername", required = false) String searchUsername,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<AccountResponse> accountPage = accountService.getFilteredAccount(
                searchUsername, role, status, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("accounts", accountPage.getContent());

        return ResponseEntity.ok(response);
    }
}
