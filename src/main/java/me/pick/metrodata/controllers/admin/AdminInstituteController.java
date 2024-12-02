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
import me.pick.metrodata.enums.InstituteType;
import me.pick.metrodata.models.dto.requests.InstituteRequest;
import me.pick.metrodata.models.dto.responses.InstituteResponse;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.institute.InstituteService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/institute")
@AllArgsConstructor
public class AdminInstituteController {

    private final InstituteService instituteService;
    private final UserService userService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "institute");
        model.addAttribute("instituteTypes", InstituteType.values());

        return "institute-admin/index";
    }

    @GetMapping("/api")
    // @PreAuthorize("hasAnyAuthority('READ_TALENT')")
    public ResponseEntity<Map<String, Object>> getInstitutes(
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "searchType", required = false) InstituteType searchType,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<InstituteResponse> institutePage = instituteService.getFilteredInstitute(
                searchName, searchType, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("institutes", institutePage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_INSTITUTE')")
    public String createForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("rms", userService.usersWithSpecificPrivileges());
        model.addAttribute("isActive", "institute");
        model.addAttribute("instituteTypes", InstituteType.values());

        return "institute-admin/create";
    }

    @GetMapping("/update/{id}")
    // @PreAuthorize("hasAnyAuthority('UPDATE_INSTITUTE')")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("institute", instituteService.getInstituteById(id));
        model.addAttribute("rms", userService.usersWithSpecificPrivileges());
        model.addAttribute("isActive", "institute");
        model.addAttribute("instituteTypes", InstituteType.values());

        return "institute-admin/update";
    }

    @PostMapping("/create")
    // @PreAuthorize("hasAnyAuthority('CREATE_PARAMETER')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody InstituteRequest instituteRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            instituteService.create(instituteRequest);
            response.put("message", "Instansi baru berhasil ditambahkan");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat menambahkan instansi baru");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    // @PreAuthorize("hasAnyAuthority('UPDATE_PARAMETER')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody InstituteRequest instituteRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            instituteService.update(id, instituteRequest);
            response.put("message", "Instansi berhasil diperbarui");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat memperbarui instansi");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
