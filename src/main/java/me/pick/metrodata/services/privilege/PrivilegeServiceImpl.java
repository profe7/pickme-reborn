package me.pick.metrodata.services.privilege;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.entity.Privilege;
import me.pick.metrodata.repositories.PrivilegeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    public List<Privilege> getAll() {
        return privilegeRepository.findAll();
    }
}
