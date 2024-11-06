package me.pick.metrodata.services.institute;

import me.pick.metrodata.models.dto.requests.InstituteUpdateRequest;
import me.pick.metrodata.models.entity.Institute;
import org.springframework.data.domain.Page;

public interface InstituteService {
    Institute getInstituteById(Long id);

    Page<Institute> getAllInstitutes(String name, Long instituteTypeId, Integer currentPage, Integer perPage);

    void editInstitute(InstituteUpdateRequest request, Long id);
}
