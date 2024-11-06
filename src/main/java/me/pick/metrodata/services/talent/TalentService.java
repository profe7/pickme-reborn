package me.pick.metrodata.services.talent;

import me.pick.metrodata.models.dto.requests.TalentDataCompletionRequest;
import me.pick.metrodata.models.dto.requests.TalentFromVacancyRequest;
import me.pick.metrodata.models.dto.responses.TalentAvailableForVacancyResponse;
import me.pick.metrodata.models.dto.responses.TalentResponse;
import me.pick.metrodata.models.entity.Talent;
import org.springframework.data.domain.Page;


public interface TalentService {
    TalentResponse getById(String id);

    Talent createViaVacancy(TalentFromVacancyRequest request);

    Talent completeNewTalentData(TalentDataCompletionRequest request);

    TalentAvailableForVacancyResponse availableForVacancy(Long vacancyId, Long mitraId);

    Talent createNewTalent(TalentDataCompletionRequest request);

    Page<Talent> getAll(Integer page, Integer size, String search, Long institute, Long baseSalary, Long limitSalary, Boolean active, String job, String skill, Boolean idle);
}
