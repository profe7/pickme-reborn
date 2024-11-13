package me.pick.metrodata.services.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.entity.MessageTemplate;
import me.pick.metrodata.repositories.MessageTemplateRepository;

@RequiredArgsConstructor
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private final MessageTemplateRepository messageTemplateRepository;

    @Override
    public Page<MessageTemplate> getFilteredMessageTemplate(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageTemplateRepository.findAllWithFilters(pageable);
    }
}
