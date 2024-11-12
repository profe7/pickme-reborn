package me.pick.metrodata.models.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.models.entity.Applicant;
import java.time.LocalDate;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadVacancyDetailResponse{
    String title;
    String position;
    LocalDate expiredDate;
    String description;
    List<ReadApplicantResponse> applicants;
}