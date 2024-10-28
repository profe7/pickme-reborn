package me.pick.metrodata.models.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class InterviewScheduleSimpleResponse {
    private String client;

    private String talent;

    private String position;

    private String type;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String date;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private String startTime;

    @DateTimeFormat
    @JsonFormat(pattern = "HH:mm")
    private String endTime;

    private String status;
}
