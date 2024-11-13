package me.pick.metrodata.services.talent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.enums.StatusCV;
import me.pick.metrodata.exceptions.reference.ReferenceDoesNotExistException;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.exceptions.role.RoleDoesNotExistException;
import me.pick.metrodata.exceptions.talent.IncompleteTalentRequestException;
import me.pick.metrodata.exceptions.talent.InvalidTalentNikException;
import me.pick.metrodata.exceptions.talent.TalentAlreadyExistException;
import me.pick.metrodata.exceptions.talent.TalentDoesNotExistException;
import me.pick.metrodata.exceptions.vacancy.VacancyNotExistException;
import me.pick.metrodata.models.dto.requests.*;
import me.pick.metrodata.models.dto.responses.TalentAvailableForVacancyResponse;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.dto.responses.TalentSimpleResponse;
import me.pick.metrodata.models.entity.*;
import me.pick.metrodata.repositories.*;
import me.pick.metrodata.repositories.specifications.TalentSpecification;
import me.pick.metrodata.services.applicant.ApplicantService;
import me.pick.metrodata.services.email.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class TalentServiceImpl implements TalentService {
    private final TalentRepository talentRepository;
    private final MitraRepository mitraRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final ApplicantService applicantService;
    private final ReferenceRepository referenceRepository;
    private final LanguageSkillRepository languageSkillRepository;
    private final EducationRepository educationRepository;
    private final SkillRepository skillRepository;
    private final JobHistoryRepository jobHistoryRepository;
    private final ProjectRepository projectRepository;
    private final TrainingRepository trainingRepository;
    private final CertificationRepository certificationRepository;
    private final OrganizationRepository organizationRepository;
    private final OtherExperienceRepository otherExperienceRepository;
    private final AchievementsRepository achievementsRepository;
    private final VacancyRepository vacancyRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    private Talent findByIdFromRepo(String id) {
        return talentRepository.findById(id).orElseThrow(() -> new TalentDoesNotExistException(id));
    }

    @Override
    public Talent getTalentDetail(String id) {
        return findByIdFromRepo(id);
    }

    @Override
    public Page<Talent> getAll(Integer page, Integer size, String search, Long institute, Long baseSalary,
            Long limitSalary, Boolean active, String job, String skill, Boolean idle) {
        Specification<Talent> spec = TalentSpecification.buildSpecification(search, baseSalary, limitSalary, active,
                institute, job, skill, idle);
        List<Talent> talents = talentRepository.findAll(spec);

        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), talents.size());

        return new PageImpl<>(talents.subList(start, end), pageable, talents.size());
    }

    @Override
    public TalentResponse getById(String id) {
        Talent talent = findByIdFromRepo(id);
        return modelMapper.map(talent, TalentResponse.class);
    }

    @Override
    public Talent createViaVacancy(TalentFromVacancyRequest request) {
        Talent existing = talentRepository.findByNik(request.getTalentNik()).orElse(null);
        if (existing != null) {
            throw new TalentAlreadyExistException(request.getTalentName());
        }

        if (request.getTalentNik().length() != 16) {
            throw new InvalidTalentNikException(request.getTalentNik());
        }

        Account newTalentAccount = new Account();
        User newTalentUser = new User();
        Talent newTalent = new Talent();

        newTalentAccount.setUsername(request.getTalentEmail().split("@")[0].replace(".", ""));
        newTalentAccount.setPassword(passwordEncoder.encode(request.getTalentNik()));
        newTalentAccount.setRole(roleRepository.findById(6L).orElseThrow(() -> new RoleDoesNotExistException(6L)));

        String[] nameParts = request.getTalentName().split(" ");

        newTalentUser.setFirstName(nameParts[0]);

        String lastName = "";

        if (nameParts.length > 1) {
            lastName = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length));
        }

        newTalentUser.setLastName(lastName);
        newTalentUser.setMitra(mitraRepository.findById(request.getTalentMitraId())
                .orElseThrow(() -> new MitraDoesNotExistException(request.getTalentMitraId())));
        newTalentUser.setEmail(request.getTalentEmail());
        userRepository.save(newTalentUser);

        newTalentAccount.setUser(newTalentUser);
        accountRepository.save(newTalentAccount);

        newTalent.setUser(newTalentUser);
        newTalent.setName(request.getTalentName());
        newTalent.setEmail(request.getTalentEmail());
        newTalent.setNik(request.getTalentNik());
        newTalent.setMitra(newTalentUser.getMitra());
        newTalent.setUser(newTalentUser);
        newTalent.setStatusCV(StatusCV.DRAFT);
        talentRepository.save(newTalent);

        ApplicantCreationRequest applicantCreationRequest = new ApplicantCreationRequest(request.getVacancyId(),
                newTalent.getId());
        applicantCreationRequest.setTalentId(newTalent.getId());
        applicantCreationRequest.setVacancyId(request.getVacancyId());
        applicantService.createApplicant(applicantCreationRequest);

        emailService.sendNewTalentCredentials(newTalentAccount, request.getTalentNik());

        return newTalent;
    }

    @Override
    public TalentAvailableForVacancyResponse availableForVacancy(Long vacancyId, Long mitraId) {
        Vacancy vacancy = vacancyRepository.findVacancyById(vacancyId)
                .orElseThrow(() -> new VacancyNotExistException(vacancyId));
        Mitra mitra = mitraRepository.findById(mitraId).orElseThrow(() -> new MitraDoesNotExistException(mitraId));

        List<Talent> available = talentRepository.findTalentsWithCompleteCVByMitra(mitra.getId());
        TalentAvailableForVacancyResponse responses = new TalentAvailableForVacancyResponse();

        available = available.stream()
                .filter(talent -> talent.getApplicants().parallelStream()
                        .noneMatch(applicant -> applicant.getVacancy().getId().equals(vacancy.getId())
                                || applicant.getStatus() == ApplicantStatus.ACCEPTED))
                .toList();

        responses.setTalents(availableForVacancyHelper(available));

        return responses;
    }

    @Override
    public Talent completeNewTalentData(TalentDataCompletionRequest request) {
        Talent talent = talentRepository.findById(request.getTalentId())
                .orElseThrow(() -> new TalentDoesNotExistException(request.getTalentId()));
        return talentFullDataHelper(request, talent);
    }

    @Override
    public Talent createNewTalent(TalentDataCompletionRequest request) {
        Talent existing = talentRepository.findByNik(request.getTalentNik()).orElse(null);
        if (existing != null) {
            throw new TalentAlreadyExistException(request.getTalentFullName());
        }
        Talent newTalent = new Talent();
        newTalent.setMitra(mitraRepository.findById(request.getMitraId())
                .orElseThrow(() -> new MitraDoesNotExistException(request.getMitraId())));
        return talentFullDataHelper(request, newTalent);
    }

    private Talent talentFullDataHelper(TalentDataCompletionRequest request, Talent talent) {
        talent.setName(request.getTalentFullName());
        talent.setNik(request.getTalentNik());
        talent.setBirthOfDate(request.getDateOfBirth());
        talent.setPlaceOfBirth(request.getPlaceOfBirth());
        talent.setNationality(referenceRepository.findById(request.getNationalityId())
                .orElseThrow(() -> new ReferenceDoesNotExistException(request.getNationalityId())));
        talent.setProvince(referenceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new ReferenceDoesNotExistException(request.getProvinceId())));
        talent.setCity(referenceRepository.findById(request.getCityId())
                .orElseThrow(() -> new ReferenceDoesNotExistException(request.getCityId())));
        talent.setFullAddress(request.getFullAddress());

        talentLanguageSkillHelper(request.getLanguageSkills(), talent);

        talentEducationsHelper(request.getEducations(), talent);

        talentSkillsHelper(request.getSkills(), talent);

        if (!talent.getJobHistories().isEmpty()) {
            talentJobHistoryHelper(request.getJobHistory(), talent);
        }

        if (!talent.getProjects().isEmpty()) {
            talentProjectHelper(request.getProjects(), talent);
        }

        if (!talent.getTrainings().isEmpty()) {
            talentTrainingHelper(request.getTrainings(), talent);
        }

        if (!talent.getCertifications().isEmpty()) {
            talentCertificationHelper(request.getCertifications(), talent);
        }

        if (!talent.getOrganizations().isEmpty()) {
            talentOrganizationHelper(request.getOrganizations(), talent);
        }

        if (!talent.getOtherExperiences().isEmpty()) {
            talentOtherExperienceHelper(request.getOtherExperiences(), talent);
        }

        if (!talent.getAchievement().isEmpty()) {
            talentAchievementHelper(request.getAchievements(), talent);
        }

        talent.setStatusCV(StatusCV.COMPLETE);

        return talentRepository.save(talent);
    }

    private void talentLanguageSkillHelper(List<LanguageSkillRequest> request, Talent talent) {
        if (request.isEmpty()) {
            throw new IncompleteTalentRequestException("language skills");
        } else {
            for (LanguageSkillRequest languageSkills : request) {
                languageSkillRepository.save(
                        new LanguageSkill(
                                languageSkills.getReadingAbilityLevel(),
                                languageSkills.getWritingAbilityLevel(),
                                languageSkills.getSpeakingAbilityLevel(),
                                referenceRepository.findById(languageSkills.getLanguageId()).orElseThrow(
                                        () -> new ReferenceDoesNotExistException(languageSkills.getLanguageId())),
                                talent));
            }
        }
    }

    private void talentEducationsHelper(List<EducationRequest> request, Talent talent) {
        if (request.isEmpty()) {
            throw new IncompleteTalentRequestException("educations");
        } else {
            for (EducationRequest education : request) {
                educationRepository.save(
                        new Education(
                                education.getEducationalLevel(),
                                education.getStartDate(),
                                education.getEndDate(),
                                education.getAcademicGrade(),
                                education.getInstitution(),
                                referenceRepository.findById(education.getMajorId())
                                        .orElseThrow(() -> new ReferenceDoesNotExistException(education.getMajorId())),
                                talent));
            }
        }
    }

    private void talentSkillsHelper(List<SkillRequest> request, Talent talent) {
        if (request.isEmpty()) {
            throw new IncompleteTalentRequestException("skills");
        } else {
            for (SkillRequest skill : request) {
                skillRepository.save(
                        new Skill(
                                skill.getName(),
                                skill.getCategory(),
                                skill.getLevel(),
                                talent));
            }
        }
    }

    private void talentJobHistoryHelper(List<JobHistoryRequest> request, Talent talent) {
        for (JobHistoryRequest jobHistory : request) {
            jobHistoryRepository.save(
                    new JobHistory(
                            jobHistory.getCompanyName(),
                            jobHistory.getContractStatus(),
                            jobHistory.getStartDate(),
                            jobHistory.getEndDate(),
                            jobHistory.getDescription(),
                            jobHistory.getProjectSpecification(),
                            talent,
                            referenceRepository.findById(jobHistory.getPositionId()).orElseThrow(
                                    () -> new ReferenceDoesNotExistException(jobHistory.getPositionId()))));
        }
    }

    private void talentProjectHelper(List<ProjectRequest> request, Talent talent) {
        for (ProjectRequest project : request) {
            projectRepository.save(
                    new Project(
                            project.getProjectName(),
                            project.getInstitution(),
                            project.getDescription(),
                            project.getStartDate(),
                            project.getEndDate(),
                            talent,
                            referenceRepository.findById(project.getSkillId())
                                    .orElseThrow(() -> new ReferenceDoesNotExistException(project.getSkillId()))));
        }
    }

    private void talentTrainingHelper(List<TrainingRequest> request, Talent talent) {
        for (TrainingRequest training : request) {
            trainingRepository.save(
                    new Training(
                            training.getTrainingName(),
                            training.getTrainingDate(),
                            training.getSyllabus(),
                            talent));
        }
    }

    private void talentCertificationHelper(List<CertificationRequest> request, Talent talent) {
        for (CertificationRequest certification : request) {
            certificationRepository.save(
                    new Certification(
                            certification.getCertificateName(),
                            certification.getInstitutionName(),
                            certification.getCertificateIssueDate(),
                            certification.getValidUntil(),
                            talent));
        }
    }

    private void talentOrganizationHelper(List<OrganizationRequest> request, Talent talent) {
        for (OrganizationRequest organization : request) {
            organizationRepository.save(
                    new Organization(
                            organization.getOrganizationName(),
                            organization.getOrganizationPosition(),
                            organization.getStartDate(),
                            organization.getEndDate(),
                            talent));
        }
    }

    private void talentOtherExperienceHelper(List<OtherExperienceRequest> request, Talent talent) {
        for (OtherExperienceRequest otherExperience : request) {
            otherExperienceRepository.save(
                    new OtherExperience(
                            otherExperience.getExperienceName(),
                            otherExperience.getInstituteName(),
                            otherExperience.getPositionName(),
                            otherExperience.getPositionName(),
                            otherExperience.getExperienceDate(),
                            talent));
        }
    }

    private void talentAchievementHelper(List<AchievementRequest> request, Talent talent) {
        for (AchievementRequest achievement : request) {
            achievementsRepository.save(
                    new Achievements(
                            achievement.getAchievementName(),
                            achievement.getInstitution(),
                            achievement.getAchievementDate(),
                            talent));
        }
    }

    private List<TalentSimpleResponse> availableForVacancyHelper(List<Talent> talents) {
        return talents.stream().map(
                talent -> {
                    TalentSimpleResponse response = new TalentSimpleResponse();
                    response.setTalentId(talent.getId());
                    response.setTalentName(talent.getName());
                    response.setTalentPosition(talent.getJobHistories().getLast().getPosition().getReference_name());
                    response.setTalentSkill(talent.getSkills().getLast().getName());
                    response.setStatusCV(talent.getStatusCV().toString());
                    return response;
                }).toList();

    }

    @Override
    public List<Talent> getByMitraId(Long mitraId) {
        return talentRepository.findTalentByMitraId(mitraId);
    }

    @Override
    public Page<TalentResponse> getFilteredTalent(String searchName, String searchMitra, StatusCV status, Integer page,
            Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return talentRepository.findAllWithFilters(searchName, searchMitra, status, pageable).map(talent -> {
            TalentResponse talentResponse = modelMapper.map(talent,
                    TalentResponse.class);
            talentResponse.setInstituteName(talent.getInstitute().getInstituteName());
            return talentResponse;
        });
    }
}
