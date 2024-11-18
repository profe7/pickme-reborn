package me.pick.metrodata.models.dto.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterviewHistoryResponse {
    private String namaTalent;
    private String status;
    private LocalDate dateChanges;
}
