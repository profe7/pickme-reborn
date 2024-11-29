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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.dto.responses.InterviewScheduleResponse;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.client.ClientService;
import me.pick.metrodata.services.interview.InterviewScheduleService;
import me.pick.metrodata.services.interviewhistory.InterviewScheduleHistoryService;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.services.user.UserService;

@Controller
@RequestMapping("/admin/interview-schedule")
@AllArgsConstructor
public class AdminInterviewScheduleController {

    private final InterviewScheduleService interviewScheduleService;
    private final InterviewScheduleHistoryService interviewScheduleHistoryService;
    private final UserService userService;
    private final TalentService talentService;
    private final ClientService clientService;

    @GetMapping
    // @PreAuthorize("hasAnyAuthority('READ_INTERVIEW')")
    public String index(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("isActive", "schedule");
        model.addAttribute("statuses", InterviewStatus.values());
        model.addAttribute("types", InterviewType.values());

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

    @GetMapping("/history/{id}")
    // @PreAuthorize("hasAnyAuthority('READ_INTERVIEW')")
    public String history(@PathVariable Long id, Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));
        InterviewSchedule interviewSchedule = interviewScheduleService.getById(id);
        String recruiter = interviewSchedule.getClient().getUser().getFirstName()
                + " " + interviewSchedule.getClient().getUser().getLastName();
        String talent = interviewSchedule.getApplicant().getTalent().getName();

        model.addAttribute("logged", loggedUser);
        model.addAttribute("histories", interviewScheduleHistoryService.getByInterviewScheduleId(id));
        model.addAttribute("recruiter", recruiter);
        model.addAttribute("isActive", "schedule");
        model.addAttribute("talent", talent);

        return "interview-schedule-admin/history";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);
        model.addAttribute("talents", talentService.getTalents());
        model.addAttribute("isActive", "schedule");
        model.addAttribute("clients", clientService.getClients());

        return "interview-schedule-admin/create";
    }
}
