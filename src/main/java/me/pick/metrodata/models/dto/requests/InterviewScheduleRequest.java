package me.pick.metrodata.models.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleRequest {

    private String position;

    private String locationAddress;

    private String interviewLink;

    private String interviewType;

    private String message;

    private String status;

    private String feedback;

    private Long clientId;

    private Long applicantId;

    private String date;

    private String onBoardDate;

    private String startTime;

    private String endTime;

    private String talentId;
}
