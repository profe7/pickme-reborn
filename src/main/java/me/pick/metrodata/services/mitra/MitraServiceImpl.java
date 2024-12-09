package me.pick.metrodata.services.mitra;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.ApplicantStatus;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.models.dto.responses.MitraDashboardTelemetryResponse;
import me.pick.metrodata.models.dto.responses.VacancyDashboardTelemetryResponse;
import me.pick.metrodata.models.entity.Mitra;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.repositories.ApplicantRepository;
import me.pick.metrodata.repositories.MitraRepository;
import me.pick.metrodata.repositories.TalentRepository;
import me.pick.metrodata.repositories.VacancyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MitraServiceImpl implements MitraService{

    private final TalentRepository talentRepository;
    private final MitraRepository mitraRepository;
    private final ApplicantRepository applicantRepository;
    private final VacancyRepository vacancyRepository;

    public Page<Talent> getMitraTalents(Long mitraId, Integer page, Integer size) {
        Mitra mitra = mitraRepository.findById(mitraId).orElseThrow(() -> new MitraDoesNotExistException(mitraId));
        List<Talent> talents = talentRepository.findTalentByMitraId(mitra.getId());
        Pageable pageable =  PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), talents.size());

        return new PageImpl<>(talents.subList(start, end), pageable, talents.size());
    }

    public MitraDashboardTelemetryResponse getMitraDashboardTelemetry(Long mitraId) {
        Mitra mitra = mitraRepository.findById(mitraId).orElseThrow(() -> new MitraDoesNotExistException(mitraId));
        Long availableVacancies = vacancyRepository.countActiveVacancy();
        Long totalTApplicants = applicantRepository.countApplicantByStatusAndMitra(mitra.getId(), ApplicantStatus.ASSIGNED);
        Long totalRejectedApplicants = applicantRepository.countApplicantByStatusAndMitra(mitra.getId(), ApplicantStatus.REJECTED);
        Long totalAcceptedApplicants = applicantRepository.countApplicantByStatusAndMitra(mitra.getId(), ApplicantStatus.ACCEPTED);

        MitraDashboardTelemetryResponse response = new MitraDashboardTelemetryResponse();
        response.setAvailableVacancies(availableVacancies);
        response.setTotalApplicants(totalTApplicants);
        response.setTotalRejectedApplicants(totalRejectedApplicants);
        response.setTotalAcceptedApplicants(totalAcceptedApplicants);
        response.setNewestVacancies(vacancyResponseHelper(vacancyRepository.findTop5ByOrderByCreatedAtDesc()));
        return response;
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
