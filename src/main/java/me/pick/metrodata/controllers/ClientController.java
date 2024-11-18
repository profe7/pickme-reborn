package me.pick.metrodata.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
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
import me.pick.metrodata.models.dto.requests.InterviewUpdateRequest;
import me.pick.metrodata.models.dto.responses.InterviewHistoryResponse;
import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.services.interview.InterviewScheduleService;
import me.pick.metrodata.services.recommendation.RecommendationService;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final RecommendationService recommendationService;

    private final InterviewScheduleService interviewScheduleService;

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

        return "recommendation/view-recommendation-talent";
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
            @RequestParam(required = false) InterviewStatus status) {

        Long clientId = (Long) session.getAttribute("clientId");

        Page<InterviewSchedule> schedules = interviewScheduleService.getAll(search, clientId, type, startDate, endDate, status, page, size);
        
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

        // Tetapkan interviewId dari path variable ke DTO
        request.setInterviewId(interviewId);
        System.out.println(request);

        // Panggil service untuk memproses perubahan status
        try {
            interviewScheduleService.updateInterviewStatus(request);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Tentukan pesan sukses berdasarkan status
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

        // Tambahkan pesan sukses ke RedirectAttributes
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        // Redirect ke halaman daftar wawancara
        return new RedirectView("/client/interview-schedules", true);
    }


}
