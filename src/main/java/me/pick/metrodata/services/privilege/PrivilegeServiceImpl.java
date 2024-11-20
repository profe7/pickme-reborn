package me.pick.metrodata.services.privilege;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.entity.Privilege;
import me.pick.metrodata.repositories.PrivilegeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    @Override
    public List<Privilege> getAll() {
        return privilegeRepository.findAll();
    }

    @Override
    public Map<String, List<Privilege>> getAllStructuredPrivilegeResponses() {
        List<Privilege> privileges = getAll();
        Map<String, List<Privilege>> groupedPrivileges = privileges.stream()
                .collect(Collectors.groupingBy(Privilege::getType));
        return groupedPrivileges;
    }
}
