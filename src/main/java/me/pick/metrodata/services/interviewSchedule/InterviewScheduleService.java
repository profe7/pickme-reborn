package me.pick.metrodata.services.interviewSchedule;

import me.pick.metrodata.models.dto.responses.AcceptedTalentsByRecruiterCountPerPositionResponse;
import me.pick.metrodata.models.dto.responses.InterviewSchedulePaginationResponse;

public interface InterviewScheduleService {
	InterviewSchedulePaginationResponse getAll(String search, Long recruiter, Boolean online,
													  String startDate, String endDate, String status,
													  Integer currentPage, Integer perPage);

	 AcceptedTalentsByRecruiterCountPerPositionResponse getAllAcceptedTalentsByRecruiterCountPerPosition();
}
