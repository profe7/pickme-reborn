package me.pick.metrodata.services.talent;

import me.pick.metrodata.models.dto.requests.TalentDataCompletionRequest;
import me.pick.metrodata.models.dto.requests.TalentFromVacancyRequest;
import me.pick.metrodata.models.dto.responses.TalentAvailableForVacancyResponse;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.dto.responses.TotalMitraTalentResponse;
import me.pick.metrodata.models.entity.Talent;

import java.util.List;

public interface TalentService {
    TalentResponse getById(String id);

    List<TotalMitraTalentResponse> getTotalByMitra();

    Talent createViaVacancy(TalentFromVacancyRequest request);

    Talent completeNewTalentData(TalentDataCompletionRequest request);

    TalentAvailableForVacancyResponse availableForVacancy(Long vacancyId, Long mitraId);

    Talent createNewTalent(TalentDataCompletionRequest request);
}
