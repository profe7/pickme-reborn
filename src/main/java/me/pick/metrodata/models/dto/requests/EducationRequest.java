package me.pick.metrodata.models.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
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

    @NotNull
    private EducationalLevel educationalLevel;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @NotNull
    private Float academicGrade;

    @NotNull
    private String institution;

    @NotNull
    private Long majorId;

    @NotNull
    private String talentId;

}
