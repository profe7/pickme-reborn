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
import me.pick.metrodata.models.entity.MessageTemplate;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.message.MessageTemplateService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/message-template")
@AllArgsConstructor
public class AdminMessageTemplateController {

    private final UserService userService;
    private final MessageTemplateService messageTemplateService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "message-template");
        return "message-template-admin/index";
    }

    @GetMapping("/api")
    // @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Map<String, Object>> getRMessageTemplate(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<MessageTemplate> messageTemplatePage = messageTemplateService.getFilteredMessageTemplate(page,
                size);

        Map<String, Object> response = new HashMap<>();
        response.put("messages", messageTemplatePage.getContent());

        return ResponseEntity.ok(response);
    }
}
