package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.responses.RecommendationGroupedResponse;
import me.pick.metrodata.services.interviewSchedule.InterviewScheduleService;
import me.pick.metrodata.services.recommendation.RecommendationService;
import me.pick.metrodata.services.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@PreAuthorize ("hasAuthority('READ_INTERVIEW') and hasAuthority('CREATE_INTERVIEW') and hasAuthority('UPDATE_INTERVIEW') and hasAuthority('DELETE_INTERVIEW')")
public class HomeController {
	private UserService userService;
	private InterviewScheduleService interviewScheduleService;
	private RecommendationService recommendationService;

	@GetMapping
	public String home(Model model) {
		String institute = userService.getLoggedUserData().getInstitute().getInstituteName ();
		String fName = userService.getLoggedUserData().getFirstName();
		String lName = userService.getLoggedUserData().getLastName();

		String logged = String.join(" ", fName, lName);

		List<RecommendationGroupedResponse> recommendations = recommendationService.getAllByInstituteOrUser ();
		if (recommendations == null) {
			recommendations = new ArrayList<> ();
		}

		var totalCount = interviewScheduleService.getAllAcceptedTalentsByRecruiterCountPerPosition ();
		Integer totalTalents = totalCount.getTotal();
		Map<String, Integer> totalTalentsByCategory = totalCount.getTotalByCategory();

		Map<String, Integer> limitedTotalTalentsByCategory = totalTalentsByCategory.entrySet()
				.stream()
				.limit(3)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		model.addAttribute("institute", institute);
		model.addAttribute("totalTalents", totalTalents);
		model.addAttribute("totalTalentsByCategory", limitedTotalTalentsByCategory);
		model.addAttribute("logged", logged);
		model.addAttribute("recommendations", recommendations);
		model.addAttribute("isActive", "home");

		return "index";
	}
}
