package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Skill;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("SELECT DISTINCT s.name FROM Skill s")
    List<String> findAllDistinctSkill();
}
