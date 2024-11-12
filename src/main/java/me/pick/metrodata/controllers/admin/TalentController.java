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
import me.pick.metrodata.enums.StatusCV;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/talent")
@AllArgsConstructor
public class TalentController {

    private final TalentService talentService;
    private final UserService userService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        Account loggedAccount = (Account) session.getAttribute("loggedAccount");
        User loggedUser = userService.getById(loggedAccount.getId());

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "talent");
        return "talent-admin/index";
    }

    @GetMapping("/api")
    // @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Map<String, Object>> getTalents(
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "searchMitra", required = false) String searchMitra,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<TalentResponse> talentPage = talentService.getFilteredTalent(searchName,
                searchMitra, StatusCV.COMPLETE, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("talents", talentPage.getContent());

        return ResponseEntity.ok(response);
    }
}
