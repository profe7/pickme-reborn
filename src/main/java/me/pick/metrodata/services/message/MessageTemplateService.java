package me.pick.metrodata.services.message;

import org.springframework.data.domain.Page;

import me.pick.metrodata.models.entity.MessageTemplate;

public interface MessageTemplateService {

    Page<MessageTemplate> getFilteredMessageTemplate(Integer page, Integer size);
}
