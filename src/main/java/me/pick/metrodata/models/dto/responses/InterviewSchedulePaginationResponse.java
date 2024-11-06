package me.pick.metrodata.models.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.pick.metrodata.models.entity.InterviewSchedule;
import me.pick.metrodata.utils.PageData;

@Data
@AllArgsConstructor
public class InterviewSchedulePaginationResponse {

    private PageData pageData;

    private List<InterviewSchedule> data;

}
