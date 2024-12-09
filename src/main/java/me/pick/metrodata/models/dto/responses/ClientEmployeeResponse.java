package me.pick.metrodata.models.dto.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEmployeeResponse {

    private String talentId;

    private String employeeName;

    private String position;

    private LocalDate onboardDate;

    private String clientName;
}
