package me.pick.metrodata.services.role;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.privilege.PrivilegeDoesNotExistException;
import me.pick.metrodata.exceptions.role.RoleDoesNotExistException;
import me.pick.metrodata.models.dto.requests.RoleUpdateRequest;
import me.pick.metrodata.models.dto.responses.RolePaginationResponse;
import me.pick.metrodata.models.dto.responses.RoleResponse;
import me.pick.metrodata.models.entity.Role;
import me.pick.metrodata.repositories.PrivilegeRepository;
import me.pick.metrodata.repositories.RoleRepository;
import me.pick.metrodata.utils.AnyUtil;
import me.pick.metrodata.utils.PageData;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final ModelMapper modelMapper;

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

    @Override
    public void updateRole(RoleUpdateRequest roleUpdateRequest) {
        Role role = roleRepository.findById(roleUpdateRequest.getId())
                .orElseThrow(() -> new RoleDoesNotExistException(roleUpdateRequest.getId()));
        role.setName(roleUpdateRequest.getName());
        for (Long privilegeId : roleUpdateRequest.getPrivilegeIds()) {
            role.getPrivileges().add(privilegeRepository.findById(privilegeId)
                    .orElseThrow(() -> new PrivilegeDoesNotExistException(privilegeId)));
        }
        roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RoleDoesNotExistException(id));
    }

    @Override
    public Role createRole(RoleUpdateRequest roleUpdateRequest) {
        Role role = new Role();
        role.setName(roleUpdateRequest.getName());
        for (Long privilegeId : roleUpdateRequest.getPrivilegeIds()) {
            role.getPrivileges().add(privilegeRepository.findById(privilegeId)
                    .orElseThrow(() -> new PrivilegeDoesNotExistException(privilegeId)));
        }
        return roleRepository.save(role);
    }

    @Override
    public Page<RoleResponse> getFilteredRole(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return roleRepository.findAllWithFilters(pageable).map(role -> modelMapper.map(role,
                RoleResponse.class));
    }
}
