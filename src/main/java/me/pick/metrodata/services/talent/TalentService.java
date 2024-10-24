package me.pick.metrodata.services.talent;

import me.pick.metrodata.models.dto.requests.TalentDataCompletionRequest;
import me.pick.metrodata.models.dto.requests.TalentFromVacancyRequest;
import me.pick.metrodata.models.entity.Talent;

import java.util.List;

public interface TalentService {
    Talent createViaVacancy(TalentFromVacancyRequest request);

    Talent completeNewTalentData(TalentDataCompletionRequest request);

    List<Talent> availableForVacancy(Long vacancyId, Long mitraId);
}
