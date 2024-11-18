package me.pick.metrodata.models.dto.responses;

import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.InterviewStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewScheduleHistoryResponse {

    private Long id;

    private LocalDate created_at;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    private String feedback;
}
