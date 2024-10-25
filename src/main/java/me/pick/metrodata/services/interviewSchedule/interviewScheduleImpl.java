package me.pick.metrodata.services.interviewSchedule;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.models.dto.responses.InterviewSchedulePaginationResponse;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.repositories.InterviewScheduleRepository;
import me.pick.metrodata.repositories.specifications.InterviewScheduleSpecification;
import me.pick.metrodata.utils.PageData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class interviewScheduleImpl implements InterviewScheduleService{
	private final InterviewScheduleRepository interviewScheduleRepository;

	public InterviewSchedulePaginationResponse getAll(String search, Long recruiter, Boolean online,
													  String startDate, String endDate, String status,
													  Integer currentPage, Integer perPage) {
		currentPage = currentPage == null ? 0 : currentPage;
		perPage = perPage == null ? 10 : perPage;

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("")
				.queryParam("search", search)
				.queryParam("recruiter", recruiter)
				.queryParam("online", online)
				.queryParam("startDate", startDate)
				.queryParam("endDate", endDate)
				.queryParam("status", status);

		Pageable pageable = PageRequest.of(currentPage, perPage, Sort.by("updatedAt").descending());

		Specification<InterviewSchedule> spec = InterviewScheduleSpecification.searchSpecification(search, recruiter,
				online, startDate, endDate, status, null);
		List<InterviewSchedule> interviews = interviewScheduleRepository.findAll(spec, pageable).getContent();
		Integer totalInterview = (int) interviewScheduleRepository.count(spec);

		PageData pageData = new PageData(totalInterview, perPage, currentPage,
				(int) Math.ceil((double) totalInterview / perPage),
				uriBuilder.replaceQueryParam("currentPage", currentPage - 1).toUriString(),
				uriBuilder.replaceQueryParam("currentPage", currentPage + 1).toUriString());

		return new InterviewSchedulePaginationResponse (pageData, interviews);
	}
}
