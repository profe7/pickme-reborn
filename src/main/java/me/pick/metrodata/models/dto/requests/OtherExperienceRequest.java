package me.pick.metrodata.models.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherExperienceRequest {

    private String experienceName;

    private String instituteName;

    private String positionName;

    private String description;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate experienceDate;

    private String talentId;
}

