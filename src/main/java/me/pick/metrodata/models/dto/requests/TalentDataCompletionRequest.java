package me.pick.metrodata.models.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.Gender;
import me.pick.metrodata.enums.MaritalStatus;
import me.pick.metrodata.enums.ReligionsEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalentDataCompletionRequest {
    private byte[] photo;

    private String talentId;

    private Long mitraId;

    private String talentFullName;

    private String talentNik;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private Long nationalityId;

    private MaritalStatus maritalStatus;

    private Gender gender;

    private ReligionsEnum religion;

    private String phone;

    private String email;

    private Long provinceId;

    private Long cityId;

    private String fullAddress;

    private List<LanguageSkillRequest> languageSkills;

    private List<EducationRequest> educations;

    private List<SkillRequest> skills;

    private List<JobHistoryRequest> jobHistory;

    private List<ProjectRequest> projects;

    private List<TrainingRequest> trainings;

    private List<CertificationRequest> certifications;

    private List<OrganizationRequest> organizations;

    private List<OtherExperienceRequest> otherExperiences;

    private List<AchievementRequest> achievements;

}
