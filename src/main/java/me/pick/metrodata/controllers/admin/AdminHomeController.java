package me.pick.metrodata.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.services.user.UserService;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminHomeController {

    private final UserService userService;

    @GetMapping
    // @PreAuthorize("hasAnyAuthority('MANAGEMENT_READ_ACCOUNT','EXTERNAL_READ_ACCOUNT')")
    public String home(Model model, HttpServletRequest request) {

        User loggedUser = userService.getById((Long) request.getSession().getAttribute("userId"));

        model.addAttribute("logged", loggedUser);

        model.addAttribute("isActive", "home");

        return "admin";
    }

    @GetMapping("/share/{id}")
    public String share(Model model, @PathVariable Long id) {

        Talent talent = userService.getById(id).getTalent();

        model.addAttribute("showFullName", talent.getName());
        model.addAttribute("showProfilePicture", talent.getPhoto());
        model.addAttribute("showDateOfBirth", talent.getBirthOfDate());
        model.addAttribute("showBirthPlace", talent.getPlaceOfBirth());
        model.addAttribute("showNationality", talent.getNationality().getReference_name());
        model.addAttribute("showMaritialStatus", talent.getMaritalStatus());
        model.addAttribute("showGender", talent.getGender());
        model.addAttribute("showReligion", talent.getReligion().name());
        model.addAttribute("showEmail", talent.getEmail());
        model.addAttribute("showPhone", talent.getPhone());
        model.addAttribute("showAddress", talent.getFullAddress());
        model.addAttribute("languageData", talent.getLanguageSkills());
        model.addAttribute("educationData", talent.getEducations());
        model.addAttribute("skillData", talent.getSkills());
        model.addAttribute("workData", talent.getJobHistories());
        model.addAttribute("projectData", talent.getProjects());
        model.addAttribute("trainingData", talent.getTrainings());
        model.addAttribute("certificationData", talent.getCertifications());
        model.addAttribute("organizationData", talent.getOrganizations());
        model.addAttribute("experienceData", talent.getOtherExperiences());
        model.addAttribute("awardData", talent.getAchievement());

        return "components-admin/share";
    }

}
