package me.pick.metrodata.services.interviewhistory;

import java.util.List;

import org.springframework.stereotype.Service;

import me.pick.metrodata.models.dto.responses.InterviewHistoryResponse;
import me.pick.metrodata.models.dto.responses.InterviewScheduleHistoryResponse;

@Service
public interface InterviewScheduleHistoryService {

    List<InterviewScheduleHistoryResponse> getByInterviewScheduleId(Long id);

    List<InterviewHistoryResponse> getByTalentIdandClientId(String talentId, Long clientId);
}
