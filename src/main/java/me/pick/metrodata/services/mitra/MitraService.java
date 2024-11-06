package me.pick.metrodata.services.mitra;

import me.pick.metrodata.models.dto.responses.MitraDashboardTelemetryResponse;
import me.pick.metrodata.models.entity.Talent;
import org.springframework.data.domain.Page;

public interface MitraService {
    Page<Talent> getMitraTalents(Long mitraId, Integer page, Integer size);

    MitraDashboardTelemetryResponse getMitraDashboardTelemetry(Long mitraId);

    
}
