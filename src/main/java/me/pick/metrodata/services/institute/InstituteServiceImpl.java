package me.pick.metrodata.services.institute;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.enums.InstituteType;
import me.pick.metrodata.exceptions.institute.InstituteDoesNotExistException;
import me.pick.metrodata.exceptions.institute.InstituteTypeDoesNotExistException;
import me.pick.metrodata.models.dto.requests.InstituteUpdateRequest;
import me.pick.metrodata.models.dto.responses.InstituteResponse;
import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.repositories.InstituteRepository;
import me.pick.metrodata.repositories.UserRepository;
import me.pick.metrodata.repositories.specifications.InstituteSpecification;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import me.pick.metrodata.models.dto.requests.InstituteRequest;

@Service
@RequiredArgsConstructor
public class InstituteServiceImpl implements InstituteService {

    private final InstituteRepository instituteRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public Institute getInstituteById(Long id) {
        return instituteRepository.findById(id).orElseThrow(() -> new InstituteDoesNotExistException(id));
    }

    @Override
    public Page<Institute> getAllInstitutes(String name, Long instituteTypeId, Integer currentPage, Integer perPage) {
        return instituteRetrievalHelper(name, instituteTypeId, currentPage, perPage);
    }

    @Override
    public void editInstitute(InstituteUpdateRequest request, Long id) {
        Institute institute = getInstituteById(id);
        institute.setInstituteName(request.getName());
        try {
            institute.setInstituteType(InstituteType.valueOf(request.getInstituteType()));
        } catch (IllegalArgumentException e) {
            throw new InstituteTypeDoesNotExistException(request.getInstituteType());
        }
    }

    private Page<Institute> instituteRetrievalHelper(String name, Long instituteTypeId, Integer currentPage,
            Integer perPage) {
        Specification<Institute> spec = InstituteSpecification.searchSpecification(name, instituteTypeId, null);
        List<Institute> institutes = instituteRepository.findAll(spec);
        Pageable pageable = PageRequest.of(currentPage, perPage);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), institutes.size());

        return new PageImpl<>(institutes.subList(start, end), pageable, institutes.size());
    }

    @Override
    public List<Institute> getAll() {
        return instituteRepository.findAll();
    }

    @Override
    public Page<InstituteResponse> getFilteredInstitute(String searchName, InstituteType searchType, Integer page,
            Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return instituteRepository.findAllWithFilters(
                searchName, searchType, pageable).map(institute -> {
                    InstituteResponse instituteResponse = modelMapper.map(
                            institute, InstituteResponse.class);
                    return instituteResponse;
                });
    }

    @Override
    public void create(InstituteRequest instituteRequest) {
        Institute institute = modelMapper.map(instituteRequest, Institute.class);
        institute.setInstituteType(InstituteType.valueOf(instituteRequest.getInstituteType()));
        institute.setRmUser(userRepository.findById(instituteRequest.getRmId()).orElse(null));

        instituteRepository.save(institute);
    }

    @Override
    public void update(Long id, InstituteRequest instituteRequest) {
        Institute institute = getInstituteById(id);
        institute.setInstituteName(instituteRequest.getInstituteName());
        institute.setInstituteType(InstituteType.valueOf(instituteRequest.getInstituteType()));
        institute.setRmUser(userRepository.findById(instituteRequest.getRmId()).orElse(null));

        instituteRepository.save(institute);
    }

}
