package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
}
