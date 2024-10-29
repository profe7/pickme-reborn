package me.pick.metrodata.controllers;

import lombok.AllArgsConstructor;
import me.pick.metrodata.models.dto.responses.CountVacancyApplicantPaginationResponse;
import me.pick.metrodata.models.dto.responses.TotalMitraTalentResponse;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.services.interviewSchedule.InterviewScheduleService;
import me.pick.metrodata.services.talent.TalentService;
import me.pick.metrodata.services.user.UserService;
import me.pick.metrodata.services.vacancy.VacancyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping ("/mitra")
@PreAuthorize ("hasAuthority('READ_APPLICANT_NOMINEE') and hasAuthority('CREATE_APPLICANT_NOMINEE') and hasAuthority('UPDATE_APPLICANT_NOMINEE') and hasAuthority('DELETE_APPLICANT_NOMINEE') and hasAuthority('READ_JOB')")
public class HomeMitraController {
	private UserService userService;
	private TalentService talentService;
	private VacancyService vacancyService;

	@GetMapping
	public String mitra(
			@RequestParam (required = false, defaultValue = "") String timeInterval,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			Model model
	) {

		String fName = userService.getLoggedUserData().getFirstName();
		String lName = userService.getLoggedUserData().getLastName();

		String logged = String.join(" ", fName, lName);

		List<TotalMitraTalentResponse> totalsTalent = talentService.getTotalByMitra ();
		CountVacancyApplicantPaginationResponse totalApplicantByJob = vacancyService.getVacanciesWithTotalNominee(
				timeInterval, page, size);

		List<Vacancy> jobs = vacancyService.getAllVacancies ();

		model.addAttribute("logged", logged);
		model.addAttribute("isActive", "home");
		model.addAttribute("totals", totalsTalent);
		model.addAttribute("totalApplicantByJob", totalApplicantByJob);
		model.addAttribute("total_jobs", jobs.size());
		model.addAttribute("jobs", jobs);
		model.addAttribute("isActive", "mitra");

		return "mitra";
	}
}
