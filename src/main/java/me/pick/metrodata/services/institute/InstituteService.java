package me.pick.metrodata.services.institute;

import me.pick.metrodata.enums.InstituteType;
import me.pick.metrodata.models.dto.requests.InstituteRequest;
import me.pick.metrodata.models.dto.requests.InstituteUpdateRequest;
import me.pick.metrodata.models.dto.responses.InstituteResponse;
import me.pick.metrodata.models.entity.Institute;

import java.util.List;

import org.springframework.data.domain.Page;

public interface InstituteService {

    Institute getInstituteById(Long id);

    Page<Institute> getAllInstitutes(String name, Long instituteTypeId, Integer currentPage, Integer perPage);

    List<Institute> getAll();

    void editInstitute(InstituteUpdateRequest request, Long id);

    Page<InstituteResponse> getFilteredInstitute(String searchName, InstituteType searchType, Integer page,
            Integer size);

    void create(InstituteRequest instituteRequest);

    void update(Long id, InstituteRequest instituteRequest);

    List<InstituteResponse> getAllInstituteByVacancyId(Long vacancyId);

}
