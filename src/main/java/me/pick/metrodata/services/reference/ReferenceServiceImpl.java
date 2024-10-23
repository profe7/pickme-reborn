package me.pick.metrodata.services.reference;

import me.pick.metrodata.exceptions.reference.ReferenceDoesNotExistException;
import me.pick.metrodata.models.entity.References;
import me.pick.metrodata.repositories.ReferenceRepository;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

@Service
public class ReferenceServiceImpl implements ReferenceService{
    private final ReferenceRepository referenceRepository;

    public ReferenceServiceImpl(ReferenceRepository referenceRepository) {
        this.referenceRepository = referenceRepository;
    }

    @Cacheable("references-by-id")
    public References getReferenceById(Long id) {
        return referenceRepository.findById(id).orElseThrow(() -> new ReferenceDoesNotExistException(id));
    }
}
