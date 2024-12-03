package me.pick.metrodata.controllers.admin;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
import me.pick.metrodata.models.dto.requests.HolidayRequest;
import me.pick.metrodata.models.entity.Holiday;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.holiday.HolidayService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/holiday")
@AllArgsConstructor
public class AdminHolidayController {

    private final HolidayService holidayService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('READ_HOLIDAY')")
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "holiday");

        return "holiday-admin/index";
    }

    @GetMapping("/api")
    @PreAuthorize("hasAnyAuthority('READ_HOLIDAY')")
    public ResponseEntity<Map<String, Object>> getHolidays(
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {

        Page<Holiday> holidayPage = holidayService.getFilteredHoliday(searchName, date, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("holidays", holidayPage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATE_HOLIDAY')")
    public String createForm(Holiday holiday, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("isActive", "holiday");
        model.addAttribute("logged", loggedUser);

        return "holiday-admin/create";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE_HOLIDAY')")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("isActive", "holiday");
        model.addAttribute("logged", loggedUser);
        model.addAttribute("holiday", holidayService.getHolidayById(id));

        return "holiday-admin/update";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATE_HOLIDAY')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody HolidayRequest holidayRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            holidayService.create(holidayRequest);
            response.put("message", "Hari libur baru berhasil ditambahkan");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat menambahkan hari libur baru");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE_HOLIDAY')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody HolidayRequest holidayRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            holidayService.update(id, holidayRequest);
            response.put("message", "Hari libur berhasil diperbarui");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan saat memperbarui hari libur");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_HOLIDAY')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        try {
            holidayService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
