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
import me.pick.metrodata.services.applicant.ApplicantService;
import me.pick.metrodata.services.user.UserService;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminHomeController {

        private final UserService userService;
        private final ApplicantService applicantService;

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

                Talent talent = applicantService.getApplicantById(id).getTalent();

                model.addAttribute("showFullName", talent.getName() != null ? talent.getName() : "");
                model.addAttribute("showProfilePicture", talent.getPhoto() != null ? talent.getPhoto() : "");
                model.addAttribute("showDateOfBirth",
                                talent.getBirthOfDate() != null ? talent.getBirthOfDate().toString() : "");
                model.addAttribute("showBirthPlace", talent.getPlaceOfBirth() != null ? talent.getPlaceOfBirth() : "");
                model.addAttribute("showNationality",
                                talent.getNationality() != null ? talent.getNationality().getReference_name() : "");
                model.addAttribute("showMaritialStatus",
                                talent.getMaritalStatus() != null ? talent.getMaritalStatus() : "");
                model.addAttribute("showGender", talent.getGender() != null ? talent.getGender() : "");
                model.addAttribute("showReligion", talent.getReligion() != null ? talent.getReligion().name() : "");
                model.addAttribute("showEmail", talent.getEmail() != null ? talent.getEmail() : "");
                model.addAttribute("showPhone", talent.getPhone() != null ? talent.getPhone() : "");
                model.addAttribute("showAddress", talent.getFullAddress() != null ? talent.getFullAddress() : "");
                model.addAttribute("languageData",
                                talent.getLanguageSkills() != null ? talent.getLanguageSkills() : "");
                model.addAttribute("educationData", talent.getEducations() != null ? talent.getEducations() : "");
                model.addAttribute("skillData", talent.getSkills() != null ? talent.getSkills() : "");
                model.addAttribute("workData", talent.getJobHistories() != null ? talent.getJobHistories() : "");
                model.addAttribute("projectData", talent.getProjects() != null ? talent.getProjects() : "");
                model.addAttribute("trainingData", talent.getTrainings() != null ? talent.getTrainings() : "");
                model.addAttribute("certificationData",
                                talent.getCertifications() != null ? talent.getCertifications() : "");
                model.addAttribute("organizationData",
                                talent.getOrganizations() != null ? talent.getOrganizations() : "");
                model.addAttribute("experienceData",
                                talent.getOtherExperiences() != null ? talent.getOtherExperiences() : "");
                model.addAttribute("awardData", talent.getAchievement() != null ? talent.getAchievement() : "");

                return "share-admin/share";
        }

}
