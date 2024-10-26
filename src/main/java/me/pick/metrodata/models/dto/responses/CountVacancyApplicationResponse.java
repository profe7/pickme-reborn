package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.models.entity.Vacancy;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountVacancyApplicationResponse {
	private Vacancy vacancy;
	private Long totalNominee;
}
