package me.pick.metrodata.controllers.admin;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.dto.responses.InterviewScheduleResponse;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.interview.InterviewScheduleService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/interview-schedule")
@AllArgsConstructor
public class InterviewScheduleController {

    private final InterviewScheduleService interviewScheduleService;
    private final UserService userService;

    @GetMapping
    // @PreAuthorize("hasAnyAuthority('READ_INTERVIEW')")
    public String index(Model model, HttpSession session) {

        Account loggedAccount = (Account) session.getAttribute("loggedAccount");
        User loggedUser = userService.getById(loggedAccount.getId());

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "schedule");
        return "interview-schedule-admin/index";
    }

    @GetMapping("/api")
    public ResponseEntity<Map<String, Object>> getInterviewSchedules(
            @RequestParam(value = "searchRecruiter", required = false) String searchRecruiter,
            @RequestParam(value = "searchTalent", required = false) String searchTalent,
            @RequestParam(value = "type", required = false) InterviewType type,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(value = "status", required = false) InterviewStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InterviewScheduleResponse> interviewPage = interviewScheduleService.getFilteredInterviews(
                searchRecruiter, searchTalent, type, date, status, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("interviews", interviewPage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", interviewPage.getTotalPages());
        response.put("totalItems", interviewPage.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
