package me.pick.metrodata.services.mitra;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.models.dto.responses.MitraDashboardTelemetryResponse;
import me.pick.metrodata.models.dto.responses.MitraTalentInterviewStatistics;
import me.pick.metrodata.models.dto.responses.VacancyDashboardTelemetryResponse;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.models.entity.Mitra;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.*;
import me.pick.metrodata.repositories.specifications.InterviewScheduleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MitraServiceImpl implements MitraService {

    private final TalentRepository talentRepository;
    private final MitraRepository mitraRepository;
    private final ApplicantRepository applicantRepository;
    private final VacancyRepository vacancyRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;

    @Override
    public Page<Talent> getMitraTalents(Long mitraId, Integer page, Integer size) {
        Mitra mitra = mitraRepository.findById(mitraId).orElseThrow(() -> new MitraDoesNotExistException(mitraId));
        List<Talent> talents = talentRepository.findTalentByMitraId(mitra.getId());
        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), talents.size());

        return new PageImpl<>(talents.subList(start, end), pageable, talents.size());
    }

    @Override
    public Page<Talent> getFilteredMitraTalents(Long mitraId, Integer page, Integer size, String position,
            String skill) {

        Page<Talent> pagedTalents = getMitraTalents(mitraId, page, size);

        List<Talent> talents = new ArrayList<>(pagedTalents.getContent());

        if (position != null && !position.isEmpty()) {
            talents = talents.stream()
                    .filter(t -> t.getApplicants().stream()
                            .anyMatch(a -> a.getVacancy() != null && a.getVacancy().getPosition().contains(position)))
                    .toList();
        }
        if (skill != null && !skill.isEmpty()) {
            talents = talents.stream()
                    .filter(t -> t.getSkills().stream()
                            .anyMatch(s -> s.getName().contains(skill)))
                    .toList();
        }

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), talents.size());
        int end = Math.min((start + pageable.getPageSize()), talents.size());

        return new PageImpl<>(talents.subList(start, end), pageable, talents.size());
    }

    @Override
    public MitraDashboardTelemetryResponse getMitraDashboardTelemetry(Long mitraId) {
        Mitra mitra = mitraRepository.findById(mitraId).orElseThrow(() -> new MitraDoesNotExistException(mitraId));
        Long availableVacancies = vacancyRepository.countActiveVacancy();
        Long totalTApplicants = applicantRepository.countApplicantByStatusAndMitra(mitra.getId(),
                ApplicantStatus.ASSIGNED);
        Long totalRejectedApplicants = applicantRepository.countApplicantByStatusAndMitra(mitra.getId(),
                ApplicantStatus.REJECTED);
        Long totalAcceptedApplicants = applicantRepository.countApplicantByStatusAndMitra(mitra.getId(),
                ApplicantStatus.ACCEPTED);
        Long totalAssignedApplicants = applicantRepository.countApplicantByStatusAndMitra(mitra.getId(),
                ApplicantStatus.ASSIGNED);

        MitraDashboardTelemetryResponse response = new MitraDashboardTelemetryResponse();
        response.setAvailableVacancies(availableVacancies);
        response.setTotalApplicants(totalTApplicants);
        response.setTotalRejectedApplicants(totalRejectedApplicants);
        response.setTotalAcceptedApplicants(totalAcceptedApplicants);
        response.setTotalAssignedApplicants(totalAssignedApplicants);
        response.setNewestVacancies(vacancyResponseHelper(vacancyRepository.findTop5ByOrderByCreatedAtDesc()));
        return response;
    }

    @Override
    public List<MitraTalentInterviewStatistics> getMitraTalentInterviewStatistics(Long mitraId, InterviewStatus status) {
        Mitra mitra = mitraRepository.findById(mitraId).orElseThrow(() -> new MitraDoesNotExistException(mitraId));
        List<MitraTalentInterviewStatistics> statistics = new ArrayList<>();
        Specification<InterviewSchedule> spec = InterviewScheduleSpecification.searchSpecification(null, null,
                null, null, null, status, mitra.getId());
        List<InterviewSchedule> schedules = interviewScheduleRepository.findAll(spec);

        schedules.forEach(schedule -> {
            MitraTalentInterviewStatistics stat = new MitraTalentInterviewStatistics();
            stat.setTalentName(schedule.getApplicant().getTalent().getName());
            if (schedule.getInterviewScheduleHistories().isEmpty() || schedule.getInterviewScheduleHistories().getLast().getFeedback() == null) {
                stat.setFeedback("No feedback");
            } else {
                stat.setFeedback(schedule.getInterviewScheduleHistories().getLast().getFeedback());
            }
            stat.setUpdatedAtDate(schedule.getUpdatedAt());
            stat.setOnboardDate(schedule.getOnBoardDate());
            stat.setRecruiterName(schedule.getClient().getUser().getInstitute().getInstituteName());
            statistics.add(stat);
        });

        return statistics;
    }

    private List<VacancyDashboardTelemetryResponse> vacancyResponseHelper(List<Vacancy> vacancies) {
        return vacancies.stream().map(vacancy -> {
            VacancyDashboardTelemetryResponse response = new VacancyDashboardTelemetryResponse();
            response.setVacancyId(vacancy.getId());
            response.setVacancyTitle(vacancy.getTitle());
            response.setVacancyApplicants(applicantRepository.countTotalApplicantByVacancy(vacancy.getId()));
            response.setExpiredDate(vacancy.getExpiredDate());
            response.setVacancyPosition(vacancy.getPosition());
            return response;
        }).toList();
    }

}
