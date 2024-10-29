package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.models.entity.Vacancy;
import me.pick.metrodata.utils.PageData;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VacancyPaginationResponse {
	private PageData pageData;
	private List<Vacancy> data;
}
