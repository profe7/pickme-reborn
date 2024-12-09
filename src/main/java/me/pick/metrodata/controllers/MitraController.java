package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import me.pick.metrodata.enums.ContractStatus;
import me.pick.metrodata.enums.EducationalLevel;
import me.pick.metrodata.enums.Gender;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.MaritalStatus;
import me.pick.metrodata.enums.OrganizationPosition;
import me.pick.metrodata.enums.ReligionsEnum;
import me.pick.metrodata.enums.SkillCategory;
import me.pick.metrodata.enums.SkillLevel;
import me.pick.metrodata.models.dto.requests.TalentDataCompletionRequest;
import me.pick.metrodata.models.dto.responses.MitraDashboardTelemetryResponse;
import me.pick.metrodata.models.dto.responses.MitraTalentInterviewStatistics;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.services.mitra.MitraService;
import me.pick.metrodata.services.reference.ReferenceService;
import me.pick.metrodata.services.talent.TalentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpSession;

@Controller
@PreAuthorize("hasRole('ROLE_MITRA')")
@RequestMapping("/mitra")
@AllArgsConstructor
public class MitraController {

    private final MitraService mitraService;
    private static final String SUCCESS = "SUCCESS";
    private final ReferenceService referenceService;
    private final TalentService talentService;

    @GetMapping
    public String mitraHomePage(Model model, HttpSession session) {
        Long mitraId = (Long) session.getAttribute("mitraId");

        MitraDashboardTelemetryResponse response = mitraService.getMitraDashboardTelemetry(mitraId);

        model.addAttribute("availableVacancies", response.getAvailableVacancies());
        model.addAttribute("totalApplicants", response.getTotalApplicants());
        model.addAttribute("totalRejectedApplicants", response.getTotalRejectedApplicants());
        model.addAttribute("totalAcceptedApplicants", response.getTotalAcceptedApplicants());
        model.addAttribute("totalAssignedApplicants", response.getTotalAssignedApplicants());
        model.addAttribute("mitraId", mitraId);

        model.addAttribute("newestVacancies", response.getNewestVacancies());

        return "mitra/dashboard-mitra";
    }

    @GetMapping("/chart-data")
    @ResponseBody
    public Map<String, Long> getChartData(HttpSession session) {
        Long mitraId = (Long) session.getAttribute("mitraId");

        MitraDashboardTelemetryResponse response = mitraService.getMitraDashboardTelemetry(mitraId);

        Map<String, Long> chartData = new HashMap<>();
        chartData.put("totalRejectedApplicants", response.getTotalRejectedApplicants());
        chartData.put("totalAcceptedApplicants", response.getTotalAcceptedApplicants());
        chartData.put("totalAssignedApplicants", response.getTotalAssignedApplicants());

        return chartData;
    }

    @GetMapping("/talent")
    public String viewAllTalentByMitra(HttpSession session,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String skill,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Model model) {
        Long mitraId = (Long) session.getAttribute("mitraId");
        Page<Talent> talents = mitraService.getFilteredMitraTalents(mitraId, page, size, position, skill);
        model.addAttribute("talents", talents);
        model.addAttribute("currentPage", page);
        model.addAttribute("position", position);
        model.addAttribute("skill", skill);
        return "mitra/mitra-view-all-talents";
    }

    @GetMapping("/result")
    @ResponseBody
    public List<MitraTalentInterviewStatistics> viewResultbyMitra(
            HttpSession session,
            @RequestParam(required = false) String status) {
        Long mitraId = (Long) session.getAttribute("mitraId");

        InterviewStatus interviewStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                interviewStatus = InterviewStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }

        return mitraService.getMitraTalentInterviewStatistics(mitraId, interviewStatus);
    }



    @GetMapping("/data-completion/{talentId}")
    public String formAddCompletedTalentData(HttpSession session, @PathVariable("talentId") String talentId, Model model){
        Long mitraId = (long) session.getAttribute("mitraId");

        var talentDTO = new TalentDataCompletionRequest();

        Talent dataTalentBefore = talentService.findByIdFromRepo(talentId.toString());

        talentDTO.setMitraId(mitraId);
        talentDTO.setTalentId(dataTalentBefore.getId());
        talentDTO.setTalentFullName(dataTalentBefore.getName());
        talentDTO.setTalentNik(dataTalentBefore.getNik());
        talentDTO.setEmail(dataTalentBefore.getEmail());

        var nationality = referenceService.getReferenceData("nationality");
        Gender[] genders = Gender.values();
        MaritalStatus[] maritalStatus = MaritalStatus.values();
        ReligionsEnum[] religion = ReligionsEnum.values();
        var province = referenceService.getReferenceData("provinsi");
        var city = referenceService.getReferenceData("kota");

        model.addAttribute("nationality", nationality);
        model.addAttribute("genders", genders);
        model.addAttribute("maritalStatus", maritalStatus);
        model.addAttribute("religion", religion);
        model.addAttribute("province", province);
        model.addAttribute("city", city);

        
        var language = referenceService.getReferenceData("bahasa");
       
        model.addAttribute("languange", language);

        var educationMajor = referenceService.getReferenceData("university_major");
        EducationalLevel[] educationalLevels = EducationalLevel.values();

        model.addAttribute("educationLevel", educationalLevels);
        model.addAttribute("educationMajor", educationMajor);


        var skills = referenceService.getReferenceData("skills");
        SkillCategory[] skillCategories = SkillCategory.values();

        model.addAttribute("skills", skills);
        model.addAttribute("skillCategories", skillCategories);

        
        var jobPosition = referenceService.getReferenceData("posisi_pekerjaan");
        ContractStatus[] contractStatus = ContractStatus.values();

        model.addAttribute("jobPosition", jobPosition);
        model.addAttribute("contractStatus", contractStatus);

        
        SkillLevel[] skillLevels = SkillLevel.values();

        model.addAttribute("skillLevels", skillLevels);

     
        OrganizationPosition[] organizationPositions = OrganizationPosition.values();

        model.addAttribute("organizationPosition", organizationPositions);

        return "talent/talent-form-cv";

    }
    
    @PostMapping("/data-completion/{talentId}")
    public RedirectView talentDataCompletion(@ModelAttribute TalentDataCompletionRequest talentDTO, 
        @PathVariable("talentId") String talentId, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session){
        
        List<String> errorMessages = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                String message = error.getDefaultMessage();
                errorMessages.add(message);
            }
        }

        if (!talentDTO.getTalentNik().equals("-") && talentDTO.getTalentNik().length() != 16){
            errorMessages.add("Nomor KK harus terdiri dari 16 digit");
        } else if (talentService.checkNIKExists(talentDTO.getTalentNik(), talentId)) {
            errorMessages.add("NIK sudah terdaftar.");
        }

        if (!errorMessages.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new RedirectView("/data-completion");
        }

        talentService.completeNewTalentData(talentDTO);
        redirectAttributes.addFlashAttribute("success", "Data Talent berhasil disimpan");

        return new RedirectView("/data-completion");
    }

}
