package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Mitra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MitraRepository extends JpaRepository<Mitra, Long> {
}
