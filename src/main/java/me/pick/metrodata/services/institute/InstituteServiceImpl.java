package me.pick.metrodata.services.institute;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.institute.InstituteDoesNotExistException;
import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.repositories.InstituteRepository;
import me.pick.metrodata.repositories.specifications.InstituteSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstituteServiceImpl implements InstituteService {
    private final InstituteRepository instituteRepository;

    public Institute getInstituteById(Long id) {
        return instituteRepository.findInstituteById(id).orElseThrow(() -> new InstituteDoesNotExistException(id));
    }

    public Page<Institute> getAllInstitutes(String name, Long instituteTypeId, Integer currentPage, Integer perPage) {
        return instituteRetrievalHelper(name, instituteTypeId, currentPage, perPage);
    }

    private Page<Institute> instituteRetrievalHelper(String name, Long instituteTypeId, Integer currentPage, Integer perPage) {
        Specification<Institute> spec = InstituteSpecification.searchSpecification(name, instituteTypeId, null);
        List<Institute> institutes = instituteRepository.findAll(spec);
        Pageable pageable = PageRequest.of(currentPage, perPage);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), institutes.size());

        return new PageImpl<>(institutes.subList(start, end), pageable, institutes.size());
    }
}

