package me.pick.metrodata.services.role;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.responses.RolePaginationResponse;
import me.pick.metrodata.models.entity.Role;
import me.pick.metrodata.repositories.RoleRepository;
import me.pick.metrodata.utils.AnyUtil;
import me.pick.metrodata.utils.PageData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RolePaginationResponse getAll(String name, Integer currentPage, Integer perPage) {
        currentPage = currentPage == null ? 0 : currentPage;
        perPage = perPage == null ? 10 : perPage;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("")
                .queryParam("name", name);
        Pageable pageable = PageRequest.of(currentPage, perPage);

        List<Role> roles = roleRepository.findByNameContaining(name, pageable);

        Integer totalRole = roleRepository.countByNameContaining(name).intValue();

        PageData pageData = AnyUtil.pagination(totalRole, currentPage, perPage, uriBuilder);

        return new RolePaginationResponse(pageData, roles);
    }
}
