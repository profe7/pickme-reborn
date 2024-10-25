package me.pick.metrodata.services.interviewSchedule;

import me.pick.metrodata.models.dto.responses.InterviewSchedulePaginationResponse;

public interface InterviewScheduleService {
	public InterviewSchedulePaginationResponse getAll(String search, Long recruiter, Boolean online,
													  String startDate, String endDate, String status,
													  Integer currentPage, Integer perPage);
}
