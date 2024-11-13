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

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.responses.ReferenceResponse;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.reference.ReferenceService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/parameter")
@AllArgsConstructor
public class ParameterController {

    private final ReferenceService referenceService;
    private final UserService userService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        Account loggedAccount = (Account) session.getAttribute("loggedAccount");
        User loggedUser = userService.getById(loggedAccount.getId());

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "parameter");
        return "parameter-admin/index";
    }

    @GetMapping("/api")
    // @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Map<String, Object>> getTalents(
            @RequestParam(value = "searchParameterName", required = false) String searchParameterName,
            @RequestParam(value = "searchParameterValue", required = false) String searchParameterValue,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<ReferenceResponse> parameterPage = referenceService.getFilteredReference(
                searchParameterName,
                searchParameterName, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("parameters", parameterPage.getContent());

        return ResponseEntity.ok(response);
    }
}
