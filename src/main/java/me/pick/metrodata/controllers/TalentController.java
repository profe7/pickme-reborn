package me.pick.metrodata.controllers;


import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.ContractStatus;
import me.pick.metrodata.enums.EducationalLevel;
import me.pick.metrodata.enums.Gender;
import me.pick.metrodata.enums.MaritalStatus;
import me.pick.metrodata.enums.OrganizationPosition;
import me.pick.metrodata.enums.ReligionsEnum;
import me.pick.metrodata.enums.SkillCategory;
import me.pick.metrodata.enums.SkillLevel;
import me.pick.metrodata.models.dto.requests.TalentDataCompletionRequest;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.entity.Organization;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.services.reference.ReferenceService;
import me.pick.metrodata.services.talent.TalentService;

@Controller
@RequestMapping("/talent")
@RequiredArgsConstructor
public class TalentController {

    private final ReferenceService referenceService;

    private final TalentService talentService;

    @GetMapping("/data-completion/{talentId}")
    public String formAddCompletedTalentData(HttpSession session, @PathVariable("talentId") UUID talentId, Model model){
        Long mitraId = (long) session.getAttribute("mitraId");

        var talentDTO = new TalentDataCompletionRequest();

        Talent dataTalentBefore = talentService.findByIdFromRepo(talentId.toString());

        talentDTO.setMitraId(mitraId);
        talentDTO.setTalentId(dataTalentBefore.getId());
        talentDTO.setTalentFullName(dataTalentBefore.getName());
        talentDTO.setTalentNik(dataTalentBefore.getNik());
        talentDTO.setEmail(dataTalentBefore.getEmail());

        // data pribadi
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

        // kemampuan bahasa
        var language = referenceService.getReferenceData("bahasa");
       
        model.addAttribute("languange", language);

        // jenjang pendidikan
        var educationMajor = referenceService.getReferenceData("university_major");
        EducationalLevel[] educationalLevels = EducationalLevel.values();

        model.addAttribute("educationLevel", educationalLevels);
        model.addAttribute("educationMajor", educationMajor);

        // skills for keterampilan 
        var skills = referenceService.getReferenceData("skills");
        SkillCategory[] skillCategories = SkillCategory.values();

        model.addAttribute("skills", skills);
        model.addAttribute("skillCategories", skillCategories);

        // riwayat pekerjaan
        var jobPosition = referenceService.getReferenceData("posisi_pekerjaan");
        ContractStatus[] contractStatus = ContractStatus.values();

        model.addAttribute("jobPosition", jobPosition);
        model.addAttribute("contractStatus", contractStatus);

        // skill levels for kemampuan bahasa, keterampilan, projects field
        SkillLevel[] skillLevels = SkillLevel.values();

        model.addAttribute("skillLevels", skillLevels);

        // Organisasi
        OrganizationPosition[] organizationPositions = OrganizationPosition.values();

        model.addAttribute("organizationPosition", organizationPositions);

        return "talent/form-completion-talent";

    }
    
    @PostMapping("/data-completion")
    public RedirectView talentDataCompletion(@ModelAttribute TalentDataCompletionRequest talentDTO,
        BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session){
        
        List<String> errorMessages = new ArrayList<>();

        String talentId = talentDTO.getTalentId();

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
