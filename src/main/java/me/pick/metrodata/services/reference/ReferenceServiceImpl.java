package me.pick.metrodata.services.reference;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.reference.ReferenceDoesNotExistException;
import me.pick.metrodata.models.entity.References;
import me.pick.metrodata.repositories.ReferenceRepository;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

@Service
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService{
    private final ReferenceRepository referenceRepository;

    @Cacheable("references-by-id")
    public References getReferenceById(Long id) {
        return referenceRepository.findById(id).orElseThrow(() -> new ReferenceDoesNotExistException(id));
    }
}
