package me.pick.metrodata.services.interview;

import me.pick.metrodata.models.dto.requests.InterviewScheduleRequest;
import me.pick.metrodata.repositories.InterviewScheduleRepository;

public class InterviewScheduleServiceImpl implements InterviewScheduleService {
    private final InterviewScheduleRepository interviewScheduleRepository;

    public InterviewScheduleServiceImpl(InterviewScheduleRepository interviewScheduleRepository) {
        this.interviewScheduleRepository = interviewScheduleRepository;
    }

    @Override
    public void inviteToInterview(InterviewScheduleRequest request) {
    }
}
