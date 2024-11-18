package me.pick.metrodata.repositories;

import me.pick.metrodata.enums.MessageTemplateEnum;
import me.pick.metrodata.models.entity.MessageTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {

    MessageTemplate findByType(MessageTemplateEnum messageTemplateEnum);

    MessageTemplate findByTypeAndIdNot(MessageTemplateEnum messageTemplateEnum, Long id);

    @Query("SELECT mt FROM MessageTemplate mt")
    Page<MessageTemplate> findAllWithFilters(Pageable pageable);
}
