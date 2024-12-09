package me.pick.metrodata.services.interview;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.models.dto.requests.InterviewUpdateRequest;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.models.entity.InterviewScheduleHistory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InterviewScheduleService {
    void inviteToInterview(InterviewScheduleRequest request);

    void updateInterviewStatus(InterviewUpdateRequest request);

    Page<InterviewSchedule> getAll(String search, Long clientId, InterviewType type, String startDate, String endDate, InterviewStatus status, int page, int size);

    Page<InterviewSchedule> getByRm(String search, Long clientId, InterviewType type, String startDate, String endDate, InterviewStatus status, int page, int size);

    List<InterviewScheduleHistory> getTalentInterviewHistory(Long interviewId);
}
