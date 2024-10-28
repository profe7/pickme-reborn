package me.pick.metrodata.services.interview;

import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;
import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.models.entity.InterviewSchedule;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface InterviewScheduleService {
    void inviteToInterview(InterviewScheduleRequest request);

    Page<InterviewSchedule> getAll(String search, Long clientId, InterviewType type, String startDate, String endDate, InterviewStatus status, int page, int size);

    Page<InterviewSchedule> getByRm(String search, Long clientId, InterviewType type, String startDate, String endDate, InterviewStatus status, int page, int size);
}
