package me.pick.metrodata.services.reference;

import org.springframework.data.domain.Page;

import me.pick.metrodata.models.dto.requests.ReferenceRequest;
import me.pick.metrodata.models.dto.responses.ReferenceResponse;
import me.pick.metrodata.models.entity.References;

public interface ReferenceService {

    References getReferenceById(Long id);

    Page<ReferenceResponse> getFilteredReference(String searchParameterName, String searchParameterValue, Integer page,
            Integer size);

    void create(ReferenceRequest referenceRequest);

    void update(Long id, ReferenceRequest referenceRequest);

    void delete(Long id);
}
