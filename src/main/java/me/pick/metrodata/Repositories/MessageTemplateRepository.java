package me.pick.metrodata.Repositories;

import me.pick.metrodata.enums.MessageTemplateEnum;
import me.pick.metrodata.models.entity.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
  public MessageTemplate findByType(MessageTemplateEnum messageTemplateEnum);

  public MessageTemplate findByTypeAndIdNot(MessageTemplateEnum messageTemplateEnum, Long id);
}