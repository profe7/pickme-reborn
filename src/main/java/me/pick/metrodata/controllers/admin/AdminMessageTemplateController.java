package me.pick.metrodata.controllers.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.enums.MessageTemplateEnum;
import me.pick.metrodata.models.dto.requests.MessageTemplateRequest;
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

    @GetMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_MESSAGE_TEMPLATE')")
    public String createForm(MessageTemplate messageTemplate, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "message-template");
        model.addAttribute("types", MessageTemplateEnum.values());

        return "message-template-admin/create";
    }

    @GetMapping("/update/{id}")
    // @PreAuthorize("hasAnyAuthority('UPDATE_MESSAGE_TEMPLATE')")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("message", messageTemplateService.getMessageTemplateById(id));
        model.addAttribute("isActive", "message-template");
        model.addAttribute("types", MessageTemplateEnum.values());

        return "message-template-admin/update";
    }

    @PostMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_PARAMETER')")
    public ResponseEntity<Void> create(@RequestBody MessageTemplateRequest request) {

        try {
            messageTemplateService.create(request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    // @PreAuthorize("hasAnyAuthority('UPDATE_PARAMETER')")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MessageTemplateRequest request) {

        try {
            messageTemplateService.update(id, request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
