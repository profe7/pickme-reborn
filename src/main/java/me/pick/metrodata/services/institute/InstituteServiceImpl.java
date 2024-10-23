package me.pick.metrodata.services.institute;

import me.pick.metrodata.exceptions.institute.InstituteDoesNotExistException;
import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.repositories.InstituteRepository;
import org.springframework.stereotype.Service;

@Service
public class InstituteServiceImpl implements InstituteService {
    private final InstituteRepository instituteRepository;

    public InstituteServiceImpl(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
    }

    public Institute getInstituteById(Long id) {
        return instituteRepository.findInstituteById(id).orElseThrow(() -> new InstituteDoesNotExistException(id));
    }
}

