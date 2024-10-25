package me.pick.metrodata.controllers.rest;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.responses.InterviewSchedulePaginationResponse;
import me.pick.metrodata.services.interviewSchedule.InterviewScheduleService;
import me.pick.metrodata.utils.StringUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/v1/interview-schedule")
@RequiredArgsConstructor
public class RestInterviewSchedule {

	private final InterviewScheduleService interviewScheduleService;

	@GetMapping
	@PreAuthorize  ("hasAnyAuthority('READ_INTERVIEW')")
	public InterviewSchedulePaginationResponse getAll(
			@RequestParam (required = false, defaultValue = "") String search,
			@RequestParam(required = false) Long recruiter,
			@RequestParam(required = false) Boolean online,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		search = StringUtil.decodeUrlParameter(search);
		return interviewScheduleService.getAll(search, recruiter, online, startDate, endDate, status, page,
				size);
	}

}
