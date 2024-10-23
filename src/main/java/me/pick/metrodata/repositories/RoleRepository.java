package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByNameContaining(String name, Pageable pageable);

    Long countByNameContaining(String name);
}