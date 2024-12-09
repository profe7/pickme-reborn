package me.pick.metrodata.controllers.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('READ_MESSAGE_TEMPLATE')")
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "message-template");
        return "message-template-admin/index";
    }

    @GetMapping("/api")
    @PreAuthorize("hasAnyAuthority('READ_MESSAGE_TEMPLATE')")
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
    @PreAuthorize("hasAnyAuthority('CREATE_MESSAGE_TEMPLATE')")
    public String createForm(MessageTemplate messageTemplate, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "message-template");
        model.addAttribute("types", MessageTemplateEnum.values());

        return "message-template-admin/create";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE_MESSAGE_TEMPLATE')")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("message", messageTemplateService.getMessageTemplateById(id));
        model.addAttribute("isActive", "message-template");
        model.addAttribute("types", MessageTemplateEnum.values());

        return "message-template-admin/update";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATE_MESSAGE_TEMPLATE')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody MessageTemplateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            messageTemplateService.create(request);
            response.put("message", "Template pesan baru berhasil ditambahkan");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat menambahkan template pesan baru");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE_MESSAGE_TEMPLATE')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody MessageTemplateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            messageTemplateService.update(id, request);
            response.put("message", "Template pesan berhasil diperbarui");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat memperbarui template pesan");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
