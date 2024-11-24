package me.pick.metrodata.services.reference;

import java.util.List;

import org.springframework.data.domain.Page;

import me.pick.metrodata.models.dto.requests.ReferenceRequest;
import me.pick.metrodata.models.dto.responses.ReferenceResponse;
import me.pick.metrodata.models.entity.References;

public interface ReferenceService {

    References getReferenceById(Long id);

    Page<ReferenceResponse> getFilteredReference(String searchParameterName, String searchParameterValue, Integer page,
            Integer size);
    
    List<ReferenceResponse> getReferenceData(String referenceGroupName);

    void create(ReferenceRequest referenceRequest);

    void update(Long id, ReferenceRequest referenceRequest);

    void delete(Long id);
}
