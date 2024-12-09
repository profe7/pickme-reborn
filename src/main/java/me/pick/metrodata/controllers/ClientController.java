package me.pick.metrodata.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.models.dto.requests.AccountRequest;
import me.pick.metrodata.models.dto.requests.InterviewUpdateRequest;
import me.pick.metrodata.models.dto.responses.AccountResponse;
import me.pick.metrodata.models.dto.responses.ClientDashboardTelemetryResponse;
import me.pick.metrodata.models.dto.responses.ClientEmployeeResponse;
import me.pick.metrodata.models.dto.responses.InterviewHistoryResponse;
import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.services.account.AccountService;
import me.pick.metrodata.services.client.ClientService;
import me.pick.metrodata.services.interview.InterviewScheduleService;
import me.pick.metrodata.services.interviewhistory.InterviewScheduleHistoryService;
import me.pick.metrodata.services.recommendation.RecommendationService;

@Controller
@PreAuthorize("hasRole('ROLE_CLIENT')")
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final RecommendationService recommendationService;

    private final InterviewScheduleService interviewScheduleService;
    
    private final InterviewScheduleHistoryService interviewScheduleHistoryService;

    private final ClientService clientService;

    private final AccountService accountService;

    @GetMapping
    public String clientHomePage(HttpSession session, Model model){
        Long clientId = (Long) session.getAttribute("clientId");
        String name = (String) session.getAttribute("name");

        ClientDashboardTelemetryResponse employeeSummary = clientService.getClientDashboardTelemetry(clientId);
        List<ClientEmployeeResponse> clientEmployees = clientService.getClientEmployees(clientId);

        model.addAttribute("employeeSummary", employeeSummary);
        model.addAttribute("clientName", clientEmployees.get(0).getClientName());

        return "client/dashboard-client";
    }

    @GetMapping("/employees")
    public String clientEmployeesPage(HttpSession session, Model model){
        Long clientId = (Long) session.getAttribute("clientId");
        
        List<ClientEmployeeResponse> clientEmployees = clientService.getClientEmployees(clientId);
        ClientDashboardTelemetryResponse employeeSummary = clientService.getClientDashboardTelemetry(clientId);

        model.addAttribute("employees", clientEmployees);
        model.addAttribute("employeeSummary", employeeSummary);

        return "client/view-employees";
    }

    @PostMapping("/employees/delete/{talentId}")
    public RedirectView deleteEmployee(@PathVariable("talentId") String talentId, HttpSession session, RedirectAttributes redirectAttributes){

        Long clientId = (Long) session.getAttribute("clientId");

        clientService.deleteClientEmployee(clientId, talentId);
        redirectAttributes.addFlashAttribute("success", "Pegawai berhasil dihapus");

        return new RedirectView("/client/employees");

    }

    @GetMapping("/employees/detail/{talentId}")
    public String detailEmployee(@PathVariable("talentId") String talentId, HttpSession session, Model model){
        Long clientId = (Long) session.getAttribute("clientId");
        List<InterviewHistoryResponse> detailEmployee = interviewScheduleHistoryService.getByTalentIdandClientId(talentId, clientId);
        System.out.println(detailEmployee);

        model.addAttribute("employee", detailEmployee);

        return "employee/view-detail-employee";
    }
    
    @GetMapping("/recommendations")
    public String clientRecommendationPage(
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(required = false) String talentName,
            @RequestParam(required = false) String position){
        Long clientId = (Long) session.getAttribute("clientId");
        model.addAttribute("clientId", clientId);
        Page<RecommendationGroupedResponse> recommendations = recommendationService.getRecommendationClientPaged(page, size, clientId, talentName, position);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("pageNumbers", IntStream.range(0, recommendations.getTotalPages()).boxed().toList());

        return "client/view-recommendation-talent";
    }

    @GetMapping("/interview-schedules")
    public String getAllSchedules(
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) InterviewType type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) InterviewStatus status,
            @RequestParam(required = false) Long mitraId) {

        Long clientId = (Long) session.getAttribute("clientId");

        Page<InterviewSchedule> schedules = interviewScheduleService.getAll(search, clientId, type, startDate, endDate, status, mitraId, page, size);
        
        model.addAttribute("interviewSchedules", schedules);
        model.addAttribute("pageNumbers", IntStream.range(0, schedules.getTotalPages()).boxed().collect(Collectors.toList())); 

        return "interview-schedule/view-interview-schedule";
    }

    @GetMapping("interview-schedules/history/{interviewId}")
    public String viewTalentsInterviewScheduleHistory(
        @PathVariable("interviewId") Long interviewId, HttpSession session, Model model){

        List<InterviewHistoryResponse> talentInterviewHistory = interviewScheduleService.getTalentInterviewHistory(interviewId);

        model.addAttribute("interviewHistory", talentInterviewHistory);

        return "interview-schedule/view-interview-history-talent";
    }

    @PostMapping("/update-interview/{interviewId}")
    public RedirectView processTalentInterview(
            @PathVariable("interviewId") Long interviewId,
            @ModelAttribute InterviewUpdateRequest request,
            RedirectAttributes redirectAttributes,
            BindingResult bindingResult) {


        request.setInterviewId(interviewId);

        interviewScheduleService.updateInterviewStatus(request);

        String successMessage;
        switch (request.getStatus()) {
            case RESCHEDULED:
                successMessage = "Interview berhasil dijadwal ulang.";
                break;
            case CANCELLED:
                successMessage = "Interview berhasil dibatalkan.";
                break;
            case ACCEPTED:
                successMessage = "Talent berhasil diterima.";
                break;
            case REJECTED:
                successMessage = "Talent berhasil ditolak.";
                break;
            default:
                successMessage = "Talent berhasil diproses.";
                break;
        }

        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return new RedirectView("/client/interview-schedules");
    }

    @PostMapping("/invite-interview")
    public RedirectView inviteInterview(
            @ModelAttribute InterviewScheduleRequest interviewScheduleRequest,
            RedirectAttributes redirectAttributes) {
    
        interviewScheduleService.inviteToInterview(interviewScheduleRequest);
    
        redirectAttributes.addFlashAttribute("successMessage", "Undangan wawancara berhasil dikirim.");
        return new RedirectView("/client/interview-schedules"); 
    }


    @GetMapping("/edit-profile")
    public String formUpdateProfileClient(HttpSession session, Model model){
        Long clientId = (Long) session.getAttribute("accountId");

        var clientProfileDTO = new AccountRequest();

        AccountResponse dataClientBefore = accountService.getAccountById(clientId);

        clientProfileDTO.setEmail(dataClientBefore.getEmail());
        clientProfileDTO.setFirstName(dataClientBefore.getFirstName());
        clientProfileDTO.setLastName(dataClientBefore.getLastName());
        clientProfileDTO.setUsername(dataClientBefore.getUsername());

        model.addAttribute("clientDTO", clientProfileDTO);
        model.addAttribute("accountId", clientId);

        return "client/form-update-profile";

    }

    @PostMapping("/edit-profile")
    public RedirectView updateProfileClient(
            @ModelAttribute AccountRequest clientDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        List<String> errorMessages = new ArrayList<>();
        Long accountId = (Long) session.getAttribute("accountId");

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));
        }

        if (!accountService.isUsernameUnique(clientDTO.getUsername(), accountId)) {
            errorMessages.add("Username sudah digunakan atau sama dengan username saat ini.");
        }

        if (!accountService.isEmailUnique(clientDTO.getEmail(), accountId)) {
            errorMessages.add("Email sudah digunakan atau sama dengan email saat ini.");
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new RedirectView("/client/edit-profile");
        }
        
        accountService.updateProfile(accountId, clientDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Profil berhasil diperbarui.");
        return new RedirectView("/client/edit-profile");
    }



}
