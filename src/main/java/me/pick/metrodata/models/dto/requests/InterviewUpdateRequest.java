package me.pick.metrodata.models.dto.requests;

import lombok.Data;
import me.pick.metrodata.enums.InterviewStatus;

@Data
public class InterviewUpdateRequest {
    private Long interviewId;

    private InterviewStatus status;

    private String feedback;
}
