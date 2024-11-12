package me.pick.metrodata.models.dto.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class InterviewScheduleCalendarResponse {
    private Long id;

    private String title;

    private LocalDate start;

    private LocalDate end;

    private String backgroundColor;

    private boolean allDay;

    private boolean editable;
}
