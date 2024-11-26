package me.pick.metrodata.services.reference;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.reference.ReferenceDoesNotExistException;
import me.pick.metrodata.models.dto.requests.ReferenceRequest;
import me.pick.metrodata.models.dto.responses.ReferenceResponse;
import me.pick.metrodata.models.entity.References;
import me.pick.metrodata.repositories.ReferenceRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

@Service
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService {

    private final ReferenceRepository referenceRepository;
    private final ModelMapper modelMapper;

    @Cacheable("references-by-id")
    @Override
    public References getReferenceById(Long id) {
        return referenceRepository.findById(id).orElseThrow(() -> new ReferenceDoesNotExistException(id));
    }

    @Override
    public Page<ReferenceResponse> getFilteredReference(String searchParameterName, String searchParameterValue,
            Integer page,
            Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return referenceRepository.findAllWithFilters(
                searchParameterName, searchParameterValue, pageable).map(reference -> {
                    ReferenceResponse referenceResponse = modelMapper.map(
                            reference,
                            ReferenceResponse.class);
                    return referenceResponse;
                });
    }

    @Override
    public List<ReferenceResponse> getReferenceData(String referenceGroupName) {

    List<References> listReference = referenceRepository.findReferencesByGroup1(referenceGroupName);

    return listReference.stream()
            .map(reference -> {
                ReferenceResponse response = new ReferenceResponse();
                response.setId(reference.getId());
                response.setReference_name(reference.getReference_name());
                return response;
            })
            .collect(Collectors.toList());
    }

    public void create(ReferenceRequest referenceRequest) {
        References references = modelMapper.map(referenceRequest, References.class);
        references.setCreated_by(1);
        references.setIs_active(Boolean.TRUE);

        referenceRepository.save(references);
    }

    @Override
    public void update(Long id, ReferenceRequest referenceRequest) {
        References referencesOld = getReferenceById(id);
        References references = modelMapper.map(referenceRequest, References.class);
        references.setId(id);
        references.setCreated_at(referencesOld.getCreated_at());
        references.setCreated_by(referencesOld.getCreated_by());
        references.setIs_active(referencesOld.getIs_active());

        referenceRepository.save(references);
    }

    @Override
    public void delete(Long id) {
        referenceRepository.delete(getReferenceById(id));
    }
}
