package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.OtherExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherExperienceRepository extends JpaRepository<OtherExperience, Long> {
}
