package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementsRepository extends JpaRepository<Achievements, Long> {
}
