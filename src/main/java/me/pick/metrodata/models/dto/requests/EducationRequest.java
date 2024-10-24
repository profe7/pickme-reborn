package me.pick.metrodata.models.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.pick.metrodata.enums.EducationalLevel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EducationRequest {
    private EducationalLevel educationalLevel;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private Float academicGrade;

    private String institution;

    private Long majorId;

    private String talentId;
}

