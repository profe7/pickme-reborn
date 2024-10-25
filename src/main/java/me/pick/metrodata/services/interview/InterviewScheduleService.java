package me.pick.metrodata.services.interview;

import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import org.springframework.stereotype.Service;

@Service
public interface InterviewScheduleService {
    void inviteToInterview(InterviewScheduleRequest request);
}
