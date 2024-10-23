package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.LanguageSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageSkillRepository extends JpaRepository<LanguageSkill, Long> {
}
