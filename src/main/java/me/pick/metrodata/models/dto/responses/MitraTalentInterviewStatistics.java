package me.pick.metrodata.models.dto.responses;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MitraTalentInterviewStatistics {
    private String talentName;

    private String feedback;

    private LocalDateTime updatedAtDate;

    private LocalDate onboardDate;

    private String recruiterName;
}
