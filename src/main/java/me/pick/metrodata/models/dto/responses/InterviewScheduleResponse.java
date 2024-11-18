package me.pick.metrodata.models.dto.responses;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import me.pick.metrodata.enums.InterviewStatus;
import me.pick.metrodata.enums.InterviewType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewScheduleResponse {

    private Long id;

    private String clientUserFirstName;

    private String clientUserLastName;

    private String applicantTalentName;

    private String position;

    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;
}
