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
import me.pick.metrodata.models.dto.requests.ReferenceRequest;
import me.pick.metrodata.models.dto.responses.ReferenceResponse;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.reference.ReferenceService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/parameter")
@AllArgsConstructor
public class AdminParameterController {

    private final ReferenceService referenceService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('READ_PARAMETER')")
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "parameter");
        return "parameter-admin/index";
    }

    @GetMapping("/api")
    @PreAuthorize("hasAnyAuthority('READ_PARAMETER')")
    public ResponseEntity<Map<String, Object>> getParameters(
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

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATE_PARAMETER')")
    public String createForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "parameter");

        return "parameter-admin/create";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE_PARAMETER')")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "parameter");
        model.addAttribute("parameter", referenceService.getReferenceById(id));

        return "parameter-admin/update";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATE_PARAMETER')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ReferenceRequest referenceRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            referenceService.create(referenceRequest);
            response.put("message", "Parameter baru berhasil ditambahkan");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat menambahkan parameter baru");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE_PARAMETER')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody ReferenceRequest referenceRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            referenceService.update(id, referenceRequest);
            response.put("message", "Parameter berhasil diperbarui");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat memperbarui parameter");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_PARAMETER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        try {
            referenceService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
