package me.pick.metrodata.services.mitra;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.models.dto.responses.MitraDashboardTelemetryResponse;
import me.pick.metrodata.models.dto.responses.MitraTalentInterviewStatistics;
import me.pick.metrodata.models.entity.Talent;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MitraService {
    Page<Talent> getMitraTalents(Long mitraId, Integer page, Integer size);

    MitraDashboardTelemetryResponse getMitraDashboardTelemetry(Long mitraId);

    Page<Talent> getFilteredMitraTalents(Long mitraId, Integer page, Integer size, String position, String skill);

    List<MitraTalentInterviewStatistics> getMitraTalentInterviewStatistics(Long mitraId, InterviewStatus status);
    
}
